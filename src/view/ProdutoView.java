package view;

import model.Produto;
import repository.jdbc.JdbcProdutoRepository;
import service.ProdutoService;
import util.ValidacaoHelper;

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

            int opcao = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");

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
        String nome = ValidacaoHelper.isStringValida(sc, "Nome: ");

        String unidadeMedida = ValidacaoHelper.lerUnidadeMedidaValida(sc, "Unidade de Medida (Quilogramas, Gramas, Litros, Mililitros, Unidades): ");

        double quantidade = ValidacaoHelper.lerDouble(sc, "Quantidade: ");

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

        int escolha;
        do {
            escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");
            if (escolha < 0 || escolha > produtos.size()) {
                System.out.println("Opção inválida. Tente novamente!");
            }
        } while (escolha < 0 || escolha > produtos.size());

        if (escolha == 0) {
            System.out.println("Operação cancelada!");
            return;
        }

        Produto produtoParaEditar = produtos.get(escolha - 1);
        Produto produtoAtualizado = new Produto(produtoParaEditar.getNome(), produtoParaEditar.getUnidade_medida(), produtoParaEditar.getQuantidade());
        produtoAtualizado.setId_produto(produtoParaEditar.getId_produto());

        System.out.println("Editando dados de: " + produtoParaEditar.getNome());

        System.out.println("Deseja alterar o nome? (S/N)");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            System.out.print("Novo nome: ");
            produtoAtualizado.setNome(sc.nextLine());
        }

        System.out.println("Deseja alterar a unidade de medida? (S/N)");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            produtoAtualizado.setUnidade_medida(ValidacaoHelper.lerUnidadeMedidaValida(sc, "Nova unidade de medida: "));
        }

        System.out.println("Deseja alterar a quantidade? (S/N)");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            produtoAtualizado.setQuantidade(ValidacaoHelper.lerDouble(sc, "Nova quantidade: "));
        }

        produtoService.editarProduto(produtoAtualizado);
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

        int escolha;
        do {
            escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");
            if (escolha < 0 || escolha > produtos.size()) {
                System.out.println("Opção inválida. Tente novamente!");
            }
        } while (escolha < 0 || escolha > produtos.size());

        if (escolha == 0) {
            System.out.println("Operação cancelada!");
            return;
        }

        Produto produtoParaExcluir = produtos.get(escolha - 1);

        System.out.println("Deseja realmente excluir esse produto? : " + produtoParaExcluir.getNome());
        System.out.println("Todas as informações relacionadas com esse Produto serão excluidas.");
        System.out.println("1. Sim");
        System.out.println("2. Não");

        int escolhafinal;
        do {
             escolhafinal = ValidacaoHelper.lerInteiro(sc, "Confirme: ");
             if (escolhafinal != 1 && escolhafinal != 2 ){
                 System.out.println("Opção inválida tente novamente");
             }
        } while (escolhafinal != 1 && escolhafinal != 2 );


        if (escolhafinal == 1) {
            produtoService.excluirProduto(produtoParaExcluir.getId_produto());
        } else {
            System.out.println("Operação cancelada!");
        }
    }
}