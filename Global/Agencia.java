package Global;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import java.io.File;

public class Agencia extends UnicastRemoteObject implements IAgencia {

    private String id;

    private int porta;

    private IServicoNomes servicoNomes;

    private HashMap<String,File> agentes = new HashMap<>(); // idAgente, Agente

    private HashMap<String, Thread> threads = new HashMap<>(); // idAgente, Thread

    public Agencia() throws RemoteException {
        super();
    }

    public Agencia(String id, int porta) throws RemoteException {
        this.id = id;
        this.porta = porta;
    }

    @Override
    public String enviarMensagem(String mensagem, String idAgente) throws Exception {
        String resposta = "";

        System.out.println("EnviarMensagem: mensagem recebida " + mensagem);
        String idAgencia = servicoNomes.getAgente(idAgente);

        if(idAgencia == null) {
            return "Agente não encontrado!";
        }

        System.out.println("idAgencia encontrado: " + idAgencia + " idAgencia atual: " + this.id);

        if(!idAgencia.equals(this.id)) {
            System.out.println("entrou no if");
            String nomeAgencia = servicoNomes.getAgente(idAgente);
            int localAgencia = servicoNomes.getAgencia(nomeAgencia);

            String objName = "rmi://localhost:" + localAgencia + "/" + nomeAgencia;
            IAgencia agenciaConectada = (IAgencia) Naming.lookup(objName);

            resposta = agenciaConectada.receberMensagem(mensagem, idAgente);
            Naming.unbind(objName);
        }
        else resposta = receberMensagem(mensagem, idAgente);

        if(resposta.equals("Agente não encontrado!")){
            return resposta;
        }
        return "O resultado da soma é: " + resposta;
    }

    private String getClassNameFromFile(File arquivoClass) {
        String fileName = arquivoClass.getName();
        return fileName.substring(0, fileName.lastIndexOf('.')).replace('/', '.');
    }
    @Override
    public String receberMensagem(String mensagem, String idAgente) throws RemoteException, ClassNotFoundException, NoSuchMethodException, MalformedURLException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // Pega o agente e executa o metodo soma, passando os parametros
        // Retorna o resultado da soma
        System.out.println("Entrou no receberMensagem");
        File file = agentes.get(idAgente);
        if(file == null){
            return "Agente não encontrado!";
        }
        Thread thread = threads.get(idAgente);
        if(thread == null){
            return "Agente não encontrado!";
        }

        System.out.println("Conseguiu pegar thread e file");

        try {

            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.getParentFile().toURI().toURL()});

            System.out.println("getClassNameFromFile: " + getClassNameFromFile(file));

            Class<?> agenteClass = classLoader.loadClass(getClassNameFromFile(file));

            System.out.println("Depois do classLoader");

            Object agenteInstance = agenteClass.getDeclaredConstructor().newInstance();
            Method metodoSoma = agenteClass.getMethod("soma", String.class);

            System.out.println("Depois do getMethod");
            Double resultadoSoma = (Double) metodoSoma.invoke(agenteInstance, mensagem);
            System.out.println("Depois da soma");

            return "O resultado da soma, segundo o agente " + idAgente + ", foi de " + resultadoSoma;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Erro ao executar agente!";
        }
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

            System.out.println("Thread do agente iniciada com sucesso!");

            String idAgente = servicoNomes.registrarAgente(this.id);
            agentes.put(idAgente,arquivoClass);
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
