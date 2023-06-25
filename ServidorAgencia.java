import Global.Agencia;
import Global.IAgencia;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class ServidorAgencia {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        try {
            IAgencia agencia = new Agencia();
            System.out.println("Digite o nome da agência: ");
            String nomeAgencia = scanner.nextLine();

            System.out.println("Digite o numero da porta: ");
            int numPorta = Integer.parseInt(scanner.nextLine());

            LocateRegistry.createRegistry(numPorta);
            String objName = "rmi://localhost:" + numPorta + "/" + nomeAgencia;
            Naming.rebind(objName, agencia);
            System.out.println("Aguardando usuarios...");
        }
        catch (Exception e) {
            System.err.println("Erro no servidor da agencia: " + e.toString());
            e.printStackTrace();
        }
    }
}
