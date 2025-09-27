package repository.jdbc;

import exception.RepositoryException;
import model.Pedido;
import repository.PedidoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcPedidoRepository implements PedidoRepository {

    @Override
    public int save(Pedido pedido) {
        String query = "INSERT INTO pedidos (fk_mesas_id_mesa, fk_funcionarios_id_funcionario, data_pedido, status) VALUES (?,?,?,?)";
        int idGerado = 0;

        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            st.setInt(1, pedido.getId_mesa());
            st.setInt(2, pedido.getId_funcionario());
            st.setTimestamp(3, Timestamp.valueOf(pedido.getData_pedido().atStartOfDay()));
            st.setString(4, pedido.getStatus());
            st.execute();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                }
            }

            System.out.println("Pedido cadastrado com sucesso!");
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao salvar pedido", e);
        }
        return idGerado;
    }

    @Override
    public void update(Pedido pedido) {
        String query = "UPDATE pedidos SET fk_mesas_id_mesa=?, fk_funcionarios_id_funcionario=?, data_pedido=?, status=? WHERE id_pedido=?";

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
        String query = "DELETE FROM pedidos WHERE id_pedido=?";

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
        String query = "SELECT id_pedido, fk_mesas_id_mesa, fk_funcionarios_id_funcionario, data_pedido, status FROM pedidos WHERE id_pedido = ?";
        Pedido pedido = null;

        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setInt(1, id_pedido);
            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    pedido = new Pedido(
                            result.getInt("fk_mesas_id_mesa"),
                            result.getInt("id_pedido"),
                            result.getTimestamp("data_pedido").toLocalDateTime().toLocalDate(),
                            result.getString("status")
                    );
                    pedido.setId_funcionario(result.getInt("fk_funcionarios_id_funcionario"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedido por ID", e);
        }
        return pedido;
    }

    @Override
    public List<Pedido> listAll() {
        String query = "SELECT id_pedido, fk_mesas_id_mesa, fk_funcionarios_id_funcionario, data_pedido, status FROM pedidos";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection con = new Conexao().getConnection();
             Statement st = con.createStatement();
             ResultSet result = st.executeQuery(query)) {

            while (result.next()) {
                Pedido pedido = new Pedido(
                        result.getInt("fk_mesas_id_mesa"),
                        result.getInt("id_pedido"),
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

    @Override
    public List<Pedido> listAllAtivos() {
        String query = "SELECT id_pedido, fk_mesas_id_mesa, fk_funcionarios_id_funcionario, data_pedido, status FROM pedidos WHERE status = 'Ativo'";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection con = new Conexao().getConnection();
             Statement st = con.createStatement();
             ResultSet result = st.executeQuery(query)) {

            while (result.next()) {
                Pedido pedido = new Pedido(
                        result.getInt("fk_mesas_id_mesa"),
                        result.getInt("id_pedido"),
                        result.getTimestamp("data_pedido").toLocalDateTime().toLocalDate(),
                        result.getString("status")
                );
                pedido.setId_funcionario(result.getInt("fk_funcionarios_id_funcionario"));
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pedidos ativos", e);
        }
        return pedidos;
    }

    @Override
    public Map<Integer, Double> listarPedidosAtivosComTotal() {
        String query = "SELECT m.numero, SUM(pi.quantidade * i.preco_venda) as total " +
                "FROM pedidos p " +
                "JOIN mesas m ON p.fk_mesas_id_mesa = m.id_mesa " +
                "LEFT JOIN pedido_itens pi ON p.id_pedido = pi.fk_pedidos_id_pediido " +
                "LEFT JOIN item i ON pi.fk_item_id_items = i.id_items " +
                "WHERE p.status = 'Ativo' " +
                "GROUP BY m.numero";
        Map<Integer, Double> totaisPorMesa = new HashMap<>();

        try (Connection con = new Conexao().getConnection();
             Statement st = con.createStatement();
             ResultSet result = st.executeQuery(query)) {

            while (result.next()) {
                totaisPorMesa.put(result.getInt("numero"), result.getDouble("total"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar totais de pedidos ativos", e);
        }
        return totaisPorMesa;
    }

    @Override
    public boolean existePedidoAtivoNaMesa(int id_mesa) {
        String query = "SELECT COUNT(*) FROM pedidos WHERE fk_mesas_id_mesa = ? AND status = 'Ativo'";
        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, id_mesa);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar pedido ativo na mesa", e);
        }
        return false;
    }
}