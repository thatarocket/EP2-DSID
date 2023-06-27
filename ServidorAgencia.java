import Global.Agencia;
import Global.IAgencia;
import Global.IServicoNomes;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class ServidorAgencia {
    static Scanner scanner = new Scanner(System.in);
    static IServicoNomes servicoNomes;


    public static void main(String[] args) {
        try {
            conectarServicoNomes();
            criarAgencia();
            System.out.println("Aguardando usuarios...");
        }
        catch (Exception e) {
            System.err.println("Erro no servidor da agencia: " + e);
            e.printStackTrace();
        }
    }

    private static void conectarServicoNomes() throws Exception{
        System.out.println("Digite o numero da porta do servidor de nomes: ");
        int portaServidor = Integer.parseInt(scanner.nextLine());
        String nomeServicoNomes = "servicoNomes";
        String objServico = "rmi://localhost:" + portaServidor + "/" + nomeServicoNomes;

        System.out.println("Conectando com o servico de nomes...");
        servicoNomes = (IServicoNomes) Naming.lookup(objServico);
        System.out.println("Conectado com o servico de nomes!");
    }

    private static void criarAgencia() throws Exception {
        System.out.println("Digite o numero da porta da agencia a ser criada: ");
        int portaAgencia = Integer.parseInt(scanner.nextLine());
        String idAgencia  = servicoNomes.registrarAgencia(portaAgencia);

        if(idAgencia == null) {
            System.out.println("Erro ao registrar agencia! Porta ja esta sendo utilizada!");
            criarAgencia();
        }

        IAgencia agencia = new Agencia(idAgencia,portaAgencia);
        LocateRegistry.createRegistry(portaAgencia);
        String objAgencia = "rmi://localhost:" + portaAgencia + "/" + idAgencia;
        Naming.rebind(objAgencia, agencia);
        System.out.println("Agencia criada com sucesso! id: " + idAgencia + " porta: " + portaAgencia);
    }
}
