package view;

import model.Funcionario;
import model.Mesa;
import repository.jdbc.JdbcMesaRepository;
import service.MesaService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MesaView {

		private final Scanner sc = new Scanner(System.in);
		private final MesaService mesaService = new MesaService(new JdbcMesaRepository());

		public void exibirMenu(){
			while (true) {
				System.out.println("\n=== Gestão de Mesa ===");
				System.out.println("1. Cadastrar Mesa");
				System.out.println("2. Listar Mesa");
				System.out.println("3. Editar Mesa");
				System.out.println("4. Excluir Mesa");
				System.out.println("0. Voltar");

				int opcao = sc.nextInt();
				sc.nextLine();

				switch (opcao) {
					case 1 :
						cadastrar();
						break;
					case 2 :
						listar("listar");
						break;
					case 3 :
						editar();
						break;
					case 4 :
						excluir();
						break;
					case 0 : { return; }
					default : System.out.println("Opção inválida!");
				}
			}
		}

		private void cadastrar() {
			System.out.print("Numero: ");
			int numero = sc.nextInt();

			System.out.print("Capacidade: ");
			int capacidade = sc.nextInt();

			Mesa mesa = new Mesa(0,numero, capacidade);
			mesaService.cadastrarMesa(mesa);
		}

		private void editar()  {
			System.out.println("\n--- Selecione a Mesa para editar ---");
			List<Mesa> mesas = listar("editar");

            if(mesas.isEmpty()) {
                return;
            }

			System.out.println("0 - Cancelar");

			int escolha = -1;
			while (escolha < 0 || escolha > mesas.size()) {
				System.out.println("Escolha uma opção:");
				try {
					escolha = sc.nextInt();
					if (escolha < 0 || escolha > mesas.size()) {
						System.out.println("Opção inválida. Tente novamente!");
					}
				} catch(InputMismatchException e) {
					System.out.println("Entrada inválida. Por favor, digite um número.");
					sc.next();
				}
			}
			sc.nextLine();
			if(escolha == 0) {
				System.out.println("Operação cancelada!");
                return;
			}

			Mesa mesaParaEditar = mesas.get(escolha -1);

			System.out.println("Editando mesa numero: " + mesaParaEditar.getNumero());

			System.out.print("Novo numero: ");
			int numero = sc.nextInt();

			System.out.print("Nova capacidade: ");
			int capacidade = sc.nextInt();

			Mesa mesa = new Mesa(mesaParaEditar.getId_mesa(), numero, capacidade);
			mesaService.editarMesa(mesa);
		}
		private List<Mesa> listar(String metodo)  {
            List<Mesa> mesas = mesaService.listarMesa();

            if (mesas.isEmpty()) {
                System.out.println("Nenhuma mesa disponível para "+ metodo);
                return mesas;
            }

            int cont = 0;
            for (Mesa m : mesas) {
                cont++;
                System.out.println("Mesa {"+cont+"}"+m.toString());
            }

            return mesas;
		}

		private void excluir() {
            System.out.println("\n--- Selecione a Mesa para excluir ---");
            List<Mesa> mesas = listar("excluir");

            if(mesas.isEmpty()) {
                return;
            }

            System.out.println("0 - Cancelar");

            int escolha = -1;
            while (escolha < 0 || escolha > mesas.size()) {
                System.out.println("Escolha uma opção:");
                try {
                    escolha = sc.nextInt();
                    if (escolha < 0 || escolha > mesas.size()) {
                        System.out.println("Opção inválida. Tente novamente!");
                    }
                } catch(InputMismatchException e) {
                    System.out.println("Entrada inválida. Por favor, digite um número.");
                    sc.next();
                }
            }
            sc.nextLine();
            if(escolha == 0) {
                System.out.println("Operação cancelada!");
            }

            Mesa mesaParaExcluir = mesas.get(escolha -1);

            int escolhafinal = -1;
            while(escolhafinal != 1 && escolhafinal != 2) {
                System.out.println("Deseja realmente excluir essa mesa? : " + mesaParaExcluir.getNumero());
                System.out.println("1. Sim");
                System.out.println("2. Não");
                try {
                    escolhafinal = sc.nextInt();
                    if(escolhafinal != 1 && escolhafinal != 2) {
                        System.out.println("Opção inválida. Tente novamente!");
                        sc.next();
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Por favor, digite um número.");
                    sc.next();
                }
            }
            sc.nextLine();
            if (escolha == 2) {
                System.out.println("Operação cancelada!");
                return;
            }
            mesaService.excluirMesa(mesaParaExcluir.getId_mesa());
		}


}


