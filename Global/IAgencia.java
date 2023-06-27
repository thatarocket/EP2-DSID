package Global;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAgencia extends Remote {

    String teste() throws RemoteException;


}
