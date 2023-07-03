package Global;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ServicoNomes extends UnicastRemoteObject implements IServicoNomes {

        int qtdAgentes;
        int qtdAgencias;

        HashMap<String, Integer> agencias = new HashMap<>(); // <idAgencia, numPorta>
        HashMap<String, String> agentes = new HashMap<>(); // <idAgente, idAgencia>

        public ServicoNomes() throws RemoteException {
            super();
        }

        public String registrarAgencia(int numPorta) throws RemoteException {
                if(agencias.containsValue(numPorta)) {
                        return null;
                }
                String idAgencia = gerarIdAgencia();
                agencias.put(idAgencia, numPorta);
                return idAgencia;
        }

        public String registrarAgente(String idAgencia) throws RemoteException {
                if(!agencias.containsKey(idAgencia)) {
                        return null;
                }
                String idAgente = gerarIdAgente();
                agentes.put(idAgente, idAgencia);
                return idAgente;
        }

        private String gerarIdAgencia() throws RemoteException{
                String id = "Agencia_" + qtdAgencias;
                qtdAgencias++;
                return id;
        }

        private String gerarIdAgente() throws RemoteException{
                String id = "Agente_" + qtdAgentes;
                qtdAgentes++;
                return id;
        }

        //localizacao agencia
        public int getAgencia(String idAgencia) throws RemoteException {
                return agencias.get(idAgencia);
        }

        //localizacao agente
        public String getAgente(String idAgente) throws RemoteException {
                return agentes.get(idAgente);
        }

        @Override
        public HashMap<String, Integer> getAgencias() throws RemoteException {
                return agencias;
        }

        @Override
        public HashMap<String, String> getAgentes() throws RemoteException {
                return agentes;
        }

        public void removerAgencia(String idAgencia) throws RemoteException {
                agencias.remove(idAgencia);
        }

        public void removerAgente(String idAgente) throws RemoteException {
                agentes.remove(idAgente);
        }


}
