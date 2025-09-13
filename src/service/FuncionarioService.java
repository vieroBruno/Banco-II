package service;

import model.Funcionario;
import repository.jdbc.JdbcFuncionarioRepository;

import java.util.HashSet;


public class FuncionarioService {
    private final JdbcFuncionarioRepository repository;

    public FuncionarioService(JdbcFuncionarioRepository repository) {
        this.repository = repository;
    }

    public void cadastrarFuncionario(Funcionario funcionario) {
        repository.save(funcionario);
    }

    public void editarFuncionario(Funcionario funcionario) {
        repository.update(funcionario);
    }

    public HashSet listarFuncionario() {
        return repository.listAll();
    }

}
