package service;

import model.Mesa;
import repository.jdbc.JdbcMesaRepository;

import java.util.List;

public class MesaService {

	private final JdbcMesaRepository repository;

	public MesaService(JdbcMesaRepository repository) {
		this.repository = repository;
	}

	public void cadastrarMesa(Mesa mesa) {
		repository.save(mesa);
	}

	public void editarMesa(Mesa mesa) {
		repository.update(mesa);
	}

	public List<Mesa> listarMesa() {
		return repository.listAll();
	}

	public void excluirMesa(int id_mesa) {
		repository.delete(id_mesa);
	}

    public Mesa findById(int id_mesa) {
        return repository.findById(id_mesa);
    }

    public Mesa findByNumero(int numero) {
        return repository.findByNumero(numero);
    }
}
