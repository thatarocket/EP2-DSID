package Global;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAgencia extends Remote {

    String teste() throws RemoteException;

    String criarAgente() throws RemoteException;

    String enviarMensagem(String mensagem, String idAgente) throws RemoteException;

    String transportarAgente(String idAgente, String idAgencia) throws RemoteException;

    String adicionarAgente(String idAgente, String idAgencia) throws RemoteException;

    String removerAgente(String idAgente,String idAgencia) throws RemoteException;


}
