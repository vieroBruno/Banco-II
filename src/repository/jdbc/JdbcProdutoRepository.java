package repository.jdbc;

import exception.RepositoryException;
import model.Produto;
import repository.ProdutoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcProdutoRepository implements ProdutoRepository {

    @Override
    public void save(Produto produto) {
        String query = "INSERT INTO produtos (nome, unidade_medida, quantidade) VALUES (?,?,?)";

        try {
            Connection con = new Conexao().getConnection();
            PreparedStatement st = con.prepareStatement(query);

            st.setString(1, produto.getNome());
            st.setString(2, produto.getUnidade_medida());
            st.setDouble(3, produto.getQuantidade());
            st.execute();
            System.out.println("Produto cadastrado com sucesso!");
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao salvar produto", e);
        }
    }

    @Override
    public void update(Produto produto) {
        String query = "UPDATE produtos SET nome=?, unidade_medida=?, quantidade=? WHERE id_produto=?";

        try {
             Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query);

            st.setString(1, produto.getNome());
            st.setString(2, produto.getUnidade_medida());
            st.setDouble(3, produto.getQuantidade());
            st.setInt(4, produto.getId_produto());
            st.execute();
            System.out.println("Produto alterado com sucesso!");
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao alterar produto", e);
        }
    }

    @Override
    public void delete(int id_produto) {
        String query = "DELETE FROM produtos WHERE id_produto=?";

        try {
             Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query);

            st.setInt(1, id_produto);
            st.execute();
            System.out.println("Produto excluído com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir produto", e);
        }
    }

    @Override
    public Produto findById(int id_produto) {
        String query = "SELECT id_produto, nome, unidade_medida, quantidade FROM produtos WHERE id_produto = ?";
        Produto produto = null;
        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, id_produto);
            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    produto = new Produto(
                            result.getString("nome"),
                            result.getString("unidade_medida"),
                            result.getDouble("quantidade")
                    );
                    produto.setId_produto(result.getInt("id_produto"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto por ID", e);
        }
        return produto;
    }

    @Override
    public List<Produto> listAll() {
        String query = "SELECT id_produto, nome, unidade_medida, quantidade FROM produtos";
        List<Produto> produtos = new ArrayList<>();

        try  {
             Connection con = new Conexao().getConnection();
             Statement st = con.createStatement();
             ResultSet result = st.executeQuery(query);

            while (result.next()) {
                Produto produto = new Produto(
                        result.getString("nome"),
                        result.getString("unidade_medida"),
                        result.getDouble("quantidade")
                );
                produto.setId_produto(result.getInt("id_produto"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos", e);
        }
        return produtos;
    }
}