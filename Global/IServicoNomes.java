package Global;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface IServicoNomes extends Remote {

    // id do agente, id da agencia
    HashMap<String,Integer> agencias = new HashMap<>();
    HashMap<String, String> agentes = new HashMap<>();
    String teste() throws RemoteException;
    String registrarAgencia(int numPorta) throws RemoteException;
    String registrarAgente(String idAgencia) throws RemoteException;
    String gerarIdAgencia() throws RemoteException;
    String gerarIdAgente() throws RemoteException;
    int getAgencia(String idAgencia) throws RemoteException;
    String getAgente(String idAgente) throws RemoteException;

    HashMap<String, Integer> getAgencias() throws RemoteException;

    HashMap<String, String> getAgentes() throws RemoteException;

    void removerAgente(String idAgente);

    void moverAgente(String idAgente, String idAgenciaDestino);

    void removerAgencia(String idAgencia);

}
