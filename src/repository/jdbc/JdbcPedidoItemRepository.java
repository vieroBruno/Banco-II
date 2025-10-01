package repository.jdbc;

import exception.RepositoryException;
import model.PedidoItem;
import repository.PedidoItemRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcPedidoItemRepository implements PedidoItemRepository {

    @Override
    public void save(PedidoItem pedidoItem) {
        String query = "INSERT INTO pedido_itens (fk_pedidos_id_pediido, fk_item_id_items, quantidade) VALUES (?, ?, ?)";

        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setInt(1, pedidoItem.getId_pedido());
            st.setInt(2, pedidoItem.getId_item());
            st.setInt(3, pedidoItem.getQuantidade());
            st.execute();

        } catch (SQLException e) {
            throw new RepositoryException("Erro ao salvar item do pedido", e);
        }
    }
}