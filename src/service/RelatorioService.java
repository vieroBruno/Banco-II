package service;

import model.Produto;
import model.RelatorioItem;
import repository.RelatorioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class RelatorioService {

    private final RelatorioRepository repository;

    public RelatorioService(RelatorioRepository repository) {
        this.repository = repository;
    }

    public double vendasPorPeriodo(LocalDate inicio, LocalDate fim) {
        return repository.vendasPorPeriodo(inicio, fim);
    }

    public List<RelatorioItem> itensMaisVendidos(LocalDate inicio, LocalDate fim) {
        return repository.itensMaisVendidos(inicio, fim);
    }

    public List<RelatorioItem> itensQueMaisGeramReceita(LocalDate inicio, LocalDate fim) {
        return repository.itensQueMaisGeramReceita(inicio, fim);
    }

    public List<Produto> relatorioEstoqueBaixo(double nivelMinimo) {
        return repository.relatorioEstoqueBaixo(nivelMinimo);
    }
}