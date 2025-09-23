package view;

import model.Produto;
import repository.jdbc.JdbcProdutoRepository;
import service.ProdutoService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ProdutoView {

    private final Scanner sc = new Scanner(System.in);
    private final ProdutoService produtoService = new ProdutoService(new JdbcProdutoRepository());

    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Gestão de Produtos ===");
            System.out.println("1. Cadastrar Produto");
            System.out.println("2. Listar Produtos");
            System.out.println("3. Editar Produto");
            System.out.println("4. Excluir Produto");
            System.out.println("0. Voltar");

            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    cadastrar();
                    break;
                case 2:
                    listar("listar");
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
        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Unidade de Medida: ");
        String unidadeMedida = sc.nextLine();

        System.out.print("Quantidade: ");
        double quantidade = sc.nextDouble();

        Produto produto = new Produto(nome, unidadeMedida, quantidade);
        produtoService.cadastrarProduto(produto);
    }

    private void editar() {
        System.out.println("\n--- Selecione o Produto para editar ---");
        List<Produto> produtos = listar("editar");

        if (produtos.isEmpty()) {
            return;
        }
        System.out.println("0 - Cancelar");

        int escolha = -1;
        while (escolha < 0 || escolha > produtos.size()) {
            System.out.println("Escolha uma opção:");
            try {
                escolha = sc.nextInt();
                if (escolha < 0 || escolha > produtos.size()) {
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
            return;
        }

        Produto produtoParaEditar = produtos.get(escolha - 1);

        System.out.println("Editando dados de: " + produtoParaEditar.getNome());

        System.out.print("Novo nome: ");
        String nome = sc.nextLine();

        System.out.print("Nova unidade de medida: ");
        String unidadeMedida = sc.nextLine();

        System.out.print("Nova quantidade: ");
        double quantidade = sc.nextDouble();


        Produto produto = new Produto(nome, unidadeMedida, quantidade);
        produto.setId_produto(produtoParaEditar.getId_produto());
        produtoService.editarProduto(produto);
    }

    private List<Produto> listar(String metodo) {
        List<Produto> produtos = produtoService.listarProduto();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto disponível para " + metodo);
            return produtos;
        }

        int cont = 0;
        for (Produto p : produtos) {
            cont++;
            System.out.println("Produto {"+cont+"}"+p.toString());
        }

        return produtos;
    }

    private void excluir() {
        System.out.println("\n--- Selecione o Produto para excluir ---");
        List<Produto> produtos = listar("excluir");

        if (produtos.isEmpty()) {
            return;
        }

        System.out.println("0 - Cancelar");

        int escolha = -1;
        while (escolha < 0 || escolha > produtos.size()) {
            System.out.println("Escolha uma opção:");
            try {
                escolha = sc.nextInt();
                if (escolha < 0 || escolha > produtos.size()) {
                    System.out.println("Opção inválida. Tente novamente!");
                    sc.next();
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                sc.next();
            }
        }
        sc.nextLine();
        if (escolha == 0) {
            System.out.println("Operação cancelada!");
            return;
        }
        Produto produtoParaExcluir = produtos.get(escolha - 1);

        int escolhafinal = -1;
        while (escolhafinal != 1 && escolhafinal != 2) {
            System.out.println("Deseja realmente excluir esse produto? : " + produtoParaExcluir.getNome());
            System.out.println("1. Sim");
            System.out.println("2. Não");
            try {
                escolhafinal = sc.nextInt();
                if (escolhafinal != 1 && escolhafinal != 2) {
                    System.out.println("Opção inválida. Tente novamente!");
                    sc.next();
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                sc.next();
            }
        }
        sc.nextLine();
        if (escolhafinal == 2) {
            System.out.println("Operação cancelada!");
            return;
        }
        produtoService.excluirProduto(produtoParaExcluir.getId_produto());
    }
}