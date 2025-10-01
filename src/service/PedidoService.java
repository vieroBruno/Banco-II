package service;

import model.Pedido;
import repository.PedidoRepository;

import java.util.List;
import java.util.Map;

public class PedidoService {

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public int cadastrarPedido(Pedido pedido) {
        if (repository.existePedidoAtivoNaMesa(pedido.getId_mesa())) {
            return -1;
        }
        return repository.save(pedido);
    }

    public void editarPedido(Pedido pedido) {
        repository.update(pedido);
    }

    public List<Pedido> listarPedido() {
        return repository.listAll();
    }

    public List<Pedido> listarPedidosAtivos() {
        return repository.listAllAtivos();
    }

    public Map<Integer, Double> listarPedidosAtivosComTotal() {
        return repository.listarPedidosAtivosComTotal();
    }

    public void excluirPedido(int id_pedido) {
        repository.delete(id_pedido);
    }

    public Pedido findById(int id_pedido) {
        return repository.findById(id_pedido);
    }
}