package Global;

import java.rmi.Remote;

public interface IAgente extends Remote {

    Double receberMensagem(String msg);
}

