package view;

import model.Item;
import repository.jdbc.JdbcItemRepository;
import service.ItemService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ItemView {

	private final Scanner sc = new Scanner(System.in);
	private final ItemService itemService = new ItemService(new JdbcItemRepository());

	public void exibirMenu() {
		while (true) {
			System.out.println("\n=== Gestão de Itens ===");
			System.out.println("1. Cadastrar Item");
			System.out.println("2. Listar Itens");
			System.out.println("3. Editar Item");
			System.out.println("4. Excluir Item");
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

		System.out.print("Preço de Venda: ");
		double precoVenda = sc.nextDouble();
		sc.nextLine();

		System.out.print("Descrição: ");
		String descricao = sc.nextLine();

		Item item = new Item(nome, precoVenda, descricao);
		itemService.cadastrarItem(item);
	}

	private void editar() {
		System.out.println("\n--- Selecione o Item para editar ---");
		List<Item> items = listar("editar");

		if (items.isEmpty()) {
			return;
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
		sc.nextLine();
		if (escolha == 0) {
			System.out.println("Operação cancelada!");
			return;
		}

		Item itemParaEditar = items.get(escolha - 1);

		System.out.println("Editando dados de: " + itemParaEditar.getNome());

		System.out.print("Novo nome: ");
		String nome = sc.nextLine();

		System.out.print("Novo Preço de Venda: ");
		double precoVenda = sc.nextDouble();
		sc.nextLine();

		System.out.print("Nova Descrição: ");
		String descricao = sc.nextLine();

		Item item = new Item(nome, precoVenda, descricao);
		item.setId_item(itemParaEditar.getId_item());
		itemService.editarItem(item);
	}

	private List<Item> listar(String metodo) {
		List<Item> items = itemService.listarItem();

		if (items.isEmpty()) {
			System.out.println("Nenhum item disponível para " + metodo);
			return items;
		}

		int cont = 0;
		for (Item i : items) {
			cont++;
			System.out.println("Item {" + cont + "} : nome='" + i.getNome() + '\'' + ", preco_venda='" + i.getPreco_venda() + '\'' + ", descricao='" + i.getDescricao() + '\'');
		}

		return items;
	}

	private void excluir() {
		System.out.println("\n--- Selecione o Item para excluir ---");
		List<Item> items = listar("excluir");

		if (items.isEmpty()) {
			return;
		}

		System.out.println("0 - Cancelar");

		int escolha = -1;
		while (escolha < 0 || escolha > items.size()) {
			System.out.println("Escolha uma opção:");
			try {
				escolha = sc.nextInt();
				if (escolha < 0 || escolha > items.size()) {
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
		Item itemParaExcluir = items.get(escolha - 1);

		int escolhafinal = -1;
		while (escolhafinal != 1 && escolhafinal != 2) {
			System.out.println("Deseja realmente excluir esse item? : " + itemParaExcluir.getNome()+ " Todas as informações relacionadas " +
            "com esse Item serão excluidas");
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
		itemService.excluirItem(itemParaExcluir.getId_item());
	}
}