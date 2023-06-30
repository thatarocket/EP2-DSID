package Global;

import java.io.Serializable;

public class Agente implements IAgente, Serializable, Runnable {

    private String id;

    public Agente() {
    }

    public String getId() {
        return id;
    }

    @Override
    public String pararExecucao() throws Exception {
        return null;
    }

    @Override
    public String transportarAgencia() throws Exception {
        return null;
    }

    @Override
    public String comunicarComAgente() throws Exception {
        return null;
    }

    @Override
    public void run() {
        // hora da festa! (ainda nao implementado)
        System.out.println("AGENTE ESTA RODANDO"); //teste
    }
}
