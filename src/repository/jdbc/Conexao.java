package repository.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexao {

    private Connection con;

    public Conexao() {
        String driver = "org.postgresql.Driver";
        String user = "postgres";
        String senha = "123456";
        String url = "jdbc:postgresql://localhost:5432/restaurantev2";

        try {
            Class.forName(driver);
            this.con = (Connection) DriverManager.getConnection(url, user, senha);
            System.out.println("Conectado com sucesso");
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
            System.exit(1);
        }

    }

    public Connection getConnection() {
        return con;
    }

    public void closeConnection(){
        try {
            this.con.close();
        } catch (SQLException e) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
            System.exit(1);
        }
    }

}
