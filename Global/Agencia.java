package Global;

import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;

public class Agencia extends UnicastRemoteObject implements IAgencia {

        public Agencia() throws Exception {
            super();
        }

        @Override
        public String teste() {
            return "Teste";
        }
}
