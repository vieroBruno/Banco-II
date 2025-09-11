package service;

import model.Funcionario;
import repository.jdbc.JdbcFuncionarioRepository;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.sql.Connection;


public class FuncionarioService {
    private final JdbcFuncionarioRepository repository;

    public FuncionarioService(JdbcFuncionarioRepository repository) {
        this.repository = repository;
    }

    public void cadastrarFuncionario(Funcionario funcionario){
        repository.save(funcionario);
    }

    public void editarFuncionario(Funcionario funcionario, Connection con) throws SQLException {
        repository.update(funcionario, con);
    }

    public HashSet listarFuncionario(Connection con) throws SQLException {
        return repository.listAll(con);
    }

}
