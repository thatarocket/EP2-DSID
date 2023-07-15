package Global;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Iterator;
import java.util.Map;

public class ServicoNomes extends UnicastRemoteObject implements IServicoNomes {
        static Scanner scanner = new Scanner(System.in);

        int qtdAgentes;
        int qtdAgencias;

        HashMap<String, Integer> agencias = new HashMap<>(); // <idAgencia, numPorta>
        HashMap<String, String> agentes = new HashMap<>(); // <idAgente, idAgencia>

        public ServicoNomes() throws RemoteException {
                super();
        }

        public String registrarAgencia(int numPorta) throws RemoteException {
                String idAgencia = null;
                if (agencias.containsValue(numPorta)) {
                        return "ERRO_PORTA";
                }
                idAgencia = gerarIdAgencia();
                agencias.put(idAgencia, numPorta);
                return idAgencia;
        }

        public String registrarAgente(String idAgencia) throws RemoteException {
                if (!agencias.containsKey(idAgencia)) {
                        System.out.println("Agente já cadastrado");
                        return null;
                }
                String idAgente = gerarIdAgente();
                agentes.put(idAgente, idAgencia);
                System.out.println("Agente registrado com sucesso!");
                return idAgente;
        }

        public String gerarIdAgencia() throws RemoteException {
                String id = "Agencia_" + qtdAgencias;
                qtdAgencias++;
                return id;
        }

        private String gerarIdAgente() throws RemoteException {
                String id = "Agente_" + qtdAgentes;
                qtdAgentes++;
                return id;
        }

        // localizacao agencia
        public int getAgencia(String idAgencia) throws RemoteException {
                return agencias.get(idAgencia);
        }

        // localizacao agente
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
                System.out.println("Agencia removida com sucesso!");
        }

        public void removerAgenciaPorPorta(int porta) throws RemoteException {
                Iterator<Map.Entry<String, Integer>> iterator = agencias.entrySet().iterator();
                while (iterator.hasNext()) {
                        Map.Entry<String, Integer> entry = iterator.next();
                        if (entry.getValue() == porta) {
                                removerAgencia(entry.getKey());
                        }
                }
                System.out.println("Agencia removida com sucesso!");
        }

        public void removerAgente(String idAgente) throws RemoteException {
                agentes.remove(idAgente);
                System.out.println("Agente removido com sucesso!");
        }

        public void transportarAgente(String idAgente, String idAgencia) throws RemoteException {
                // pegar onde o agente está e mudar para ele estar associado com a nova agencia
                agentes.put(idAgente, idAgencia);
                System.out.println("Agente transportado com sucesso!");
        }
}
