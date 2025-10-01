package repository.jdbc;

import exception.RepositoryException;
import model.Produto;
import model.Receita;
import repository.ReceitaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcReceitaRepository implements ReceitaRepository {

	@Override
	public void save(Receita receita) {
		String query = "INSERT INTO receitas (fk_item_id_items, fk_produtos_id_produto, quantidade_necessaria) VALUES (?,?,?)";

		try (Connection con = new Conexao().getConnection();
		     PreparedStatement st = con.prepareStatement(query)) {

			st.setInt(1, receita.getId_item());
			st.setInt(2, receita.getId_produto());
			st.setDouble(3, receita.getQuantidade());
			st.execute();
			System.out.println("Produto adicionado a receita com sucesso!");
		} catch (SQLException e) {
			throw new RepositoryException("Erro ao salvar receita", e);
		}
	}

	@Override
	public void update(Receita receita) {
        String query = "UPDATE receitas SET quantidade_necessaria = ? WHERE fk_item_id_items = ? AND fk_produtos_id_produto = ?";

        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setDouble(1, receita.getQuantidade());
            st.setInt(2, receita.getId_item());
            st.setInt(3, receita.getId_produto());
            st.execute();
            System.out.println("Quantidade do produto alterada com sucesso!");
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao alterar receita", e);
        }
	}

	@Override
	public void delete(int id_item, int id_produto) {
        String query = "DELETE FROM receitas WHERE fk_item_id_items = ? AND fk_produtos_id_produto = ?";

        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setInt(1, id_item);
            st.setInt(2, id_produto);
            st.execute();
            System.out.println("Produto removido da receita com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir produto da receita", e);
        }
	}

	@Override
	public Receita findById(int id_receita) {
		// Implementação futura
		return null;
	}

	@Override
    public List<Produto> listarProdutosItem(int id_item) {
        String query = "SELECT p.id_produto, p.nome, p.unidade_medida, r.quantidade_necessaria " +
                "FROM item i " +
                "JOIN receitas r ON r.fk_item_id_items = i.id_items " +
                "JOIN produtos p ON p.id_produto = r.fk_produtos_id_produto " +
                "WHERE i.id_items = ?";
        List<Produto> produtos = new ArrayList<>();

        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setInt(1, id_item);

            try (ResultSet result = st.executeQuery()) {
                while (result.next()) {
                    Produto produto = new Produto(
                            result.getString("nome"),
                            result.getString("unidade_medida"),
                            result.getDouble("quantidade_necessaria")
                    );
                    produto.setId_produto(result.getInt("id_produto"));
                    produtos.add(produto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar os produtos da receita", e);
        }
        return produtos;
    }

    @Override
    public boolean produtoJaExisteNaReceita(int id_item, int id_produto) {
        String query = "SELECT COUNT(*) FROM receitas WHERE fk_item_id_items = ? AND fk_produtos_id_produto = ?";
        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, id_item);
            st.setInt(2, id_produto);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar se produto existe na receita", e);
        }
        return false;
    }
}