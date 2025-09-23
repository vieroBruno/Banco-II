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

import java.util.InputMismatchException;
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
            System.out.println("1. Adicionar Ingrediente a um Item");
            System.out.println("2. Listar Ingredientes de um Item");
            System.out.println("3. Editar Quantidade de um Ingrediente");
            System.out.println("4. Excluir Ingrediente de um Item");
            System.out.println("0. Voltar");

            int opcao;
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();


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
        System.out.println("\n--- Adicionar Ingrediente a uma Receita ---");

        Item itemSelecionado = selecionarItem("para adicionar um ingrediente");
        if (itemSelecionado == null) return;

        Produto produtoSelecionado = selecionarProduto();
        if (produtoSelecionado == null) return;

        String unidadeMedida = produtoSelecionado.getUnidade_medida();
        System.out.print("Quantidade Necessária (em " + unidadeMedida + "): ");
        double quantidade = sc.nextDouble();
        sc.nextLine();

        Receita receita = new Receita(itemSelecionado.getId_item(), produtoSelecionado.getId_produto(), quantidade);
        receitaService.cadastrarReceita(receita);
    }

    private void editar() {
        System.out.println("\n--- Editar Quantidade de um Ingrediente ---");

        Item itemSelecionado = selecionarItem("para editar a receita");
        if (itemSelecionado == null) return;

        Produto ingredienteParaEditar = selecionarIngrediente(itemSelecionado);
        if (ingredienteParaEditar == null) return;

        System.out.print("Nova Quantidade (em " + ingredienteParaEditar.getUnidade_medida() + "): ");
        double novaQuantidade = sc.nextDouble();
        sc.nextLine();

        Receita receitaAtualizada = new Receita(itemSelecionado.getId_item(), ingredienteParaEditar.getId_produto(), novaQuantidade);
        receitaService.editarReceita(receitaAtualizada);
    }

    private void excluir() {
        System.out.println("\n--- Excluir Ingrediente de uma Receita ---");

        Item itemSelecionado = selecionarItem("para excluir um ingrediente");
        if (itemSelecionado == null) return;

        Produto ingredienteParaExcluir = selecionarIngrediente(itemSelecionado);
        if (ingredienteParaExcluir == null) return;

        System.out.println("Deseja realmente remover '" + ingredienteParaExcluir.getNome() + "' da receita de '" + itemSelecionado.getNome() + "'?");
        System.out.println("1. Sim");
        System.out.println("2. Não");
        int confirmacao = sc.nextInt();
        sc.nextLine();

        if (confirmacao == 1) {
            receitaService.excluirReceita(itemSelecionado.getId_item(), ingredienteParaExcluir.getId_produto());
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    private void listarPorItem() {
        Item itemSelecionado = selecionarItem("para ver a receita");
        if (itemSelecionado == null) return;

        System.out.println("\n--- Receita para o item: " + itemSelecionado.getNome() + " ---");
        List<Produto> ingredientes = receitaService.listarReceita(itemSelecionado.getId_item());

        if (ingredientes.isEmpty()) {
            System.out.println("Este item ainda não possui ingredientes cadastrados.");
        } else {
            for (Produto ingrediente : ingredientes) {
                System.out.printf("- %s: %.2f %s\n",
                        ingrediente.getNome(),
                        (double) ingrediente.getQuantidade(),
                        ingrediente.getUnidade_medida());
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

        int escolha = -1;
        while (escolha < 0 || escolha > items.size()) {
            System.out.print("Escolha uma opção: ");
            try {
                escolha = sc.nextInt();
                if (escolha < 0 || escolha > items.size()) {
                    System.out.println("Opção inválida. Tente novamente!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                sc.next();
            }
        }
        sc.nextLine();

        if (escolha == 0) {
            System.out.println("Operação cancelada!");
            return null;
        }
        return items.get(escolha - 1);
    }

    private Produto selecionarIngrediente(Item item) {
        System.out.println("\n--- Selecione o Ingrediente ---");
        List<Produto> ingredientes = receitaService.listarReceita(item.getId_item());

        if (ingredientes.isEmpty()) {
            System.out.println("Este item não possui ingredientes.");
            return null;
        }

        int cont = 0;
        for (Produto p : ingredientes) {
            cont++;
            System.out.printf("%d - %s (%.2f %s)\n", cont, p.getNome(), (double) p.getQuantidade(), p.getUnidade_medida());
        }
        System.out.println("0 - Cancelar");

        int escolha = -1;
        while (escolha < 0 || escolha > ingredientes.size()) {
            System.out.print("Escolha uma opção: ");
            try {
                escolha = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida.");
                sc.next();
            }
        }
        sc.nextLine();

        if (escolha == 0) {
            System.out.println("Operação cancelada.");
            return null;
        }
        return ingredientes.get(escolha - 1);
    }

    private Produto selecionarProduto() {
        System.out.println("\n--- Selecione o Produto (Ingrediente) para Adicionar ---");
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

        int escolha = -1;
        while (escolha < 0 || escolha > produtos.size()) {
            System.out.print("Escolha uma opção: ");
            try {
                escolha = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida.");
                sc.next();
            }
        }
        sc.nextLine();

        if (escolha == 0) {
            System.out.println("Operação cancelada!");
            return null;
        }
        return produtos.get(escolha - 1);
    }
}