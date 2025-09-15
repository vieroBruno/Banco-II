package repository;

import model.Funcionario;

import java.util.List;


public interface FuncionarioRepository {

    void save(Funcionario funcionario);
    void update(Funcionario funcionario);
    void delete(int id_funcionario);
    Funcionario findById(int id_funcionario);
    List<Funcionario> listAll();
}
