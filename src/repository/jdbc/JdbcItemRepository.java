package repository.jdbc;

import exception.RepositoryException;
import model.Item;
import repository.ItemRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcItemRepository implements ItemRepository {

	@Override
	public void save(Item item) {
		String query = "INSERT INTO item (nome, preco_venda, descricao) VALUES (?,?,?)";

		try {
			Connection con = new Conexao().getConnection();
			PreparedStatement st = con.prepareStatement(query);

			st.setString(1, item.getNome());
			st.setDouble(2, item.getPreco_venda());
			st.setString(3, item.getDescricao());
			st.execute();
			System.out.println("Item cadastrado com sucesso!");
		} catch (SQLException e) {
			throw new RepositoryException("Erro ao salvar item", e);
		}
	}

	@Override
	public void update(Item item) {
		String query = "UPDATE item SET nome=?, preco_venda=?, descricao=? WHERE id_items=?";

		try {
			Connection con = new Conexao().getConnection();
	        PreparedStatement st = con.prepareStatement(query);

			st.setString(1, item.getNome());
			st.setDouble(2, item.getPreco_venda());
			st.setString(3, item.getDescricao());
			st.setInt(4, item.getId_item());
			st.execute();
			System.out.println("Item alterado com sucesso!");
		} catch (SQLException e) {
			throw new RepositoryException("Erro ao alterar item", e);
		}
	}

	@Override
	public void delete(int id_item) {
		String query = "DELETE FROM item WHERE id_items=?";

		try {
			Connection con = new Conexao().getConnection();
	        PreparedStatement st = con.prepareStatement(query);

			st.setInt(1, id_item);
			st.execute();
			System.out.println("Item excluído com sucesso!");
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao excluir item", e);
		}
	}

	@Override
	public Item findById(int id_item) {
		return null;
	}

	@Override
	public List<Item> listAll() {
		String query = "SELECT id_items, nome, preco_venda, descricao FROM item";
		List<Item> items = new ArrayList<>();

		try {
			Connection con = new Conexao().getConnection();
	        Statement st = con.createStatement();
	        ResultSet result = st.executeQuery(query);

			while (result.next()) {
				Item item = new Item(
					result.getString("nome"),
					result.getDouble("preco_venda"),
					result.getString("descricao")
				);
				item.setId_item(result.getInt("id_items"));
				items.add(item);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar items", e);
		}
		return items;
	}
}