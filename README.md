# EP2-DSID
Implementação, em Java, de um sistema de agentes móveis que se apoia na utilização da tecnologia RMI

## Como compilar e rodar
### 1.1. Compilar classes:
```
javac *.java
cd Global
javac *.java
cd ..
```


### 1.2. Subir o Servidor de Nomes:
java ServidorServicoNomes
Escolha uma porta para rodar esse servidor


### 1.3. Subir o Servidor de Agências:
Abra outro terminal e rode o seguinte comando:
```
java ServidorAgencia
```

Informe a mesma porta fornecida no passo 1.2 e uma porta para a agência a ser criada
Note que deve aparecer no terminal do ServidorServicoNomes deve aparecer a seguinte mensagem “Agencia registrada com sucesso!”. 
