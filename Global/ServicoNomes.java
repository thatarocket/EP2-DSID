package Global;

import java.rmi.server.UnicastRemoteObject;

public class ServicoNomes extends UnicastRemoteObject implements IServicoNomes {

        public ServicoNomes() throws Exception {
            super();
        }

        @Override
        public String teste() {
            return "Teste";
        }
}
