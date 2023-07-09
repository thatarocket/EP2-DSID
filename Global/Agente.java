package Global;

import java.io.Serializable;

public class Agente implements IAgente, Serializable, Runnable {

    private String mensagem;

    private double resultadoSoma;
    private final Object lock = new Object();
    public Agente() {
    }

    public Double soma(String numbers) {

        Double numero1 = Double.valueOf(numbers.split(" ")[0]);
        Double numero2 = Double.valueOf(numbers.split(" ")[1]);

        this.resultadoSoma = numero1 + numero2;
        return resultadoSoma;
    }

    public void receberMensagem(String msg) {
        synchronized (lock) {
            this.mensagem = msg;
            lock.notifyAll();
        }
    }

    public double getResultadoSoma() {
        return resultadoSoma;
    }

    @Override
    public void run() {
        System.out.println("Agente executando");
        while (true) {
            synchronized (lock) {
                while (mensagem == null) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        // Lidar com a interrupção da thread, se necessário
                    }
                }
                System.out.println("Mensagem recebida: " + mensagem);
                System.out.println("Resultado da soma: " + soma(mensagem));
                mensagem = null;
            }
        }
    }


}
