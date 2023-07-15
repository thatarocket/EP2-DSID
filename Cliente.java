import Global.IAgencia;
import Global.IServicoNomes;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.Naming;
import java.util.HashMap;
import java.util.Scanner;

public class Cliente {

    static Scanner scanner = new Scanner(System.in);
    static ContextoAtual contextoAtual;

    public static void main(String[] args) {
        try {
            contextoAtual = new ContextoAtual();
            encontrarServico();
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void encontrarServico() throws Exception {
        System.out.println("Conecte com o servico de nomes");
        System.out.println("Digite o numero da porta: ");
        int numPorta = Integer.parseInt(scanner.nextLine());
        String objName = "rmi://localhost:" + numPorta + "/servicoNomes";

        contextoAtual.servicoNomes = (IServicoNomes) Naming.lookup(objName);
        contextoAtual.portaServico = numPorta;
        System.out.println("Conectado com sucesso!");
        encontrarAgencia();
    }

    private static void encontrarAgencia() throws Exception {
        System.out.println("Agencias disponiveis: ");
        HashMap<String, Integer> listaAgencias = contextoAtual.servicoNomes.getAgencias();
        for (String i : listaAgencias.keySet()) {
            System.out.println("agencia: " + i + " porta: " + listaAgencias.get(i));
        }

        System.out.println("Digite o nome da agencia a ser conectada: ");
        String nomeAgencia = scanner.nextLine();

        int numPorta = listaAgencias.get(nomeAgencia);
        if (!isPortaEmUso(numPorta)) {
            // porta nao esta em uso
            System.out.println(
                    "Hmm parece que a agencia cadastrada nesta porta ja nao se encontra em operacao, tente logar em outra ");
            contextoAtual.servicoNomes.removerAgencia(nomeAgencia);
            encontrarServico();
        }

        String objName = "rmi://localhost:" + numPorta + "/" + nomeAgencia;

        contextoAtual.agenciaAtual = (IAgencia) Naming.lookup(objName);
        contextoAtual.idAgencia = nomeAgencia;
        contextoAtual.portaAgencia = numPorta;

        System.out.println("Conectado com sucesso!");
        opcoes();
    }

    public static void opcoes() throws Exception {
        System.out.println("--------------------------------------------------");
        System.out.println("Escolha a opcao desejada! (Pelo numero)");
        System.out.println("1 - CONECTAR com outra agencia"); // Faz o cliente se conectar com uma agencia
        System.out.println("2 - DESCONECTAR da agencia"); // Desconectar da agência
        System.out.println("3 - CRIAR agente em agencia"); // Envia o código do agente para a agência
        System.out.println("4 - ENVIAR mensagem a agente ");
        System.out.println("5 - INTERROMPER execução de agente ");
        System.out.println("6 - SAIR do programa"); // Encerra a execucao do cliente
        selecionar();
    }

    public static void selecionar() throws Exception {
        System.out.println("INSIRA A OPCAO DESEJADA: ");
        int input = Integer.parseInt(scanner.nextLine());

        switch (input) {
            case 1:
                desconectar();
                encontrarAgencia();
                opcoes();
                break;
            case 2:
                desconectar();
                opcoes();
                break;
            case 3:
                criarAgente();
                opcoes();
                break;
            case 4:
                enviarMensagemAgente();
                opcoes();
                break;
            case 5:
                interromperExecucaoAgente();
                opcoes();
                break;
            case 6:
                quit();
                break;
            default:
                System.out.println("Opcao invalida! Coloque uma opcao valida:");
                opcoes();
        }
    }

    private static void criarAgente() throws Exception {
        File arquivoClass = compilarAgente();
        String idAgente = contextoAtual.agenciaAtual.adicionarAgente(arquivoClass);
        contextoAtual.idAgente = idAgente;
        System.out.println("Agente criado com sucesso! Id: " + idAgente);
    }

    private static File compilarAgente() throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = compiler.run(null, null, null, "Global/Agente.java");

        if (compilationResult != 0)
            throw new Exception("Erro ao compilar o agente.");
        System.out.println("Agente compilado com sucesso!");
        return new File("Global/Agente.class");
    }

    private static void enviarMensagemAgente() throws Exception {
        if (!estaConectado())
            throw new Exception("Voce nao esta conectado a nenhuma agencia.");

        System.out.println("Digite o id do agente para receber a mensagem: ");
        String idAgente = scanner.nextLine();
        System.out.println("Digite a mensagem a ser enviada: ");
        String mensagem = scanner.nextLine();

        String resposta = contextoAtual.agenciaAtual.enviarMensagem(mensagem, idAgente);
        System.out.println(resposta);
    }

    private static void interromperExecucaoAgente() throws Exception {
        if (!estaConectado())
            throw new Exception("Voce nao esta conectado a nenhuma agencia.");

        System.out.println("Digite o id do agente a ser interrompido: ");
        String idAgente = scanner.nextLine();

        contextoAtual.agenciaAtual.removerAgente(idAgente);
    }

    private static void desconectar() throws Exception {
        if (!estaConectado())
            throw new Exception("Voce nao esta conectado a nenhuma agencia.");
        String objName = "rmi://localhost:" + contextoAtual.portaAgencia + "/" + contextoAtual.idAgencia;
        Naming.unbind(objName);

        contextoAtual.agenciaAtual = null;
        contextoAtual.idAgencia = null;
        contextoAtual.portaAgencia = -1;
    }

    private static void quit() {
        System.out.println("Encerrando...");
        System.exit(0);
    }

    private static boolean estaConectado() {
        return contextoAtual.agenciaAtual != null;
    }

    public static boolean isPortaEmUso(int porta) {
        try {
            // Tenta criar um soquete na porta especificada
            ServerSocket serverSocket = new ServerSocket(porta);
            serverSocket.close(); // Fecha o soquete imediatamente se a criação for bem-sucedida
            return false; // A porta não está em uso
        } catch (IOException e) {
            return true; // A porta está em uso
        }
    }

}
