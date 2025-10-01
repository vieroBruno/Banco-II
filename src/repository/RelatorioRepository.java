package repository;

import model.Produto;
import model.RelatorioItem; // Import adicionado
import java.time.LocalDate;
import java.util.List;

public interface RelatorioRepository {

    double vendasPorPeriodo(LocalDate inicio, LocalDate fim);
    List<RelatorioItem> itensMaisVendidos(LocalDate inicio, LocalDate fim);
    List<RelatorioItem> itensQueMaisGeramReceita(LocalDate inicio, LocalDate fim);
    List<Produto> relatorioEstoqueBaixo(double nivelMinimo);
}