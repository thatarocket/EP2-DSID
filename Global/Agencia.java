package Global;

import java.rmi.server.UnicastRemoteObject;

public class Agencia extends UnicastRemoteObject implements IAgencia {

    private String id;

    private int porta;

    public Agencia() throws Exception {
        super();
    }

    public Agencia(String id, int porta) throws Exception {
        this.id = id;
        this.porta = porta;
    }

    @Override
    public String teste() {
        return "Teste";
    }

    public String getId() {
        return id;
    }

    public int getPorta() {
        return porta;
    }


}
