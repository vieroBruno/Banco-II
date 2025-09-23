package service;

import model.Pedido;
import repository.PedidoRepository;
import repository.jdbc.JdbcPedidoRepository;

import java.util.List;

public class PedidoService {

	private final PedidoRepository repository;

	public PedidoService(PedidoRepository repository) {
		this.repository = repository;
	}

	public void cadastrarPedido(Pedido pedido) {
		repository.save(pedido);
	}

	public void editarPedido(Pedido pedido) {
		repository.update(pedido);
	}

	public List<Pedido> listarPedido() {
		return repository.listAll();
	}

	public void excluirPedido(int id_pedido) {
		repository.delete(id_pedido);
	}
}