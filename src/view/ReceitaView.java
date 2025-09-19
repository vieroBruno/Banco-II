package view;

import model.Receita;
import repository.jdbc.JdbcReceitaRepository;
import service.ReceitaService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ReceitaView {

	private final Scanner sc = new Scanner(System.in);
	private final ReceitaService receitaService = new ReceitaService(new JdbcReceitaRepository());

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
		System.out.print("ID do Item: ");
		int idItem = sc.nextInt();

		System.out.print("ID do Produto: ");
		int idProduto = sc.nextInt();

		System.out.print("Quantidade Necessária: ");
		double quantidade = sc.nextDouble();

		Receita receita = new Receita(idItem, idProduto, quantidade);
		receitaService.cadastrarReceita(receita);
	}

	private void editar() {
		System.out.println("\n--- Selecione a Receita para editar ---");
		List<Receita> receitas = listar("editar");

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

		Receita receitaParaEditar = receitas.get(escolha - 1);

		System.out.println("Editando dados da Receita: Item ID " + receitaParaEditar.getId_item() + ", Produto ID " + receitaParaEditar.getId_produto());

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

	private List<Receita> listar(String metodo) {
		List<Receita> receitas = receitaService.listarReceita();

		if (receitas.isEmpty()) {
			System.out.println("Nenhuma receita disponível para " + metodo);
			return receitas;
		}

		int cont = 0;
		for (Receita r : receitas) {
			cont++;
			System.out.println("Receita {" + cont + "} : id_item='" + r.getId_item() + '\'' + ", id_produto='" + r.getId_produto() + '\'' + ", quantidade_necessaria='" + r.getQuantidade() + '\'');
		}

		return receitas;
	}

	private void excluir() {
		System.out.println("\n--- Selecione a Receita para excluir ---");
		List<Receita> receitas = listar("excluir");

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
		Receita receitaParaExcluir = receitas.get(escolha - 1);

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
}