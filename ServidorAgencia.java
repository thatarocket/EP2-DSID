import Global.Agencia;
import Global.IAgencia;
import Global.IServicoNomes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Scanner;

public class ServidorAgencia {
    static Scanner scanner = new Scanner(System.in);
    static IServicoNomes servicoNomes;
    static final String NOME_SERVICO_NOMES = "servicoNomes";

    public static void main(String[] args) throws Exception {
        try {
            conectarServicoNomes();
            criarAgencia();
            System.out.println("Aguardando usuarios...");
        } catch (Exception e) {
            System.err.println("Erro no servidor da agencia: " + e);
            e.printStackTrace();
            criarAgencia();
        }
    }

    private static void conectarServicoNomes() throws Exception {
        System.out.println("Digite o numero da porta do servidor de nomes: ");
        int portaServidor = Integer.parseInt(scanner.nextLine());
        String objServico = "rmi://localhost:" + portaServidor + "/" + NOME_SERVICO_NOMES;

        System.out.println("Conectando com o servico de nomes...");
        servicoNomes = (IServicoNomes) Naming.lookup(objServico);
        System.out.println("Conectado com o servico de nomes!");
    }

    private static void criarAgencia() throws Exception {
        System.out.println("Digite o numero da porta da agencia a ser criada: ");
        int portaAgencia = Integer.parseInt(scanner.nextLine());
        if (isPortaEmUso(portaAgencia)) {
            System.out.println("Erro ao registrar agencia! Porta ja esta sendo utilizada, escolha alguma outra");
            criarAgencia();
        }

        String idAgencia = servicoNomes.registrarAgencia(portaAgencia);

        if (idAgencia == null) {
            System.out.println("Escolha outra porta para criar a agencia!");
            criarAgencia();
        } else if (idAgencia.contentEquals("ERRO_PORTA")) {
            System.out.println("Ja existe uma agencia nessa porta, deseja exclui-la e cadastrar outra?");
            System.out.println("Escolha a opcao desejada! (Pelo numero)");
            System.out.println("1 - Sim! Limpar porta e cadastrar outra agencia nela");
            System.out.println("2 - Nao! Cadastrarei com outra porta");
            selecionar(idAgencia, portaAgencia);
            return;
        }
        conectarNaPorta(idAgencia, portaAgencia);
    }

    public static void selecionar(String idAgencia, int numPorta) throws Exception {
        System.out.println("INSIRA A OPCAO DESEJADA: ");
        int input = Integer.parseInt(scanner.nextLine());

        switch (input) {
            case 1:
                servicoNomes.removerAgenciaPorPorta(numPorta);
                String idAgencia_ = servicoNomes.gerarIdAgencia();
                servicoNomes.getAgencias().put(idAgencia_, numPorta);
                conectarNaPorta(idAgencia_, numPorta);
                break;
            case 2:
                criarAgencia();
                break;
            default:
                break;
        }
    }

    private static void conectarNaPorta(String idAgencia, int portaAgencia)
            throws RemoteException, MalformedURLException {
        IAgencia agencia = new Agencia(idAgencia);
        LocateRegistry.createRegistry(portaAgencia);
        String objAgencia = "rmi://localhost:" + portaAgencia + "/" + idAgencia;
        Naming.rebind(objAgencia, agencia);
        agencia.setServicoNomes(servicoNomes);
        System.out.println("Agencia criada com sucesso! id: " + idAgencia + " porta: " + portaAgencia);
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
