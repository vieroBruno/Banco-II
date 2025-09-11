package repository;

import model.Funcionario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;

public interface FuncionarioRepository {

    void save(Funcionario funcionario);
    void update(Funcionario funcionario, Connection con) throws SQLException;
    void delete(int id_funcionario, Connection con) throws SQLException;
    Funcionario findById(int id_funcionario, Connection con);
    HashSet listAll(Connection con) throws SQLException;
}
