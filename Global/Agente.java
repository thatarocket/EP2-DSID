package Global;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;


public class Agente implements IAgente, Serializable, Runnable {
    private AtomicReference<String> mensagem = new AtomicReference<>();
    public Agente() {
    }

    public Double soma(String numbers) {
        Double numero1 = Double.valueOf(numbers.split(" ")[0]);
        Double numero2 = Double.valueOf(numbers.split(" ")[1]);
        return numero1 + numero2;
    }

    public Double receberMensagem(String msg) {
        mensagem.set(msg);
        return soma(msg);
    }

    @Override
    public void run() {
        try {
            while (true) {
                if(mensagem.get() == null) {
                    System.out.println("Agente irá dormir por um minuto...");
                    Thread.sleep(60000); // 1 minutos
                }
                else {
                    System.out.println("Mensagem recebida: " + mensagem);
                    System.out.println("Resultado da soma: " + soma(String.valueOf(mensagem)));
                    mensagem.set(null);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
