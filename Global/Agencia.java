package Global;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import java.io.File;
import java.util.Map;

public class Agencia extends UnicastRemoteObject implements IAgencia {

    private String id;

    private int porta;

    private IServicoNomes servicoNomes;

    private HashMap<String,File> agentes = new HashMap<>(); // Agente, idAgente

    private Map<String, Thread> threads;

    public Agencia() throws RemoteException {
        super();
    }

    public Agencia(String id, int porta) throws RemoteException {
        this.id = id;
        this.porta = porta;
    }

    @Override
    public String enviarMensagem(String mensagem, String idAgente) throws Exception {
        String nomeAgencia = servicoNomes.getAgente(idAgente);
        int localAgencia = servicoNomes.getAgencia(nomeAgencia);

        String objName = "rmi://localhost:" + localAgencia + "/" + nomeAgencia;
        IAgencia agenciaConectada = (IAgencia) Naming.lookup(objName);

        String resposta = agenciaConectada.receberMensagem(mensagem, idAgente);
        Naming.unbind(objName);

        if(resposta.equals("Agente não encontrado!")){
            return resposta;
        }
        return "O resultado da soma é: " + resposta;
    }

    @Override
    public String receberMensagem(String mensagem, String idAgente) throws RemoteException, MalformedURLException, NotBoundException {
        // Recebe mensagem de um agente (enviado pela agencia) e envia ela para o outro agente
        // Pega o agente e executa o metodo soma, passando os parametros
        // Retorna o resultado da soma

        File file = agentes.get(idAgente);
        if(file == null){
            return "Agente não encontrado!";
        }

//        RunnableAgente runnableAgente = new RunnableAgente(file);
//        Thread thread = new Thread(runnableAgente);
//        thread.start();
//        threads.put(idAgente, thread);

        return resultadoSoma.toString();
    }

    @Override
    public String transportarAgente(String idAgente, String idAgenciaDestino) throws RemoteException, MalformedURLException, NotBoundException {
        // Transporta um agente para outra agencia
        // Contata o servico de nomes para saber em qual servidor esta a agencia de destino
        // O objeto do agente deve estar serializado 
        // Envia o agente serializado para a agencia de destino [?????]
        // Remove o agente da agencia atual
        // Indica que foi transmitido pela rede
    
        int localAgencia = servicoNomes.getAgencia(idAgenciaDestino);
        String objName = "rmi://localhost:" + localAgencia + "/" + idAgenciaDestino;
        IAgencia agenciaConectada = (IAgencia) Naming.lookup(objName);

        servicoNomes.removerAgente(idAgente);
        agenciaConectada.adicionarAgente(agentes.get(idAgente));
        agentes.remove(idAgente);

        return "Agente transportado com sucesso!";
    }

    @Override
    public String adicionarAgente(File arquivoClass) throws RemoteException {
        try {
            // Carrega o codigo do agente, gerando um id e criando uma thread para ele
            // Registra o agente no servico de nomes
            // Executa o agente por meio de uma thread
            // retorna o id ao usuario

            RunnableAgente runnableAgente = new RunnableAgente(arquivoClass);
            Thread thread = new Thread(runnableAgente);
            thread.start();

            String idAgente = servicoNomes.registrarAgente(this.id);
            agentes.put(idAgente,arquivoClass);
            threads.put(idAgente,thread);
            return idAgente;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Erro ao adicionar agente!";
        }
    }

    @Override
    public String removerAgente(String idAgente) throws RemoteException {
        // Retira do servico de nomes
        // Finaliza a thread

        Thread thread = threads.get(idAgente);
        if(thread == null) return "Agente nao encontrado!";
        thread.interrupt();
        threads.remove(idAgente);
        servicoNomes.removerAgente(idAgente);
        agentes.remove(idAgente);
        return "Agente interrompido com sucesso!";
    }

    public String getId() {
        return id;
    }

    public int getPorta() {
        return porta;
    }

    public void setServicoNomes(IServicoNomes servicoNomes) throws RemoteException {
        this.servicoNomes = servicoNomes;
    }

    public ServicoNomes getServicoNomes() throws RemoteException {
        return (ServicoNomes) servicoNomes;
    }


}
