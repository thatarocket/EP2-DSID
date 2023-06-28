package Global;

import java.rmi.Remote;

public interface IAgente extends Remote {

    String pararExecucao() throws Exception;

    String transportarAgencia() throws Exception;

    String comunicarComAgente() throws Exception;;

}

