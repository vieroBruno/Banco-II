package view;

import model.Item;
import model.Produto;
import model.Receita;
import repository.jdbc.JdbcItemRepository;
import repository.jdbc.JdbcProdutoRepository;
import repository.jdbc.JdbcReceitaRepository;
import service.ItemService;
import service.ProdutoService;
import service.ReceitaService;
import util.ValidacaoHelper;

import java.util.List;
import java.util.Scanner;

public class ReceitaView {

    private final Scanner sc = new Scanner(System.in);
    private final ReceitaService receitaService = new ReceitaService(new JdbcReceitaRepository());
    private final ProdutoService produtoService = new ProdutoService(new JdbcProdutoRepository());
    private final ItemService itemService = new ItemService(new JdbcItemRepository());

    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Gestão de Receitas ===");
            System.out.println("1. Inserir produtos a uma Receita");
            System.out.println("2. Listar produtos de uma Receita");
            System.out.println("3. Editar produtos de uma Receita");
            System.out.println("4. Excluir produtos de uma Receita");
            System.out.println("0. Voltar");

            int opcao = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    cadastrar();
                    break;
                case 2:
                    listarPorItem();
                    break;
                case 3:
                    editar();
                    break;
                case 4:
                    excluir();
                    break;
                case 0: {
                    return;
                }
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrar() {
        System.out.println("\n--- Adicionar Produto a uma Receita ---");

        Item itemSelecionado = selecionarItem("para adicionar um produto");
        if (itemSelecionado == null) return;

        Produto produtoSelecionado = selecionarProduto();
        if (produtoSelecionado == null) return;

        if (receitaService.produtoJaExisteNaReceita(itemSelecionado.getId_item(), produtoSelecionado.getId_produto())) {
            System.out.println("Erro: Este produto já está presente nesta receita.");
            return;
        }

        String unidadeMedida = produtoSelecionado.getUnidade_medida();
        System.out.print("Quantidade Necessária (em " + unidadeMedida + "): ");
        double quantidade = ValidacaoHelper.lerDouble(sc, "Quantidade Necessária (em " + unidadeMedida + "): ");

        Receita receita = new Receita(itemSelecionado.getId_item(), produtoSelecionado.getId_produto(), quantidade);
        receitaService.cadastrarReceita(receita);
    }

    private void editar() {
        System.out.println("\n--- Editar produtos de uma receita  ---");

        Item itemSelecionado = selecionarItem("para editar a receita");
        if (itemSelecionado == null) return;

        Produto produtoParaEditar = selecionarProdutosPorReceita(itemSelecionado);
        if (produtoParaEditar == null) return;

        double novaQuantidade = ValidacaoHelper.lerDouble(sc, "Nova Quantidade (em " + produtoParaEditar.getUnidade_medida() + "): ");

        Receita receitaAtualizada = new Receita(itemSelecionado.getId_item(), produtoParaEditar.getId_produto(), novaQuantidade);
        receitaService.editarReceita(receitaAtualizada);
    }

    private void excluir() {
        System.out.println("\n--- Excluir Receita ---");

        Item itemSelecionado = selecionarItem("para excluir um produto");
        if (itemSelecionado == null) return;

        Produto produtoParaExcluir = selecionarProdutosPorReceita(itemSelecionado);
        if (produtoParaExcluir == null) return;

        System.out.println("Deseja realmente remover '" + produtoParaExcluir.getNome() + "' da receita de '" + itemSelecionado.getNome() + "'?");
        System.out.println("1. Sim");
        System.out.println("2. Não");

        int confirmacao;
        do {
            confirmacao = ValidacaoHelper.lerInteiro(sc, "Confirme: ");
            if (confirmacao != 1 && confirmacao != 2 ){
                System.out.println("Opção inválida tente novamente");
            }
        } while (confirmacao != 1 && confirmacao != 2 );

        if (confirmacao == 1) {
            receitaService.excluirReceita(itemSelecionado.getId_item(), produtoParaExcluir.getId_produto());
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    private void listarPorItem() {
        Item itemSelecionado = selecionarItem("para ver a receita");
        if (itemSelecionado == null) return;

        System.out.println("\n--- Receita para o item: " + itemSelecionado.getNome() + " ---");
        List<Produto> produtos = receitaService.listarReceita(itemSelecionado.getId_item());

        if (produtos.isEmpty()) {
            System.out.println("Este item ainda não possui produtos cadastrados.");
        } else {
            for (Produto produto : produtos) {
                System.out.printf("- %s: %.2f %s\n",
                        produto.getNome(),
                        produto.getQuantidade(),
                        produto.getUnidade_medida());
            }
        }
    }

    private Item selecionarItem(String acao) {
        System.out.println("\n--- Selecione o Item " + acao + " ---");
        List<Item> items = itemService.listarItem();

        if (items.isEmpty()) {
            System.out.println("Nenhum item cadastrado.");
            return null;
        }

        int cont = 0;
        for (Item i : items) {
            cont++;
            System.out.println(cont + " - " + i.getNome());
        }
        System.out.println("0 - Cancelar");

        int escolha;
        do {
            escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");
            if (escolha < 0 || escolha > items.size()) {
                System.out.println("Opção inválida. Tente novamente!");
            }
        } while (escolha < 0 || escolha > items.size());

        if (escolha == 0) {
            System.out.println("Operação cancelada!");
            return null;
        }
        return items.get(escolha - 1);
    }

    private Produto selecionarProdutosPorReceita(Item item) {
        System.out.println("\n--- Selecione o Produto ---");
        List<Produto> produtos = receitaService.listarReceita(item.getId_item());

        if (produtos.isEmpty()) {
            System.out.println("Este item não possui produtos.");
            return null;
        }

        int cont = 0;
        for (Produto p : produtos) {
            cont++;
            System.out.printf("%d - %s (%.2f %s)\n", cont, p.getNome(), (double) p.getQuantidade(), p.getUnidade_medida());
        }
        System.out.println("0 - Cancelar");

        int escolha;
        do {
            escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");
            if (escolha < 0 || escolha > produtos.size()) {
                System.out.println("Opção inválida. Tente novamente!");
            }
        } while (escolha < 0 || escolha > produtos.size());

        if (escolha == 0) {
            System.out.println("Operação cancelada.");
            return null;
        }
        return produtos.get(escolha - 1);
    }

    private Produto selecionarProduto() {
        System.out.println("\n--- Selecione o Produto  para Adicionar ---");
        List<Produto> produtos = produtoService.listarProduto();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto disponível para selecionar.");
            return null;
        }

        int cont = 0;
        for (Produto p : produtos) {
            cont++;
            System.out.println(cont + " - " + p.getNome() + " (" + p.getUnidade_medida() + ")");
        }
        System.out.println("0 - Cancelar");

        int escolha;
        do {
            escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");
            if (escolha < 0 || escolha > produtos.size()) {
                System.out.println("Opção inválida. Tente novamente!");
            }
        } while (escolha < 0 || escolha > produtos.size());

        if (escolha == 0) {
            System.out.println("Operação cancelada!");
            return null;
        }
        return produtos.get(escolha - 1);
    }
}