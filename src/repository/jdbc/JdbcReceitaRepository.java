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
			System.out.println("Receita cadastrada com sucesso!");
		} catch (SQLException e) {
			throw new RepositoryException("Erro ao salvar receita", e);
		}
	}

	@Override
	public void update(Receita receita) {
		String query = "UPDATE receitas SET fk_item_id_items=?, fk_produtos_id_produto=?, quantidade_necessaria=? WHERE id_receitas=?";

		try (Connection con = new Conexao().getConnection();
		     PreparedStatement st = con.prepareStatement(query)) {

			st.setInt(1, receita.getId_item());
			st.setInt(2, receita.getId_produto());
			st.setDouble(3, receita.getQuantidade());
			//st.setInt(4, receita.getId_receita()); // Model não possui Id_receita
			st.execute();
			System.out.println("Receita alterada com sucesso!");
		} catch (SQLException e) {
			throw new RepositoryException("Erro ao alterar receita", e);
		}
	}

	@Override
	public void delete(int id_receita) {
		String query = "DELETE FROM receitas WHERE id_receitas=?";

		try (Connection con = new Conexao().getConnection();
		     PreparedStatement st = con.prepareStatement(query)) {

			st.setInt(1, id_receita);
			st.execute();
			System.out.println("Receita excluída com sucesso!");
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao excluir receita", e);
		}
	}

	@Override
	public Receita findById(int id_receita) {
		// Implementação futura
		return null;
	}

	@Override
	public List<Produto> listOne(int id_item) {
		String query = "select p.nome,\n" +
                "\t   p.quantidade,\n" +
                "\t   p.unidade_medida \n" +
                "from item\n" +
                "join receitas r \n" +
                "on r.fk_item_id_items = item.id_items\n" +
                "join produtos p\n" +
                "on p.id_produto  = r.fk_produtos_id_produto \n" +
                "and item.id_items ="+ id_item;
		List<Produto> receitas = new ArrayList<>();

		try (Connection con = new Conexao().getConnection();
             Statement st = con.createStatement();
		     ResultSet result = st.executeQuery(query)) {

            while (result.next()) {
                String nomeProduto = result.getString("nome");
                int quantidade = result.getInt("quantidade");
                String unidadeMedida = result.getString("unidade_medida");

                // Formata a string para exibição
                Produto produtos = new Produto(nomeProduto, unidadeMedida, quantidade );
                receitas.add(produtos);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar receitas", e);
		}
		return receitas;
	}
}