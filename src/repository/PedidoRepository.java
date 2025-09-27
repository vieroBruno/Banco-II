package repository;

import model.Pedido;

import java.util.List;
import java.util.Map;

public interface PedidoRepository {

    int save(Pedido pedido);
    void update(Pedido pedido);
    void delete(int id_pedido);
    Pedido findById(int id_pedido);
    List<Pedido> listAll();
    List<Pedido> listAllAtivos();
    Map<Integer, Double> listarPedidosAtivosComTotal();
    boolean existePedidoAtivoNaMesa(int id_mesa);
}