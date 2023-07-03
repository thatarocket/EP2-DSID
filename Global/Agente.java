package Global;

import java.io.Serializable;

public class Agente implements IAgente, Serializable {

    private String numbers;
    public Agente() {
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    @Override
    public Double soma(String numbers) throws Exception {
        Double numero1 = Double.valueOf(numbers.split(" ")[0]);
        Double numero2 = Double.valueOf(numbers.split(" ")[1]);

        Double resultadoSoma = numero1 + numero2;
        return resultadoSoma;
    }

    public static void main(String[] args) throws Exception {
        Agente agente = new Agente();
        agente.setNumbers(args[0]);
        System.out.println(agente.soma(agente.numbers));
    }


}
