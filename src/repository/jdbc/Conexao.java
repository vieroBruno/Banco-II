package repository.jdbc;

import java.awt.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexao {

    Connection con = null;
    ResultSet rts = null;

    public Conexao(){
        String driver = "org.postgresql.Driver";
        String user = "postgres";
        String senha = "123456";
        String url = "jdbc:postgresql://localhost:5432/restaurantev2";

        try {
            Class.forName(driver);
            this.con = (Connection) DriverManager.getConnection(url, user, senha);
        } catch (SQLException | ClassNotFoundException | HeadlessException e) {
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
