package repository;

import model.Item;

import java.util.List;

public interface ItemRepository {

	void save(Item item);
	void update(Item item);
	void delete(int id_item);
	Item findById(int id_item);
	List<Item> listAll();
}