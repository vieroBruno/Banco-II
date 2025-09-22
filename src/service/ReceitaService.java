package service;

import model.Produto;
import model.Receita;
import repository.ReceitaRepository;

import java.util.List;

public class ReceitaService {

	private final ReceitaRepository repository;

	public ReceitaService(ReceitaRepository repository) {
		this.repository = repository;
	}

	public void cadastrarReceita(Receita receita) {
		repository.save(receita);
	}

	public void editarReceita(Receita receita) {
		repository.update(receita);
	}

	public List<Produto> listarReceita(int id_item) {
		return repository.listOne(id_item);
	}

	public void excluirReceita(int id_receita) {
		repository.delete(id_receita);
	}
}