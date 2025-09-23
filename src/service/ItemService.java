package service;

import model.Item;
import repository.ItemRepository;
import repository.jdbc.JdbcItemRepository;

import java.util.List;

public class ItemService {

	private final ItemRepository repository;

	public ItemService(ItemRepository repository) {
		this.repository = repository;
	}

	public void cadastrarItem(Item item) {
		repository.save(item);
	}

	public void editarItem(Item item) {
		repository.update(item);
	}

	public List<Item> listarItem() {
		return repository.listAll();
	}

	public void excluirItem(int id_item) {
		repository.delete(id_item);
	}
}