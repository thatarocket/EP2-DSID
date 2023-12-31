package Global;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

public class Agente implements IAgente, Serializable, Runnable {
    private AtomicReference<String> mensagem = new AtomicReference<>();

    public Agente() {
    }

    public Double soma(String numbers) {
        int soma = 0;
        String[] numberArray = numbers.split(" ");
        for (String number : numberArray) {
            soma += Integer.parseInt(number);
        }
        return (double) soma;
    }

    @Override
    public Double receberMensagem(String msg) {
        mensagem.set(msg);
        return soma(msg);
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (mensagem.get() == null) {
                    System.out.println("Agente ira dormir por um minuto...");
                    Thread.sleep(60000); // 1 minutos
                } else {
                    System.out.println("Mensagem recebida: " + mensagem);
                    System.out.println("Resultado da soma: " + soma(String.valueOf(mensagem)));
                    mensagem.set(null);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Agente interrompido!");
        }
    }
}
