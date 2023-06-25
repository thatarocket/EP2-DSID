import Global.Agencia;
import Global.IAgencia;

import java.rmi.Naming;
import java.util.Scanner;

public class Cliente {

    static Scanner scanner = new Scanner(System.in);
    static ContextoAtual contextoAtual;
    public static void main(String[] args) throws Exception{
        try {
            contextoAtual = new ContextoAtual();
            conectar();
            if(contextoAtual.agenciaAtual != null) opcoes();
        }
        catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static void opcoes() throws Exception{
        System.out.println("--------------------------------------------------");
        System.out.println("Escolha a opcao desejada! (Pelo numero)");
        System.out.println("1 - bind");        // Faz o cliente se conectar com uma agênncia
        System.out.println("2 - unbind");     // Desconectar da agência
        System.out.println("3 - quit");       // Encerra a execucao do cliente
        System.out.println("4 - teste");      // Teste
        selecionar();
    }

    public static void selecionar() throws Exception {
        System.out.println("INSIRA A OPCAO DESEJADA: ");
        int input = Integer.parseInt(scanner.nextLine());

        switch(input) {
            case 1:
                conectar();
                break;
            case 2:
                desconectar();
                break;
            case 3:
                quit();
                break;
            case 4:
                teste();
                break;
            default:
                System.out.println("Opcao invalida! Coloque uma opcao valida:");
                selecionar();
                break;
        }
    }

    public static void conectar() throws Exception{
        if(estaConectado()) throw new Exception("Se desconecte para conseguir conectar em outra agência.");
        System.out.println("--------------------------------------------------");
        System.out.println("Digite o nome da agencia: ");
        String nomeAgencia = scanner.nextLine();
        System.out.println("Digite o numero da porta: ");
        int numPorta = Integer.parseInt(scanner.nextLine());

        String objName = "rmi://localhost:" + numPorta + "/" + nomeAgencia;
        IAgencia agencia = (IAgencia) Naming.lookup(objName);
        contextoAtual.agenciaAtual = agencia;
        contextoAtual.nomeAgencia = nomeAgencia;
        contextoAtual.portaAgencia = numPorta;
        System.out.println("Conectado com sucesso!");
        opcoes();
    }

    public static void desconectar() throws Exception {
        if(!estaConectado()) throw new Exception("Voce nao esta conectado a nenhuma agencia.");
        contextoAtual.agenciaAtual = null;
        contextoAtual.nomeAgencia = null;
        contextoAtual.portaAgencia = -1;
        System.out.println("Desconectado com sucesso!");
        opcoes();
    }

    public static void quit() throws Exception {
        if(estaConectado()) desconectar();
        System.out.println("Encerrando...");
        System.exit(0);
    }

    public static boolean estaConectado() {
        return contextoAtual.agenciaAtual != null;
    }

    public static void teste() throws Exception{
        if(!estaConectado()) throw new Exception("Voce nao esta conectado a nenhuma agencia.");
        System.out.println("--------------------------------------------------");
        String teste = contextoAtual.agenciaAtual.teste();
        System.out.println(teste);
    }


}
