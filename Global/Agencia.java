package Global;

import java.rmi.RemoteException;
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

    @Override
    public String criarAgente() throws RemoteException {
        return null;
    }

    @Override
    public String enviarMensagem(String mensagem, String idAgente) throws RemoteException {
        return null;
    }

    @Override
    public String transportarAgente(String idAgente, String idAgencia) throws RemoteException {
        return null;
    }

    @Override
    public String adicionarAgente(String idAgente, String idAgencia) throws RemoteException {
        return null;
    }

    @Override
    public String removerAgente(String idAgente, String idAgencia) throws RemoteException {
        return null;
    }

    public String getId() {
        return id;
    }

    public int getPorta() {
        return porta;
    }


}
