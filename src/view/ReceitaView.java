package view;

import model.Item;
import model.Produto;
import model.Receita;
import repository.jdbc.JdbcItemRepository;
import repository.jdbc.JdbcProdutoRepository;
import repository.jdbc.JdbcReceitaRepository;
import service.ReceitaService;
import service.ItemService;
import service.ProdutoService;



import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ReceitaView {

	private final Scanner sc = new Scanner(System.in);
	private final ReceitaService receitaService = new ReceitaService(new JdbcReceitaRepository());
    private final ItemService itemService = new ItemService(new JdbcItemRepository());
    private final ProdutoService produtoService = new ProdutoService(new JdbcProdutoRepository());

    public void exibirMenu() {
		while (true) {
			System.out.println("\n=== Gestão de Receitas ===");
			System.out.println("1. Cadastrar Receita");
			System.out.println("2. Listar Receitas");
			System.out.println("3. Editar Receita");
			System.out.println("4. Excluir Receita");
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
		int idItem = listarItem("cadastrar");

        if (idItem == 0) {
           return;
        }

        Produto produtoSelecionado = listarProduto();

        if (produtoSelecionado == null) {
            return;
        }

        String unidadeMedida = produtoSelecionado.getUnidade_medida();
        System.out.print("Quantos(as) "+unidadeMedida+":");
		double quantidade = sc.nextDouble();

		Receita receita = new Receita(idItem, produtoSelecionado.getId_produto(), quantidade);
		receitaService.cadastrarReceita(receita);
	}

	private void editar() {
		System.out.println("\n--- Selecione a Receita para editar ---");
		List<Produto> receitas = listar("editar");

		if (receitas.isEmpty()) {
			return;
		}
		System.out.println("0 - Cancelar");

		int escolha = -1;
		while (escolha < 0 || escolha > receitas.size()) {
			System.out.println("Escolha uma opção:");
			try {
				escolha = sc.nextInt();
				if (escolha < 0 || escolha > receitas.size()) {
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

		//Receita receitaParaEditar = receitas.get(escolha - 1);

		//System.out.println("Editando dados da Receita: Item ID " + receitaParaEditar.getId_item() + ", Produto ID " + receitaParaEditar.getId_produto());

		System.out.print("Novo ID do Item: ");
		int idItem = sc.nextInt();

		System.out.print("Novo ID do Produto: ");
		int idProduto = sc.nextInt();

		System.out.print("Nova Quantidade Necessária: ");
		double quantidade = sc.nextDouble();

		Receita receita = new Receita(idItem, idProduto, quantidade);
		// receita.setId_receita(receitaParaEditar.getId_receita()); // Model não possui Id_receita
		receitaService.editarReceita(receita);
	}

	private List<Produto> listar(String metodo) {
        System.out.println("Qual item você gostaria de ver a receita? ");
        int id_item = listarItem("listar");

        List<Produto> produtos = receitaService.listarReceita(id_item);

		if (produtos.isEmpty()) {
			System.out.println("Nenhuma receita disponível para " + metodo);
			return produtos;
		}


		int cont = 0;
		for (Produto p : produtos) {
			cont++;
			System.out.println("Produto {" + cont + "} : " +p.toString());
		}

		return produtos;
	}

	private void excluir() {
		System.out.println("\n--- Selecione a Receita para excluir ---");
		List<Produto> receitas = listar("excluir");

		if (receitas.isEmpty()) {
			return;
		}

		System.out.println("0 - Cancelar");

		int escolha = -1;
		while (escolha < 0 || escolha > receitas.size()) {
			System.out.println("Escolha uma opção:");
			try {
				escolha = sc.nextInt();
				if (escolha < 0 || escolha > receitas.size()) {
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
		//Item receitaParaExcluir = receitas.get(escolha - 1);

		int escolhafinal = -1;
		while (escolhafinal != 1 && escolhafinal != 2) {
			System.out.println("Deseja realmente excluir essa receita?");
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
		// A exclusão precisaria de um ID, o modelo não possui.
		// receitaService.excluirReceita(receitaParaExcluir.getId_receita());
		System.out.println("Funcionalidade de exclusão de receita a ser implementada.");

	}

    private int listarItem(String metodo) {
        List<Item> items = itemService.listarItem();

        if(Objects.equals(metodo, "cadastrar")){
            System.out.println("\n--- Selecione o Item para incluir uma receita ---");
        }

        if (items.isEmpty()) {
            System.out.println("Nenhum item disponível");
            return 0;
        }

        int cont = 0;
        for (Item i : items) {
            cont++;
            System.out.println("Item {" + cont + "} : nome='" + i.getNome() + '\'' + ", preco_venda='" + i.getPreco_venda() + '\'' + ", descricao='" + i.getDescricao() + '\'');
        }

        System.out.println("0 - Cancelar");
        int escolha = -1;

        while (escolha < 0 || escolha > items.size()) {
            System.out.println("Escolha uma opção:");
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

        if (escolha == 0) {
            System.out.println("Operação cancelada!");
            return 0;
        }

        Item itemParaIncluir = items.get(escolha - 1);

        return itemParaIncluir.getId_item();
    }

    private Produto listarProduto() {
        List<Produto> produtos = produtoService.listarProduto();
        System.out.println("\n--- Selecione o Produto para incluir uma receita ---");

        if (produtos.isEmpty()) {
            System.out.println("Nenhum item disponível");
            return null;
        }

        int cont = 0;
        for (Produto p : produtos) {
            cont++;
            System.out.println("Produto {"+cont+"}"+p.toString());
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

        if (escolha == 0) {
            System.out.println("Operação cancelada!");
            return null;
        }

        return produtos.get(escolha - 1);
    }
}