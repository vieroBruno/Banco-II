package repository.jdbc;

import exception.RepositoryException;
import model.Funcionario;
import repository.FuncionarioRepository;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class JdbcFuncionarioRepository implements FuncionarioRepository {

    @Override
    public void save(Funcionario funcionario) {
        String query = "INSERT INTO FUNCIONARIOS (nome, cargo, salario, telefone) VALUES (?,?,?,?)";

        try {
            Connection con = new Conexao().getConnection();
            PreparedStatement st  = con.prepareStatement(query);

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
    }

    @Override
    public void update(Funcionario funcionario){
        String query = "UPDATE FUNCIONARIOS SET nome=?, cargo=?, salario=?, telefone=? WHERE id_funcionario=?";

        try {
            Connection con = new Conexao().getConnection();
            PreparedStatement st  = con.prepareStatement(query);

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
    }

    @Override
    public void delete(int id_funcionario){
        String query= "DELETE FROM FUNCIONARIOS WHERE id_funcionario =?";

        try {
            Connection con = new Conexao().getConnection();
            PreparedStatement st  = con.prepareStatement(query);

            st.setInt(1, id_funcionario);
            st.execute();
            st.close();
            System.out.println("Funcionário excluído com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir funcionário",e);
        }
    }
    @Override
    public Funcionario findById(int id_funcionario) {
        String query = "SELECT id_funcionario, nome, cargo, salario, telefone FROM FUNCIONARIOS WHERE id_funcionario = ?";
        Funcionario funcionario = null;

        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setInt(1, id_funcionario);
            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    funcionario = new Funcionario(
                            result.getInt("id_funcionario"),
                            result.getString("nome"),
                            result.getString("cargo"),
                            result.getDouble("salario"),
                            result.getString("telefone")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionário por ID", e);
        }
        return funcionario;
    }

    @Override
    public List<Funcionario> listAll() {
        String query = "SELECT id_funcionario, nome, cargo, salario, telefone FROM FUNCIONARIOS";
        List<Funcionario> funcionarios = new ArrayList<>();

        try {
            Connection con = new Conexao().getConnection();
            Statement st = con.createStatement();
            ResultSet result = st.executeQuery(query);
            while(result.next()) {
                funcionarios.add(new Funcionario(
                        result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getDouble(4),
                        result.getString(5)
                ));
            }
        } catch (SQLException e) {
            throw  new RuntimeException("Erro ao listar funcionários", e);
        }
        return funcionarios;
    }

}