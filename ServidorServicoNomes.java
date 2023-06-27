import Global.IServicoNomes;
import Global.ServicoNomes;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class ServidorServicoNomes {
    static Scanner scanner = new Scanner (System.in);
    public static void main(String[] args) {
        try {
            criarServico();
            System.out.println("Aguardando agências...");
        }
        catch (Exception e) {
            System.err.println("Erro no servidor: " + e);
            e.printStackTrace();
        }
    }

    private static void criarServico() throws Exception{
        IServicoNomes serv = new ServicoNomes();
        System.out.println("Digite o numero da porta: ");
        int numPorta = Integer.parseInt(scanner.nextLine());
        String nomeServ  = "servicoNomes";
        LocateRegistry.createRegistry(numPorta);

        String objName = "rmi://localhost:" + numPorta + "/" + nomeServ;
        Naming.rebind(objName, serv);
    }
}
