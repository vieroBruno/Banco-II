package repository;

import model.Pedido;

import java.util.List;

public interface PedidoRepository {

	void save(Pedido pedido);
	void update(Pedido pedido);
	void delete(int id_pedido);
	Pedido findById(int id_pedido);
	List<Pedido> listAll();
}