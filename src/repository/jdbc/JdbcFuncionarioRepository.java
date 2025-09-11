package repository.jdbc;

import exception.RepositoryException;
import model.Funcionario;
import repository.FuncionarioRepository;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.HashSet;

public class JdbcFuncionarioRepository extends Conexao implements FuncionarioRepository {

    @Override
    public void save(Funcionario funcionario) {
        Conect();
        String query = "INSERT INTO FUNCIONARIOS (nome, cargo, salario, telefone) VALUES (?,?,?,?)";

        try {
            PreparedStatement st;
            st = con.prepareStatement(query);
            st.setString(1, funcionario.getNome());
            st.setString(2, funcionario.getCargo());
            st.setDouble(3, funcionario.getSalario());
            st.setString(4, funcionario.getTelefone());
            st.execute();
            st.close();
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao salvar funcionário", e);
        }
        closeConnection();
    }
    public void update(Funcionario funcionario, Connection con) throws SQLException {
        PreparedStatement st;
        st = con.prepareStatement("UPDATE FUNCIONARIOS SET nome=?, cargo=?, salario=?, telefone=? WHERE id_funcionario=?");
        st.setString(1, funcionario.getNome());
        st.setString(2, funcionario.getCargo());
        st.setDouble(3, funcionario.getSalario());
        st.setString(4, funcionario.getTelefone());
        st.setInt(5, funcionario.getIdFuncionario());
        st.execute();
        st.close();
    }
    public void delete(int id_funcionario, Connection con) throws SQLException {
        PreparedStatement st;
        st = con.prepareStatement("DELETE FROM FUNCIONARIOS  WHERE id_funcionario=?");
        st.setInt(1, id_funcionario);
        st.execute();
        st.close();
    }
    public Funcionario findById(int id_funcionario, Connection con) {
        return null;
    }
    public HashSet listAll(Connection con) throws SQLException {
        Statement st;
        HashSet list = new HashSet();
            st = con.createStatement();
            String sql = "SELECT nome, cargo, salario, telefone FROM FUNCIONARIOS";
            ResultSet result = st.executeQuery(sql);
            while(result.next()) {
                list.add(new Funcionario(
                    result.getString(1),
                    result.getString(2),
                    result.getDouble(3),
                    result.getString(4)
                ));
            }
        return list;
    }

}

