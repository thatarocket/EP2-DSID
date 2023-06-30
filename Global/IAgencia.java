package Global;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAgencia extends Remote {

    String teste() throws RemoteException;

    String enviarMensagem(String mensagem, String idAgente) throws Exception;

    String receberMensagem(String mensagem, String idAgente) throws RemoteException;

    String transportarAgente(String idAgente, String idAgenciaDestino) throws RemoteException;

    String adicionarAgente(byte[] arquivoCompilado) throws RemoteException;

    String removerAgente(String idAgente) throws RemoteException;

    void setServicoNomes(IServicoNomes servicoNomes) throws RemoteException;

    ServicoNomes getServicoNomes() throws RemoteException;

}
