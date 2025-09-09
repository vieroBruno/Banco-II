package repository.jdbc;

import model.Funcionario;
import repository.FuncionarioRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.List;

public class JdbcFuncionarioRepository implements FuncionarioRepository {

    @Override
    public void save(Funcionario funcionario, Connection con) throws SQLException {
        PreparedStatement st;
        st = con.prepareStatement("INSERT INTO FUNCIONARIOS (nome, cargo, salario, telefone) VALUES (?,?,?,?)");
        st.setString(1, funcionario.getNome());
        st.setString(2, funcionario.getCargo());
        st.setDouble(3, funcionario.getSalario());
        st.setString(4, funcionario.getTelefone());
        st.execute();
        st.close();
    }
    public void update(Funcionario funcionario, Connection con) throws SQLException {
        PreparedStatement st;
        st = con.prepareStatement("UPDATE FUNCIONARIOS SET nome=?, cargo=?, salario=?, telefone=? WHERE id_funcionario=?");
        st.setString(1, funcionario.getNome());
        st.setString(2, funcionario.getCargo());
        st.setDouble(3, funcionario.getSalario());
        st.setString(4, funcionario.getTelefone());
        st.setInt(5, funcionario.getId_fornecedor());
        st.execute();
        st.close();
    }
    public void delete(int id_funcionario, Connection con) {

    }
    public Funcionario findById(int id_funcionario, Connection con) {
        return null;
    }
    public HashSet listAll(Connection con) throws SQLException {
        Statement st;
        HashSet list = new HashSet();
            st = con.createStatement();
            String sql = "SELECT " +
                    "id_funcionario, " +
                    "nome, " +
                    "cargo, " +
                    "salario, " +
                    "telefone " +
                "FROM FUNCIONARIOS";
            ResultSet result = st.executeQuery(sql);
            while(result.next()) {
                list.add(new Funcionario(
                    result.getInt(1),
                    result.getString(2),
                    result.getString(3),
                    result.getDouble(4),
                    result.getString(5)
                ));
            }
        return list;
    }

}

