package view;

import model.Item;
import repository.jdbc.JdbcItemRepository;
import service.ItemService;
import util.ValidacaoHelper;


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

        double precoVenda = ValidacaoHelper.lerDouble(sc, "Preço de Venda: ");

        String descricao = ValidacaoHelper.isStringValida(sc,"Descrição: ");

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

        int escolha;
        do {
            escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");
            if (escolha < 0 || escolha > items.size()) {
                System.out.println("Opção inválida. Tente novamente!");
            }
        } while (escolha < 0 || escolha > items.size());

        if (escolha == 0) {
            System.out.println("Operação cancelada!");
            return;
        }

        Item itemParaEditar = items.get(escolha - 1);
        Item itemAtualizado = new Item(itemParaEditar.getNome(), itemParaEditar.getPreco_venda(), itemParaEditar.getDescricao());
        itemAtualizado.setId_item(itemParaEditar.getId_item());

        System.out.println("Editando dados de: " + itemParaEditar.getNome());

        System.out.println("Deseja alterar o nome? (S/N)");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            System.out.print("Novo nome: ");
            itemAtualizado.setNome(sc.nextLine());
        }

        System.out.println("Deseja alterar o Preço de Venda? (S/N)");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            itemAtualizado.setPreco_venda(ValidacaoHelper.lerDouble(sc, "Novo Preço de Venda: "));
        }

        System.out.println("Deseja alterar a Descrição? (S/N)");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            System.out.print("Nova Descrição: ");
            itemAtualizado.setDescricao(sc.nextLine());
        }

        itemService.editarItem(itemAtualizado);
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

        int escolha;
        do {
            escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");
            if (escolha < 0 || escolha > items.size()) {
                System.out.println("Opção inválida. Tente novamente!");
            }
        } while (escolha < 0 || escolha > items.size());

        if (escolha == 0) {
            System.out.println("Operação cancelada!");
            return;
        }

        Item itemParaExcluir = items.get(escolha - 1);

        System.out.println("Deseja realmente excluir esse item? : " + itemParaExcluir.getNome());
        System.out.println("Todas as informações relacionadas com esse Item serão excluidas.");
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
            itemService.excluirItem(itemParaExcluir.getId_item());
        } else {
            System.out.println("Operação cancelada!");
        }
	}
}