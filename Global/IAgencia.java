package Global;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAgencia extends Remote {

    String enviarMensagem(String mensagem, String idAgente) throws Exception;

    String receberMensagem(String mensagem, String idAgente) throws Exception;

    String transportarAgente(String idAgente, String idAgenciaDestino) throws RemoteException, MalformedURLException, NotBoundException;

    String adicionarAgente(File arquivoCompilado) throws RemoteException;

    String removerAgente(String idAgente) throws RemoteException;

    void setServicoNomes(IServicoNomes servicoNomes) throws RemoteException;

    ServicoNomes getServicoNomes() throws RemoteException;
    String mudarAgente(Anonymous agente, String idAgente) throws RemoteException, MalformedURLException, NotBoundException;


}
