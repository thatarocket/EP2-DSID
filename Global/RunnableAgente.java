package Global;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RunnableAgente implements Runnable {

    private File arquivoClass;

    public RunnableAgente(File arquivoClass){
        this.arquivoClass = arquivoClass;
    }

    @Override
    public void run() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("java", arquivoClass.getName().replace(".class",""));
            Process process = processBuilder.start();
            process.waitFor();

            InputStream inputStream = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
