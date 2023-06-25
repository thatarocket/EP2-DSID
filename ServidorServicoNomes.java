import Global.IServicoNomes;
import Global.ServicoNomes;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class ServidorServicoNomes {
    static Scanner scanner = new Scanner (System.in);
    public static void main(String[] args) {
        try {
            IServicoNomes serv = new ServicoNomes();
            System.out.println("Digite o nome do Servico de nomes: ");
            String nomeServ = scanner.nextLine();

            System.out.println("Digite o numero da porta: ");
            int numPorta = Integer.parseInt(scanner.nextLine());

            LocateRegistry.createRegistry(numPorta);

            String objName = "rmi://localhost:" + numPorta + "/" + nomeServ;

            Naming.rebind(objName, serv);

            System.out.println("Aguardando agências...");
        }
        catch (Exception e) {
            System.err.println("Erro no servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}
