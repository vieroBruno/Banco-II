package repository.jdbc;

import exception.RepositoryException;
import model.Mesa;
import repository.MesaRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcMesaRepository implements MesaRepository {

    @Override
    public void save(Mesa mesa) {
        String query = "INSERT INTO MESAS (numero, capacidade) VALUES (?,?)";

        try {
            Connection con = new Conexao().getConnection();
            PreparedStatement st  = con.prepareStatement(query);
            st.setInt(1, mesa.getNumero());
            st.setInt(2, mesa.getCapacidade());
            st.execute();
            st.close();
            System.out.println("Mesa cadastrada com sucesso!");
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao salvar mesa", e);
        }
    }

    @Override
    public void update(Mesa mesa){
        String query = "UPDATE MESAS SET numero=?, capacidade=? WHERE id_mesa=?";

        try {
            Connection con = new Conexao().getConnection();
            PreparedStatement st  = con.prepareStatement(query);

            st.setInt(1, mesa.getNumero());
            st.setInt(2, mesa.getCapacidade());
            st.setInt(3, mesa.getId_mesa());
            st.execute();
            st.close();
            System.out.println("Mesa alterada com sucesso!");
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao alterar mesa", e);
        }
    }

    @Override
    public void delete(int id_mesa){
        String query= "DELETE FROM MESAS WHERE id_mesa=?";

        try {
            Connection con = new Conexao().getConnection();
            PreparedStatement st  = con.prepareStatement(query);

            st.setInt(1, id_mesa);
            st.execute();
            st.close();
            System.out.println("Mesa excluída com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir mesa",e);
        }
    }

    @Override
    public Mesa findById(int id_mesa) {
        String query = "SELECT id_mesa, numero, capacidade FROM MESAS WHERE id_mesa = ?";
        Mesa mesa = null;

        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setInt(1, id_mesa);
            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    mesa = new Mesa(
                            result.getInt("id_mesa"),
                            result.getInt("numero"),
                            result.getInt("capacidade")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar mesa por ID", e);
        }
        return mesa;
    }


    @Override
    public List<Mesa> listAll() {
        String query = "SELECT id_mesa, numero, capacidade FROM MESAS";
        List<Mesa> mesas= new ArrayList<>();

        try {
            Connection con = new Conexao().getConnection();
            Statement st = con.createStatement();
            ResultSet result = st.executeQuery(query);
            while(result.next()) {
                mesas.add(new Mesa(
                        result.getInt(1),
                        result.getInt(2),
                        result.getInt(3)
                ));
            }
        } catch (SQLException e) {
            throw  new RuntimeException("Erro ao listar mesas", e);
        }
        return mesas;
    }

    @Override
    public Mesa findByNumero(int numero) {
        String query = "SELECT id_mesa, numero, capacidade FROM MESAS WHERE numero = ?";
        Mesa mesa = null;

        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setInt(1, numero);
            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    mesa = new Mesa(
                            result.getInt("id_mesa"),
                            result.getInt("numero"),
                            result.getInt("capacidade")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar mesa por número", e);
        }
        return mesa;
    }
}