package service;

import model.PedidoItem;
import repository.PedidoItemRepository;

public class PedidoItemService {

    private final PedidoItemRepository repository;

    public PedidoItemService(PedidoItemRepository repository) {
        this.repository = repository;
    }

    public void adicionarItemAoPedido(PedidoItem pedidoItem) {
        repository.save(pedidoItem);
    }
}