package service;

import model.Produto;
import repository.jdbc.JdbcProdutoRepository;

import java.util.List;

public class ProdutoService {

    private final JdbcProdutoRepository repository;

    public ProdutoService(JdbcProdutoRepository repository) {
        this.repository = repository;
    }

    public void cadastrarProduto(Produto produto) {
        repository.save(produto);
    }

    public void editarProduto(Produto produto) {
        repository.update(produto);
    }

    public List<Produto> listarProduto() {
        return repository.listAll();
    }

    public void excluirProduto(int id_produto) {
        repository.delete(id_produto);
    }

    public Produto findById(int id_produto) {
        return repository.findById(id_produto);
    }
}