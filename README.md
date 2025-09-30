# Guia de Compilação e Execução do Sistema

O projeto foi desenvolvido utilizando o banco rodando no wsl assim como o JDK17, mas o projeto também pode ser compilado no windows, basta seguir o passo a passo a seguir.

## Visão Geral dos Requisitos

- **Java:** JDK 17
- **Banco de Dados:** PostgreSQL
- **Driver JDBC:** `postgresql-42.7.7.jar`
- **Nome do Banco:** `restaurantev2`
- **Usuário do Banco:** `postgres`
- **Senha do Banco:** `123456`

---

## Ambiente Windows

Guia para configurar e rodar o projeto usando o Prompt de Comando (CMD) ou PowerShell.

### Passo 1: Instalar o JDK 17

1.  **Download:** Baixe o instalador do JDK 17 para Windows. A fonte recomendada é o **Eclipse Temurin (Adoptium)**.
    - **Link:** [https://adoptium.net/temurin/releases/?version=17](https://adoptium.net/temurin/releases/?version=17)
    - Escolha a versão para `Windows`, arquitetura `x64`, e baixe o arquivo `.msi`.

2.  **Instalação:** Execute o arquivo `.msi` e siga o assistente de instalação. Ele configurará automaticamente as variáveis de ambiente necessárias.

3.  **Verificação:** Abra um novo Prompt de Comando e digite:
    ```sh
    java --version
    ```
    A saída deve confirmar a instalação da versão 17.

### Passo 2: Instalar o PostgreSQL

1.  **Download:** Baixe o instalador do PostgreSQL no site oficial.
    - **Link:** [https://www.postgresql.org/download/windows/](https://www.postgresql.org/download/windows/)

2.  **Instalação:** Execute o instalador. Durante o processo, será solicitado que você defina uma senha para o superusuário `postgres`.
    - **Importante:** Defina a senha que quiser, ela será utilizada futuramente no código de conexão do projeto.

### Passo 3: Criar o Banco de Dados e as Tabelas

1.  Abra o **pgAdmin 4**, instalado junto com o PostgreSQL.
2.  Conecte-se ao servidor local usando a senha criada anteriormente.
3.  Na árvore à esquerda, clique com o botão direito em **Databases** > **Create** > **Database...**.
4.  No campo "Database", escolha o nome para sua base e clique em "Save".
5.  Selecione a base criada no passo anterior, vá ao menu **Tools** e clique em **Query Tool**.
6.  Na janela de consulta, cole o seguinte **script SQL** para criar as tabelas.
    ```sql
    CREATE TABLE receitas (
    id_receitas serial PRIMARY KEY,
    quantidade_necessaria decimal,
    fk_item_id_items serial,
    fk_produtos_id_produto serial);

    CREATE TABLE item (
    id_items serial PRIMARY KEY,
    nome varchar,
    preco_venda decimal,
    descricao varchar
    );
    
    CREATE TABLE pedido_itens (
    id_produtos_pedidos serial PRIMARY KEY,
    quantidade integer,
    fk_pedidos_id_pediido serial,
    fk_item_id_items serial
    );
    
    CREATE TABLE produtos (
    id_produto serial PRIMARY KEY,
    nome varchar,
    unidade_medida varchar
    );
    
    CREATE TABLE funcionarios (
    id_funcionario serial PRIMARY KEY,
    nome varchar,
    cargo varchar,
    salario decimal,
    telefone varchar
    );
    
    CREATE TABLE pedidos (
    id_pediido serial PRIMARY KEY,
    data_pedido timestamp,
    status varchar,
    fk_mesas_id_mesa serial,
    fk_funcionarios_id_funcionario serial
    );
    
    CREATE TABLE mesas (
    id_mesa serial PRIMARY KEY,
    numero integer,
    capacidade integer
    );

    ALTER TABLE pedidos ADD CONSTRAINT FK_pedidos_2
    FOREIGN KEY (fk_mesas_id_mesa)
    REFERENCES mesas (id_mesa)
    ON DELETE CASCADE;
    
    ALTER TABLE pedidos ADD CONSTRAINT FK_pedidos_3
    FOREIGN KEY (fk_funcionarios_id_funcionario)
    REFERENCES funcionarios (id_funcionario)
    ON DELETE CASCADE;
    
    ALTER TABLE receitas ADD CONSTRAINT FK_receitas_2
    FOREIGN KEY (fk_item_id_items)
    REFERENCES item (id_items)
    ON DELETE CASCADE;
    
    ALTER TABLE receitas ADD CONSTRAINT FK_receitas_3
    FOREIGN KEY (fk_produtos_id_produto)
    REFERENCES produtos (id_produto)
    ON DELETE CASCADE;
    
    ALTER TABLE pedido_itens ADD CONSTRAINT FK_pedido_itens_2
    FOREIGN KEY (fk_pedidos_id_pediido)
    REFERENCES pedidos (id_pediido)
    ON DELETE CASCADE;
    
    ALTER TABLE pedido_itens ADD CONSTRAINT FK_pedido_itens_3
    FOREIGN KEY (fk_item_id_items)
    REFERENCES item (id_items)
    ON DELETE CASCADE;
    ```

7.  Clique no ícone de "Play" (Execute) para rodar o script.

### Passo 4: Preparar a Estrutura do Projeto

1.  Crie uma pasta principal para o projeto, por exemplo, `C:\RestauranteApp`.
2.  Dentro desta pasta, coloque a pasta `src` presente neste link.
    - **Link:** [https://github.com/vieroBruno/Banco-II/tree/main/src](https://github.com/vieroBruno/Banco-II/tree/main/src)
3.  **Baixe o driver JDBC do PostgreSQL v42.7.7**:
    - **Link:** [https://jdbc.postgresql.org/download/](https://jdbc.postgresql.org/download/)
4.  Coloque o arquivo `postgresql-42.7.7.jar` dentro da pasta `C:\RestauranteApp`.
5.  Crie uma pasta vazia chamada `bin` dentro de `C:\RestauranteApp`.

A estrutura final da pasta deve ser:
```
C:\RestauranteApp\
|-- src\
|-- bin\
|-- postgresql-42.7.7.jar
```

### Passo 5: Compilar o Sistema

1.  Abra o Prompt de Comando e navegue até a pasta do projeto:
    ```sh
    cd C:\RestauranteApp
    ```
2.  Execute o comando de compilação:
    ```sh
    javac -d bin -cp ".;postgresql-42.7.7.jar" src/model/*.java src/repository/*.java src/repository/jdbc/*.java src/exception/*.java src/service/*.java src/util/*.java src/view/*.java
    ```

### Passo 6: Executar o Sistema

1.  No mesmo local, execute o comando `java`, especificando o classpath e a classe principal:
    ```sh
    java -cp "bin;postgresql-42.7.7.jar" view.Menu
    ```
2.  O menu principal do sistema será exibido no console.

---

## Ambiente WSL (Ubuntu/Debian)

Guia para configurar e rodar o projeto usando o terminal do WSL.

### Passo 1: Instalar o JDK 17

1.  Abra o terminal do WSL.
2.  Atualize os pacotes e instale o JDK:
    ```sh
    sudo apt update
    sudo apt install openjdk-17-jdk
    ```
3.  Verifique a instalação:
    ```sh
    java --version
    ```

### Passo 2: Instalar e Configurar o PostgreSQL

1.  Instale o PostgreSQL:
    ```sh
    sudo apt install postgresql postgresql-contrib
    ```
2.  Inicie o serviço do PostgreSQL:
    ```sh
    sudo service postgresql start
    ```
3.  **Defina a senha do usuário `postgres`:**
    - Acesse o psql: `sudo -u postgres psql`
    - Dentro do psql, execute o comando: `\password postgres`
    - Digite uma senha e confirme.
    - Saia do psql: `\q`

### Passo 3: Criar o Banco de Dados e as Tabelas

1.  Crie o banco de dados `nomeDoBanco`:
    ```sh
    sudo -u postgres createdb nomeDoBanco
    ```
2.  Conecte-se ao banco:
    ```sh
    sudo -u postgres psql -d nomeDoBanco
    ```
3.  No psql, cole os scripts SQL para criar as tabelas e inserir os dados. Após a execução, saia com `\q`.

### Passo 4: Preparar a Estrutura do Projeto

1.  Crie uma pasta para o projeto: `mkdir ~/RestauranteApp`
2.  Dentro de `~/RestauranteApp`, coloque a pasta `src` com o código.
3.  **Baixe o driver JDBC via terminal:**
    ```sh
    cd ~/RestauranteApp
    wget [https://jdbc.postgresql.org/download/postgresql-42.7.7.jar](https://jdbc.postgresql.org/download/postgresql-42.7.7.jar)
    ```
4.  Crie a pasta `bin`: `mkdir bin`

A estrutura final da pasta deve ser:
```
~/RestauranteApp/
|-- src/
|-- bin/
|-- postgresql-42.7.7.jar
```

### Passo 5: Compilar o Sistema

1.  Navegue até a pasta do projeto: `cd ~/RestauranteApp`
2.  Execute o comando de compilação (note o separador `:` no classpath):
    ```sh
    javac -d bin -cp ".:postgresql-42.7.7.jar" src/model/*.java src/repository/*.java src/repository/jdbc/*.java src/exception/*.java src/service/*.java src/util/*.java src/view/*.java
    ```

### Passo 6: Executar o Sistema

1.  No mesmo local, execute o comando `java` (novamente, usando `:` no classpath):
    ```sh
    java -cp "bin:postgresql-42.7.7.jar" view.Menu
    ```
2.  O sistema será iniciado no seu terminal WSL.