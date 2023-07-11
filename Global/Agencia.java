package Global;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import java.io.File;

public class Agencia extends UnicastRemoteObject implements IAgencia {

    private String id;

    private IServicoNomes servicoNomes;

    private HashMap<String,Anonymous> agentes = new HashMap<>(); // idAgente, Agente

    private HashMap<String, Thread> threads = new HashMap<>(); // idAgente, Thread

    public Agencia(String id) throws RemoteException {
        this.id = id;
    }

    @Override
    public String enviarMensagem(String mensagem, String idAgente) throws Exception {
        String resposta;

        String idAgencia = servicoNomes.getAgente(idAgente);
        if(idAgencia == null) {
            return "Agente não encontrado!";
        }

        if(!idAgencia.equals(this.id)) {
            String nomeAgencia = servicoNomes.getAgente(idAgente);
            int localAgencia = servicoNomes.getAgencia(nomeAgencia);

            String objName = "rmi://localhost:" + localAgencia + "/" + nomeAgencia;
            IAgencia agenciaConectada = (IAgencia) Naming.lookup(objName);
            resposta = agenciaConectada.receberMensagem(mensagem, idAgente);
            Naming.unbind(objName);
        }
        else resposta = receberMensagem(mensagem, idAgente);
        return resposta;
    }
    @Override
    public String receberMensagem(String mensagem, String idAgente) throws Exception {
        // Pega o agente e executa o metodo soma, passando os parametros
        // Retorna o resultado da soma

        Anonymous agente = agentes.get(idAgente);
        if(agente == null){
            return "Agente não encontrado!";
        }

        synchronized (agente) {
            Object resultadoSomaObj = agente.call("receberMensagem", mensagem);
            double resultadoSoma = Double.parseDouble(resultadoSomaObj.toString());
            System.out.println("Resultado da soma: " + resultadoSoma);
            return "O resultado da soma, segundo o agente " + idAgente + ", foi de " + resultadoSoma;
        }
    }

    @Override
    public String transportarAgente(String idAgente, String idAgenciaDestino) throws RemoteException, MalformedURLException, NotBoundException {
        // Transporta um agente para outra agencia
        // Contata o servico de nomes para saber em qual servidor esta a agencia de destino
        // O objeto do agente deve estar serializado 
        // Envia o agente serializado para a agencia de destino
        // Remove o agente da agencia atual
        // Indica que foi transmitido pela rede

        if(this.id.equals(idAgenciaDestino)){
            return "Agente já está na agência destino!";
        }

        if(!agentes.containsKey(idAgente)){
            return "Agente não encontrado!";
        }

        int localAgencia = servicoNomes.getAgencia(idAgenciaDestino);
        String objName = "rmi://localhost:" + localAgencia + "/" + idAgenciaDestino;
        IAgencia agenciaConectada = (IAgencia) Naming.lookup(objName);

        servicoNomes.removerAgente(idAgente);
        Anonymous agente = agentes.get(idAgente);
        agenciaConectada.mudarAgente(agente, idAgente);
        agentes.remove(idAgente);
        threads.remove(idAgente);

        Naming.unbind(objName);
        return "Agente transportado com sucesso!";
    }

    public String mudarAgente(Anonymous agente, String idAgente) throws RemoteException {
        try {
            servicoNomes.transportarAgente(idAgente, this.id);
            agentes.put(idAgente,agente);
            threads.put(idAgente,threads.get(idAgente));
            System.out.println("Agente " + idAgente + " adicionado com sucesso!");
            return idAgente;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Erro ao adicionar agente!";
        }
    }

    @Override
    public String adicionarAgente(File arquivoClass) throws RemoteException {
        try {
            // Carrega o codigo do agente, gerando um id e criando uma thread para ele
            // Registra o agente no servico de nomes
            // Executa o agente por meio de uma thread
            // retorna o id ao usuario

            Anonymous agente = new Anonymous(arquivoClass);
            Thread thread = new Thread((Runnable) agente.getObject());
            thread.start();
            System.out.println("Thread do agente iniciada com sucesso!");

            String idAgente = servicoNomes.registrarAgente(this.id);
            agentes.put(idAgente,agente);
            threads.put(idAgente,thread);
            System.out.println("Agente " + idAgente + " adicionado com sucesso!");
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

    public void setServicoNomes(IServicoNomes servicoNomes) throws RemoteException {
        this.servicoNomes = servicoNomes;
    }

    public ServicoNomes getServicoNomes() throws RemoteException {
        return (ServicoNomes) servicoNomes;
    }


}
