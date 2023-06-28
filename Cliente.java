
import Global.IAgencia;
import Global.IServicoNomes;

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
        }
        catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void encontrarServico() throws Exception {
        System.out.println("--------------------------------------------------");
        System.out.println("Conecte com o serviço de nomes");
        System.out.println("Digite o numero da porta: ");
        int numPorta = Integer.parseInt(scanner.nextLine());
        String objName = "rmi://localhost:" + numPorta + "/servicoNomes";

        contextoAtual.servicoNomes = (IServicoNomes) Naming.lookup(objName);
        contextoAtual.portaServico = numPorta;
        System.out.println("Conectado com sucesso!");

        System.out.println("Agencias disponiveis: ");
        HashMap<String,Integer> listaAgencias =  contextoAtual.servicoNomes.getAgencias();
        for (String i : listaAgencias.keySet()) {
            System.out.println("agência: " + i + " porta: " + listaAgencias.get(i));
        }
        encontrarAgencia();
    }

    private static void encontrarAgencia() throws Exception {
        System.out.println("--------------------------------------------------");
        System.out.println("Digite o nome da agencia a ser conectada: ");
        String nomeAgencia = scanner.nextLine();
        System.out.println("Digite o numero da porta: ");
        int numPorta = Integer.parseInt(scanner.nextLine());

        String objName = "rmi://localhost:" + numPorta + "/" + nomeAgencia;
        contextoAtual.agenciaAtual = (IAgencia) Naming.lookup(objName);
        contextoAtual.idAgencia = nomeAgencia;
        contextoAtual.portaAgencia = numPorta;
        System.out.println("Conectado com sucesso!");
        opcoes();
    }

    public static void opcoes() throws Exception{
        System.out.println("--------------------------------------------------");
        System.out.println("Escolha a opcao desejada! (Pelo numero)");
        System.out.println("1 - conectar com uma agência");        // Faz o cliente se conectar com uma agênncia
        System.out.println("2 - desconectar da agência");     // Desconectar da agência
        System.out.println("3 - sair do programa");       // Encerra a execucao do cliente
        System.out.println("4 - teste");      // Teste
        System.out.println("5 - Criar agente em agencia"); //Envia o código do agente para a agência
        System.out.println("6 - Enviar mensagem a agente ");
        System.out.println("7 - Interromper execução de agente ");
        selecionar();
    }

    public static void selecionar() throws Exception {
        System.out.println("INSIRA A OPCAO DESEJADA: ");
        int input = Integer.parseInt(scanner.nextLine());

        switch (input) {
            case 1 -> encontrarAgencia();
            case 2 -> desconectar();
            case 3 -> quit();
            case 4 -> teste();
            case 5,6, 7 -> System.out.println("Ainda nao implementado");
            default -> {
                System.out.println("Opcao invalida! Coloque uma opcao valida:");
                opcoes();
            }
        }
    }

    public static void desconectar() throws Exception {
        if(!estaConectado()) throw new Exception("Voce nao esta conectado a nenhuma agencia.");
        contextoAtual.agenciaAtual = null;
        contextoAtual.idAgencia = null;
        contextoAtual.portaAgencia = -1;
        System.out.println("Desconectado com sucesso!");
        opcoes();
    }

    public static void quit() {
        System.out.println("Encerrando...");
        System.exit(0);
    }

    private static boolean estaConectado() {
        return contextoAtual.agenciaAtual != null;
    }

    public static void teste() throws Exception{
        if(!estaConectado()) throw new Exception("Voce nao esta conectado a nenhuma agencia.");
        System.out.println("--------------------------------------------------");
        String teste = contextoAtual.agenciaAtual.teste();
        System.out.println(teste);
    }


}
