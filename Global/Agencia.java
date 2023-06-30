package Global;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Agencia extends UnicastRemoteObject  implements IAgencia {

    private String id;

    private int porta;

    private IServicoNomes servicoNomes;

    public Agencia() throws RemoteException {
        super();
    }

    public Agencia(String id, int porta) throws RemoteException {
        this.id = id;
        this.porta = porta;
    }

    @Override
    public String teste() throws RemoteException {
        return "Teste";
    }


    @Override
    public String enviarMensagem(String mensagem, String idAgente) throws Exception {
        // Agente envia a mensagem para outro agente
        // Contata o servico de nomes para saber em qual agencia (E maquina), esta o agente de destino
        // Repassa a mensagem para a agência de destino (metodo de recepcao de mensagens)
        String nomeAgencia = servicoNomes.getAgente(idAgente);
        int localAgencia = servicoNomes.getAgencia(nomeAgencia);
        String objName = "rmi://localhost:" + localAgencia + "/" + nomeAgencia;
        IAgencia agenciaConectada = (IAgencia) Naming.lookup(objName);
        agenciaConectada.receberMensagem(mensagem, idAgente);
        Naming.unbind(objName);
        return "Mensagem enviada com sucesso!";
    }

    @Override
    public String receberMensagem(String mensagem, String idAgente) throws RemoteException {
        // Recebe mensagem um agente (enviado pela agencia) e envia para outro agente
        return null;
    }

    @Override
    public String transportarAgente(String idAgente, String idAgenciaDestino) throws RemoteException {
        // Transporta um agente para outra agencia
        // Contata o servico de nomes para saber em qual servidor esta a agencia de destino
        // O objeto do agente deve estar serializado 
        // Envia o agente serializado para a agencia de destino [?????]
        // Remove o agente da agencia atual
        // Indica que foi transmitido pela rede
        servicoNomes.removerAgente(idAgente);
        // [falta implementacao]
        return "Agente transportado com sucesso!";
    }

    @Override
    public String adicionarAgente(byte[] classBytes) throws RemoteException {
        // Carrega o codigo do agente, gerando um id e criando uma thread para ele
        // Registra o agente no servico de nomes
        // Executa o agente por meio de uma thread
        // retorna o id ao usuario
//        String idAgente = servicoNomes.registrarAgente(this.id);
//        Class<?> agentClass = ClassLoader.defineClassFromBytes(classBytes);
//        Thread thread = new Thread(Arrays.toString(classBytes));
//        thread.start();
        return null;
    }

    @Override
    public String removerAgente(String idAgente) throws RemoteException {
        // Retira do servico de nomes
        // Finaliza a thread
        return null;
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
