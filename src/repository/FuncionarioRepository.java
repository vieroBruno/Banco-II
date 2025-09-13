package repository;

import model.Funcionario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;

public interface FuncionarioRepository {

    void save(Funcionario funcionario);
    void update(Funcionario funcionario);
    void delete(int id_funcionario);
    Funcionario findById(int id_funcionario);
    HashSet listAll();
}
