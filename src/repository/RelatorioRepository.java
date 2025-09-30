package repository;

import model.Produto;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RelatorioRepository {

    double vendasPorPeriodo(LocalDate inicio, LocalDate fim);
    Map<String, Integer> itensMaisVendidos(LocalDate inicio, LocalDate fim);
    Map<String, Double> itensQueMaisGeramReceita(LocalDate inicio, LocalDate fim);
    List<Produto> relatorioEstoqueBaixo(double nivelMinimo);
}