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
            System.out.println("Funcionário cadastrado com sucesso!");
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao salvar funcionário", e);
        }
        closeConnection();
    }
    public void update(Funcionario funcionario){
        Conect();
        String query = "UPDATE FUNCIONARIOS SET nome=?, cargo=?, salario=?, telefone=? WHERE id_funcionario=?";

        try {
            PreparedStatement st;
            st = con.prepareStatement(query);
            st.setString(1, funcionario.getNome());
            st.setString(2, funcionario.getCargo());
            st.setDouble(3, funcionario.getSalario());
            st.setString(4, funcionario.getTelefone());
            st.setInt(5, funcionario.getIdFuncionario());
            st.execute();
            st.close();
            System.out.println("Funcionário alterado com sucesso!");
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao alterar funcionário", e);
        }
        closeConnection();
    }
    public void delete(int id_funcionario){
        Conect();
        String query= "DELETE FROM FUNCIONARIOS WHERE id_funcionario =?";

        try{
            PreparedStatement st;
            st = con.prepareStatement(query);
            st.setInt(1, id_funcionario);
            st.execute();
            st.close();
            System.out.println("Funcionário excluído com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir funcionário",e);
        }
        closeConnection();
    }
    public Funcionario findById(int id_funcionario) {
        return null;
    }
    public HashSet listAll() {
        Conect();
        Statement st;
        String query = "SELECT nome, cargo, salario, telefone FROM FUNCIONARIOS";
        HashSet list = new HashSet();

        try {
            st = con.createStatement();
            ResultSet result = st.executeQuery(query);
            while(result.next()) {
                list.add(new Funcionario(
                    result.getString(1),
                    result.getString(2),
                    result.getDouble(3),
                    result.getString(4)
                ));
            }
        } catch (SQLException e) {
            throw  new RuntimeException("Erro ao listar funcionários", e);
        }
        return list;
    }

}

