package Global;

import java.rmi.Remote;

public interface IAgente extends Remote {

    Double soma(String numbers) throws Exception;
}

