package repository;

import model.Mesa;

import java.util.List;

public interface MesaRepository {

    void save(Mesa mesa);
    void update(Mesa mesa);
    void delete(int id_mesa);
    Mesa findById(int id_mesa);
    List<Mesa> listAll();
    Mesa findByNumero(int numero);

}
