package view;

import model.Produto;
import model.RelatorioItem;
import repository.jdbc.JdbcRelatorioRepository;
import service.RelatorioService;
import util.ValidacaoHelper;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class RelatorioView {
    private final Scanner sc = new Scanner(System.in);
    private final RelatorioService relatorioService = new RelatorioService(new JdbcRelatorioRepository());

    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Menu de Relatórios ===");
            System.out.println("1. Vendas por Período");
            System.out.println("2. Itens Mais Vendidos");
            System.out.println("3. Itens que Mais Geram Receita");
            System.out.println("4. Relatório de Estoque Baixo");
            System.out.println("0. Voltar ao Menu Principal");

            int opcao = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");

            switch (opcao) {
                case 1: gerarRelatorioVendasPorPeriodo(); break;
                case 2: gerarRelatorioItensMaisVendidos(); break;
                case 3: gerarRelatorioItensQueMaisGeramReceita(); break;
                case 4: gerarRelatorioEstoqueBaixo(); break;
                case 0: return;
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private void gerarRelatorioVendasPorPeriodo() {
        System.out.println("\n--- Relatório de Vendas por Período ---");
        LocalDate inicio = ValidacaoHelper.lerData(sc, "Digite a data de início (DD/MM/YYYY): ");
        LocalDate fim = ValidacaoHelper.lerData(sc, "Digite a data de fim (DD/MM/YYYY): ");

        if (inicio.isAfter(fim)) {
            System.out.println("Erro: A data de início não pode ser posterior à data de fim.");
            return;
        }

        double faturamento = relatorioService.vendasPorPeriodo(inicio, fim);
        System.out.printf("\nFaturamento total no período de %s a %s: R$ %.2f\n",
                inicio.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                fim.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                faturamento);
    }

    private void gerarRelatorioItensMaisVendidos() {
        System.out.println("\n--- Relatório de Itens Mais Vendidos ---");
        LocalDate inicio = ValidacaoHelper.lerData(sc, "Digite a data de início (DD/MM/YYYY): ");
        LocalDate fim = ValidacaoHelper.lerData(sc, "Digite a data de fim (DD/MM/YYYY): ");

        if (inicio.isAfter(fim)) {
            System.out.println("Erro: A data de início não pode ser posterior à data de fim.");
            return;
        }

        List<RelatorioItem> ranking = relatorioService.itensMaisVendidos(inicio, fim);

        if (ranking.isEmpty()) {
            System.out.println("Nenhuma venda de item registrada no período.");
        } else {
            System.out.println("\n--- Ranking de Itens Mais Vendidos ---");
            int pos = 1;
            for (RelatorioItem item : ranking) {
                System.out.printf("%d. %s\n", pos++, item.getNome());
                System.out.printf("   - Descrição: %s\n", item.getDescricao());
                System.out.printf("   - Preço Unitário: R$ %.2f\n", item.getPrecoVenda());
                System.out.printf("   - Unidades Vendidas: %d\n\n", item.getQuantidadeVendida());
            }
        }
    }

    private void gerarRelatorioItensQueMaisGeramReceita() {
        System.out.println("\n--- Relatório de Itens que Mais Geram Receita ---");
        LocalDate inicio = ValidacaoHelper.lerData(sc, "Digite a data de início (DD/MM/YYYY): ");
        LocalDate fim = ValidacaoHelper.lerData(sc, "Digite a data de fim (DD/MM/YYYY): ");

        if (inicio.isAfter(fim)) {
            System.out.println("Erro: A data de início não pode ser posterior à data de fim.");
            return;
        }

        List<RelatorioItem> ranking = relatorioService.itensQueMaisGeramReceita(inicio, fim);

        if (ranking.isEmpty()) {
            System.out.println("Nenhuma receita gerada no período.");
        } else {
            System.out.println("\n--- Ranking de Itens por Receita Gerada ---");
            int pos = 1;
            for (RelatorioItem item : ranking) {
                System.out.printf("%d. %s\n", pos++, item.getNome());
                System.out.printf("   - Descrição: %s\n", item.getDescricao());
                System.out.printf("   - Preço Unitário: R$ %.2f\n", item.getPrecoVenda());
                System.out.printf("   - Receita Total Gerada: R$ %.2f\n\n", item.getReceitaGerada());
            }
        }
    }

    private void gerarRelatorioEstoqueBaixo() {
        System.out.println("\n--- Relatório de Estoque Baixo ---");
        double nivelMinimo = ValidacaoHelper.lerDouble(sc, "Digite o nível mínimo de estoque para alerta: ");

        List<Produto> produtos = relatorioService.relatorioEstoqueBaixo(nivelMinimo);

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto com estoque baixo encontrado.");
        } else {
            System.out.println("\nProdutos com estoque igual ou inferior a " + nivelMinimo + ":");
            for (Produto p : produtos) {
                System.out.printf("- %s: %.2f %s\n", p.getNome(), p.getQuantidade(), p.getUnidade_medida());
            }
        }
    }
}