package repository;

import model.Funcionario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

public interface FuncionarioRepository {

    void save(Funcionario funcionario, Connection con) throws SQLException;
    void update(Funcionario funcionario, Connection con) throws SQLException;
    void delete(int id_funcionario, Connection con);
    Funcionario findById(int id_funcionario, Connection con);
    HashSet listAll(Connection con) throws SQLException;
}
