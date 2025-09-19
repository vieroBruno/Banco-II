package service;

import model.Receita;
import repository.ReceitaRepository;
import repository.jdbc.JdbcReceitaRepository;

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

	public List<Receita> listarReceita() {
		return repository.listAll();
	}

	public void excluirReceita(int id_receita) {
		repository.delete(id_receita);
	}
}