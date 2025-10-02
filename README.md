# Guia de Configuração e Execução do Sistema

O projeto foi desenvolvido utilizando o JDK 17 e banco Postgres rodando no WSL e IDEA Intellij no Windows, por isso foi decidido trazer um passo a passo tanto no Windows como no WSL.
Este guia detalha os passos para configurar o ambiente e executar o projeto utilizando a IDE **IntelliJ IDEA**

## Visão Geral dos Requisitos

- **IDE:** IntelliJ IDEA (Community ou Ultimate)
- **Java:** JDK 17
- **Banco de Dados:** PostgreSQL
- **Driver JDBC:** `postgresql-42.7.7.jar`
- **Nome do Banco (sugerido):** `restaurantev2`
- **Usuário do Banco (padrão):** `postgres`
- **Senha do Banco (sugerida):** `123456`

---

## Ambiente Windows

Guia para configurar e rodar o projeto no Windows com IntelliJ IDEA.

### Passo 1: Instalar o JDK 17

1.  **Download:** Baixe o instalador do JDK 17 para Windows do **Eclipse Temurin (Adoptium)**.
    - **Link:** [https://adoptium.net/temurin/releases/?version=17](https://adoptium.net/temurin/releases/?version=17)
    - Baixe o arquivo `.msi` para `Windows` `x64`.
2.  **Instalação:** Execute o instalador e siga as instruções. Ele configurará o `PATH` do sistema automaticamente.
3.  **Verificação:** Abra um Prompt de Comando e digite `java --version` para confirmar a instalação.

### Passo 2: Instalar o PostgreSQL

1.  **Download:** Baixe o instalador do PostgreSQL no site oficial.
    - **Link:** [https://www.postgresql.org/download/windows/](https://www.postgresql.org/download/windows/)
2.  **Instalação:** Durante a instalação, defina uma senha para o usuário `postgres`.
    - **Importante:** Para evitar alterações no código, use a senha `123456`. Se usar outra, lembre-se de atualizá-la no arquivo `src/repository/jdbc/Conexao.java`.

### Passo 3: Criar o Banco de Dados e as Tabelas

1.  Abra o **pgAdmin 4**.
2.  Conecte-se ao seu servidor local com a senha definida no passo anterior.
3.  Crie um novo banco de dados chamado `restaurantev2`, ou qualquer nome que preferir.
4.  Abra a **Query Tool** para o banco criado anteriormente.
5.  Cole e execute o script SQL abaixo para criar toda a estrutura de tabelas.
    ```sql

    CREATE TABLE funcionarios (
    id_funcionario serial PRIMARY KEY,
    nome varchar(100),
    cargo varchar(50),
    salario decimal,
    telefone varchar(15)
    );
    
    CREATE TABLE mesas (
        id_mesa serial PRIMARY KEY,
        numero integer UNIQUE NOT NULL,
        capacidade integer
    );
    
    CREATE TABLE produtos (
        id_produto serial PRIMARY KEY,
        nome varchar(100),
        unidade_medida varchar(20),
        quantidade decimal
    );
    
    CREATE TABLE item (
        id_items serial PRIMARY KEY,
        nome varchar(100),
        preco_venda decimal,
        descricao varchar(255)
    );
    
    CREATE TABLE pedidos (
        id_pedido serial PRIMARY KEY,
        data_pedido timestamp,
        status varchar(50),
        fk_mesas_id_mesa integer,
        fk_funcionarios_id_funcionario integer,
        CONSTRAINT fk_pedidos_mesas FOREIGN KEY (fk_mesas_id_mesa) REFERENCES mesas (id_mesa) ON DELETE CASCADE,
        CONSTRAINT fk_pedidos_funcionarios FOREIGN KEY (fk_funcionarios_id_funcionario) REFERENCES funcionarios (id_funcionario) ON DELETE SET NULL
    );
    
    CREATE TABLE pedido_itens (
        id_pedido_item serial PRIMARY KEY,
        quantidade integer,
        fk_pedidos_id_pediido integer,
        fk_item_id_items integer,
        CONSTRAINT fk_pi_pedidos FOREIGN KEY (fk_pedidos_id_pediido) REFERENCES pedidos (id_pedido) ON DELETE CASCADE,
        CONSTRAINT fk_pi_item FOREIGN KEY (fk_item_id_items) REFERENCES item (id_items) ON DELETE CASCADE
    );
    
    CREATE TABLE receitas (
        id_receita serial PRIMARY KEY,
        quantidade_necessaria decimal,
        fk_item_id_items integer,
        fk_produtos_id_produto integer,
        CONSTRAINT fk_receitas_item FOREIGN KEY (fk_item_id_items) REFERENCES item (id_items) ON DELETE CASCADE,
        CONSTRAINT fk_receitas_produtos FOREIGN KEY (fk_produtos_id_produto) REFERENCES produtos (id_produto) ON DELETE CASCADE
    );
    
    DELETE FROM pedido_itens;
    DELETE FROM receitas;
    DELETE FROM pedidos;
    DELETE FROM item;
    DELETE FROM produtos;
    DELETE FROM funcionarios;
    DELETE FROM mesas;
    
    ALTER SEQUENCE funcionarios_id_funcionario_seq RESTART WITH 1;
    ALTER SEQUENCE mesas_id_mesa_seq RESTART WITH 1;
    ALTER SEQUENCE produtos_id_produto_seq RESTART WITH 1;
    ALTER SEQUENCE item_id_items_seq RESTART WITH 1;
    ALTER SEQUENCE pedidos_id_pedido_seq RESTART WITH 1;
    
    INSERT INTO funcionarios (nome, cargo, salario, telefone) VALUES
    ('João Silva', 'Garçom', 2200.00, '47999887766'),
    ('Maria Oliveira', 'Garçonete', 2250.00, '47988776655'),
    ('Carlos Pereira', 'Gerente', 4500.00, '47977665544');
    
    INSERT INTO mesas (numero, capacidade) VALUES
    (1, 4),
    (2, 2),
    (3, 6),
    (4, 4),
    (5, 8);
    
    INSERT INTO produtos (nome, unidade_medida, quantidade) VALUES
    ('Batata', 'Quilogramas', 20.0),
    ('Óleo de Soja', 'Litros', 10.0),
    ('Sal', 'Gramas', 50.0),
    ('Pão de Hambúrguer', 'Unidades', 50),
    ('Carne de Hambúrguer', 'Gramas', 7500.0),
    ('Queijo Cheddar', 'Gramas', 2000.0),
    ('Alface', 'Gramas', 1000.0),
    ('Tomate', 'Gramas', 2000.0),
    ('Refrigerante Cola', 'Unidades', 300);
    
    INSERT INTO item (nome, preco_venda, descricao) VALUES
    ('Batata Frita Média', 15.00, 'Porção de 300g de batata frita crocante'),
    ('Hambúrguer Clássico', 25.00, 'Pão, carne 150g e queijo cheddar'),
    ('X-Salada', 28.00, 'Pão, carne 150g, queijo, alface e tomate'),
    ('Refrigerante Lata', 6.00, 'Lata de 350ml');
    
    INSERT INTO receitas (fk_item_id_items, fk_produtos_id_produto, quantidade_necessaria) VALUES
    (1, 1, 0.3),
    (1, 3, 2.5);
    
    INSERT INTO receitas (fk_item_id_items, fk_produtos_id_produto, quantidade_necessaria) VALUES
    (2, 4, 1),
    (2, 5, 150),
    (2, 6, 20);
    
    INSERT INTO receitas (fk_item_id_items, fk_produtos_id_produto, quantidade_necessaria) VALUES
    (3, 4, 1),
    (3, 5, 150),
    (3, 6, 20),
    (3, 7, 30),
    (3, 8, 40);
    
    INSERT INTO receitas (fk_item_id_items, fk_produtos_id_produto, quantidade_necessaria) VALUES
    (4, 9, 1);
    
    INSERT INTO pedidos (data_pedido, status, fk_mesas_id_mesa, fk_funcionarios_id_funcionario) VALUES
    ('2025-09-20', 'Pago', 1, 1),
    ('2025-09-21', 'Pago', 3, 2),
    ('2025-09-21', 'Cancelado', 2, 1),
    ('2025-09-22', 'Pago', 5, 2),
    ('2025-09-28', 'Pago', 1, 1),
    ('2025-09-29', 'Ativo', 4, 2);
    
    INSERT INTO pedido_itens (fk_pedidos_id_pediido, fk_item_id_items, quantidade) VALUES
    (1, 1, 2),
    (1, 4, 2);
    
    INSERT INTO pedido_itens (fk_pedidos_id_pediido, fk_item_id_items, quantidade) VALUES
    (2, 2, 1),
    (2, 1, 1),
    (2, 4, 1);
    
    INSERT INTO pedido_itens (fk_pedidos_id_pediido, fk_item_id_items, quantidade) VALUES
    (3, 3, 1);
    
    INSERT INTO pedido_itens (fk_pedidos_id_pediido, fk_item_id_items, quantidade) VALUES
    (4, 3, 1),
    (4, 1, 1),
    (4, 4, 1);
    
    INSERT INTO pedido_itens (fk_pedidos_id_pediido, fk_item_id_items, quantidade) VALUES
    (5, 2, 2),
    (5, 4, 2);
    
    INSERT INTO pedido_itens (fk_pedidos_id_pediido, fk_item_id_items, quantidade) VALUES
    (6, 1, 1);
    ```

### Passo 4: Configurar o Projeto no IntelliJ IDEA

1.  **Estrutura de Pastas:** Crie uma pasta `C:\RestauranteApp`. Dentro dela, coloque a pasta `src` do projeto.
    - **[Clique aqui para baixar o arquivo src.zip diretamente](https://raw.githubusercontent.com/vieroBruno/Banco-II/main/docs/src.zip)**
2.  **Driver JDBC:** Baixe o arquivo `postgresql-42.7.7.jar` do [site oficial do PostgreSQL](https://jdbc.postgresql.org/download/) e coloque-o também dentro de `C:\RestauranteApp`.
3.  **Abra no IntelliJ:**
    - Vá em `File` > `Open...` e selecione a pasta `C:\RestauranteApp`.
4.  **Adicione o Driver JDBC como Biblioteca:**
    - Vá em `File` > `Project Structure...` (ou pressione `Ctrl+Alt+Shift+S`).
    - No menu lateral, selecione `Libraries`.
    - Clique no `+` e escolha `Java`.
    - Navegue até `C:\RestauranteApp` e selecione o arquivo `postgresql-42.7.7.jar`.
    - Clique em `OK` para confirmar.
5.  **Compile e Execute:**
    - Encontre o arquivo `Menu.java` na árvore de projeto (dentro de `src/view`).
    - Clique com o botão direito sobre o arquivo `Menu.java`.
    - Selecione a opção **`Run 'Menu.main()'`**.

O IntelliJ IDEA irá compilar e executar o sistema automaticamente. O menu principal aparecerá no painel "Run" na parte inferior da IDE.

---

## Ambiente WSL (Ubuntu/Debian)

Guia para configurar o projeto no WSL e executá-lo via IntelliJ IDEA no Windows.

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

1.  Crie o banco de dados `restauranteV2` ou o nome que preferir:
    ```sh
    sudo -u postgres createdb nomeDoBanco
    ```
2.  Conecte-se ao banco:
    ```sh
    sudo -u postgres psql -d nomeDoBanco
    ```
3.  No psql, cole os scripts SQL fornecido no passo a passo do Windows para criar as tabelas e inserir os dados. Após a execução, saia com `\q`.

### Passo 4: Configurar o Projeto no IntelliJ IDEA (com WSL)

1.  **Estrutura de Pastas (no WSL):** No seu terminal WSL, crie uma pasta `~/RestauranteApp`, coloque a pasta `src` dentro dela e baixe o driver JDBC:
    ```sh
    mkdir ~/RestauranteApp
    cd ~/RestauranteApp
    # Coloque sua pasta 'src' aqui
    wget [https://jdbc.postgresql.org/download/postgresql-42.7.7.jar](https://jdbc.postgresql.org/download/postgresql-42.7.7.jar)
    ```
2.  **Abra o Projeto no IntelliJ:**
    - Vá em `File` > `Open...`.
    - Na barra de endereço do explorador de arquivos, digite `\\wsl$` e pressione Enter.
    - Navegue pela sua distribuição (ex: `Ubuntu`) até a pasta do projeto: `\home\<seu_usuario_wsl>\RestauranteApp`.
    - Clique em `OK` para abrir o projeto.
3.  **Configure o JDK do WSL:**
    - Vá em `File` > `Project Structure...`.
    - Em `Project Settings` > `Project`, no campo `SDK`, clique em `Add SDK` > `JDK...`.
    - O IntelliJ deve detectar automaticamente os JDKs instalados no WSL. Selecione o `OpenJDK 17` do WSL (o caminho será algo como `\\wsl$\Ubuntu\usr\lib\jvm\...`).
4.  **Adicione o Driver JDBC como Biblioteca:**
    - Siga os mesmos passos da configuração para Windows (`File` > `Project Structure...` > `Libraries`), mas ao adicionar o `.jar`, navegue pelo caminho `\\wsl$` até a pasta do seu projeto para selecionar o arquivo `postgresql-42.7.7.jar`.
5.  **Compile e Execute:**
    - Encontre o arquivo `Menu.java` (`src/view/Menu.java`).
    - Clique com o botão direito e selecione **`Run 'Menu.main()'`**.


O IntelliJ usará o JDK configurado dentro do WSL para compilar e executar seu projeto, conectando-se ao banco de dados PostgreSQL que também está rodando no WSL.



