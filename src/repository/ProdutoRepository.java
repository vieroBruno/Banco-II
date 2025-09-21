package repository;

import model.Produto;

import java.util.List;

public interface ProdutoRepository {

    void save(Produto produto);
    void update(Produto produto);
    void delete(int id_produto);
    Produto findById(int id_produto);
    List<Produto> listAll();
}