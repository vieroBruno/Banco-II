package service;

import model.Funcionario;
import repository.jdbc.JdbcFuncionarioRepository;

import java.util.List;


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

    public List<Funcionario> listarFuncionario() {
        return repository.listAll();
    }

    public void excluirFuncionario(int id_funcionario) {
         repository.delete(id_funcionario);
    }

    public Funcionario findById(int id_funcionario) {
        return repository.findById(id_funcionario);
    }

}
