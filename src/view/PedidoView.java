package view;

import model.Funcionario;
import model.Item;
import model.Pedido;
import model.Mesa;
import repository.jdbc.JdbcMesaRepository;
import repository.jdbc.JdbcPedidoRepository;
import service.PedidoService;
import service.MesaService;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class PedidoView {

	private final Scanner sc = new Scanner(System.in);
	private final PedidoService pedidoService = new PedidoService(new JdbcPedidoRepository());
    private final MesaService mesaService = new MesaService(new JdbcMesaRepository());

	public void exibirMenu() {
		while (true) {
			System.out.println("\n=== Gestão de Pedidos ===");
			System.out.println("1. Cadastrar Pedido");
			System.out.println("2. Listar Pedidos");
			System.out.println("3. Editar Pedido");
			System.out.println("4. Excluir Pedido");
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
        System.out.println("\n--- Novo Pedido ---");

        Mesa mesaSelecionado = selecionarMesa("para adionar um novo pedido");
		if (mesaSelecionado == null) return;

        Funcionario funcionarioSelecionado = selecionarFuncionario();
        int idFuncionario = sc.nextInt();
		sc.nextLine();

		System.out.print("Status: ");
		String status = sc.nextLine();

		Pedido pedido = new Pedido(mesaSelecionado.getId_mesa(), 0, LocalDate.now(), status);
		pedido.setId_funcionario(idFuncionario);
		pedidoService.cadastrarPedido(pedido);
	}

	private void editar() {
		System.out.println("\n--- Selecione o Pedido para editar ---");
		List<Pedido> pedidos = listar("editar");

		if (pedidos.isEmpty()) {
			return;
		}
		System.out.println("0 - Cancelar");

		int escolha = -1;
		while (escolha < 0 || escolha > pedidos.size()) {
			System.out.println("Escolha uma opção:");
			try {
				escolha = sc.nextInt();
				if (escolha < 0 || escolha > pedidos.size()) {
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

		Pedido pedidoParaEditar = pedidos.get(escolha - 1);

		System.out.println("Editando dados do Pedido ID: " + pedidoParaEditar.getId_pedido());

		System.out.print("Novo ID da Mesa: ");
		int idMesa = sc.nextInt();

		System.out.print("Novo ID do Funcionário: ");
		int idFuncionario = sc.nextInt();
		sc.nextLine();

		System.out.print("Novo Status: ");
		String status = sc.nextLine();

		Pedido pedido = new Pedido(idMesa, pedidoParaEditar.getId_pedido(), pedidoParaEditar.getData_pedido(), status);
		pedido.setId_funcionario(idFuncionario);
		pedidoService.editarPedido(pedido);
	}

	private List<Pedido> listar(String metodo) {
		List<Pedido> pedidos = pedidoService.listarPedido();

		if (pedidos.isEmpty()) {
			System.out.println("Nenhum pedido disponível para " + metodo);
			return pedidos;
		}

		int cont = 0;
		for (Pedido p : pedidos) {
			cont++;
			System.out.println("Pedido {" + cont + "} : id_pedido='" + p.getId_pedido() + '\'' + ", id_mesa='" + p.getId_mesa() + '\'' + ", id_funcionario='" + p.getId_funcionario() + '\'' + ", data_pedido='" + p.getData_pedido() + '\'' + ", status='" + p.getStatus() + '\'');
		}

		return pedidos;
	}

	private void excluir() {
		System.out.println("\n--- Selecione o Pedido para excluir ---");
		List<Pedido> pedidos = listar("excluir");

		if (pedidos.isEmpty()) {
			return;
		}

		System.out.println("0 - Cancelar");

		int escolha = -1;
		while (escolha < 0 || escolha > pedidos.size()) {
			System.out.println("Escolha uma opção:");
			try {
				escolha = sc.nextInt();
				if (escolha < 0 || escolha > pedidos.size()) {
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
		Pedido pedidoParaExcluir = pedidos.get(escolha - 1);

		int escolhafinal = -1;
		while (escolhafinal != 1 && escolhafinal != 2) {
			System.out.println("Deseja realmente excluir esse pedido? : " + pedidoParaExcluir.getId_pedido());
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
		pedidoService.excluirPedido(pedidoParaExcluir.getId_pedido());
	}

    private Mesa selecionarMesa(String acao) {
        System.out.println("\n--- Selecione a Mesa " + acao + " ---");
        List<Mesa> mesas = mesaService.listarMesa();

        if (mesas.isEmpty()) {
            System.out.println("Nenhuma mesa cadastrada.");
            return null;
        }

        int cont = 0;
        for (Mesa m : mesas) {
            cont++;
            System.out.println(cont + " - " + m.getNumero());
        }
        System.out.println("0 - Cancelar");

        int escolha = -1;
        while (escolha < 0 || escolha > mesas.size()) {
            System.out.print("Escolha uma opção: ");
            try {
                escolha = sc.nextInt();
                if (escolha < 0 || escolha > mesas.size()) {
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
        return mesas.get(escolha - 1);
    }

    private Funcionario selecionarFuncionario() {
        System.out.println("\n--- Selecione o Funcionario ---");
        List<Mesa> mesas = mesaService.listarMesa();

        if (mesas.isEmpty()) {
            System.out.println("Nenhuma mesa cadastrada.");
            return null;
        }

        int cont = 0;
        for (Mesa m : mesas) {
            cont++;
            System.out.println(cont + " - " + m.getNumero());
        }
        System.out.println("0 - Cancelar");

        int escolha = -1;
        while (escolha < 0 || escolha > mesas.size()) {
            System.out.print("Escolha uma opção: ");
            try {
                escolha = sc.nextInt();
                if (escolha < 0 || escolha > mesas.size()) {
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
        return mesas.get(escolha - 1);
    }
}