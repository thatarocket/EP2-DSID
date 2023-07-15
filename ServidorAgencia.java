import Global.Agencia;
import Global.IAgencia;
import Global.IServicoNomes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class ServidorAgencia {
    static Scanner scanner = new Scanner(System.in);
    static IServicoNomes servicoNomes;
    static final String NOME_SERVICO_NOMES = "servicoNomes";

    public static void main(String[] args) throws RemoteException, MalformedURLException {
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

    private static void criarAgencia() throws RemoteException, MalformedURLException {
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
        }

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
