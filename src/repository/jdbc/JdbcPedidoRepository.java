package repository.jdbc;

import exception.RepositoryException;
import model.Pedido;
import repository.PedidoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcPedidoRepository implements PedidoRepository {

	@Override
	public void save(Pedido pedido) {
		String query = "INSERT INTO pedidos (fk_mesas_id_mesa, fk_funcionarios_id_funcionario, data_pedido, status) VALUES (?,?,?,?)";

		try (Connection con = new Conexao().getConnection();
		     PreparedStatement st = con.prepareStatement(query)) {

			st.setInt(1, pedido.getId_mesa());
			st.setInt(2, pedido.getId_funcionario());
			st.setTimestamp(3, Timestamp.valueOf(pedido.getData_pedido().atStartOfDay()));
			st.setString(4, pedido.getStatus());
			st.execute();
			System.out.println("Pedido cadastrado com sucesso!");
		} catch (SQLException e) {
			throw new RepositoryException("Erro ao salvar pedido", e);
		}
	}

	@Override
	public void update(Pedido pedido) {
		String query = "UPDATE pedidos SET fk_mesas_id_mesa=?, fk_funcionarios_id_funcionario=?, data_pedido=?, status=? WHERE id_pediido=?";

		try (Connection con = new Conexao().getConnection();
		     PreparedStatement st = con.prepareStatement(query)) {

			st.setInt(1, pedido.getId_mesa());
			st.setInt(2, pedido.getId_funcionario());
			st.setTimestamp(3, Timestamp.valueOf(pedido.getData_pedido().atStartOfDay()));
			st.setString(4, pedido.getStatus());
			st.setInt(5, pedido.getId_pedido());
			st.execute();
			System.out.println("Pedido alterado com sucesso!");
		} catch (SQLException e) {
			throw new RepositoryException("Erro ao alterar pedido", e);
		}
	}

	@Override
	public void delete(int id_pedido) {
		String query = "DELETE FROM pedidos WHERE id_pediido=?";

		try (Connection con = new Conexao().getConnection();
		     PreparedStatement st = con.prepareStatement(query)) {

			st.setInt(1, id_pedido);
			st.execute();
			System.out.println("Pedido excluído com sucesso!");
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao excluir pedido", e);
		}
	}

	@Override
	public Pedido findById(int id_pedido) {
		// Implementação futura
		return null;
	}

	@Override
	public List<Pedido> listAll() {
		String query = "SELECT id_pediido, fk_mesas_id_mesa, fk_funcionarios_id_funcionario, data_pedido, status FROM pedidos";
		List<Pedido> pedidos = new ArrayList<>();

		try (Connection con = new Conexao().getConnection();
		     Statement st = con.createStatement();
		     ResultSet result = st.executeQuery(query)) {

			while (result.next()) {
				Pedido pedido = new Pedido(
					result.getInt("fk_mesas_id_mesa"),
					result.getInt("id_pediido"),
					result.getTimestamp("data_pedido").toLocalDateTime().toLocalDate(),
					result.getString("status")
				);
				pedido.setId_funcionario(result.getInt("fk_funcionarios_id_funcionario"));
				pedidos.add(pedido);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar pedidos", e);
		}
		return pedidos;
	}
}