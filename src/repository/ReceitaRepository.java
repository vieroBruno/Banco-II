package repository;

import model.Produto;
import model.Receita;

import java.util.List;

public interface ReceitaRepository {

	void save(Receita receita);
	void update(Receita receita);
	void delete(int id_receita, int id_produto);
	Receita findById(int id_receita);
	List<Produto> listarProdutosItem(int id_item);
}