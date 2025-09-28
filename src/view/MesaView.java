package view;

import util.ValidacaoHelper;
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

				int opcao = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");

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
            int numero;
            while (true) {
                numero = ValidacaoHelper.lerInteiro(sc, "Numero: ");
                if (numero == 0) {
                    System.out.println("Mesa não pode ter numero igual a zero");
                    numero = ValidacaoHelper.lerInteiro(sc, "Numero: ");
                }
                if (mesaService.findByNumero(numero) == null) {
                    break;
                } else {
                    System.out.println("Erro: Já existe uma mesa cadastrada com este número. Tente outro.");
                }
            }

            int capacidade = ValidacaoHelper.lerInteiro(sc, "Capacidade: ");

            Mesa mesa = new Mesa(0, numero, capacidade);
            mesaService.cadastrarMesa(mesa);
		}

		private void editar()  {
            System.out.println("\n--- Selecione a Mesa para editar ---");
            List<Mesa> mesas = listar("editar");

            if (mesas.isEmpty()) {
                return;
            }

            System.out.println("0 - Cancelar");

            int escolha;
            do {
                escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");
                if (escolha < 0 || escolha > mesas.size()) {
                    System.out.println("Opção inválida. Tente novamente!");
                }
            } while (escolha < 0 || escolha > mesas.size());

            if (escolha == 0) {
                System.out.println("Operação cancelada!");
                return;
            }

            Mesa mesaParaEditar = mesas.get(escolha);
            Mesa mesaAtualizada = new Mesa(mesaParaEditar.getId_mesa(), mesaParaEditar.getNumero(), mesaParaEditar.getCapacidade());

            System.out.println("Editando mesa numero: " + mesaParaEditar.getNumero());

            System.out.println("Deseja alterar o número da mesa? (S/N)");
            if (sc.nextLine().equalsIgnoreCase("S")) {
                int novoNumero;
                while (true) {
                    novoNumero = ValidacaoHelper.lerInteiro(sc, "Novo numero: ");
                    Mesa mesaExistente = mesaService.findByNumero(novoNumero);
                    if (mesaExistente == null || mesaExistente.getId_mesa() == mesaParaEditar.getId_mesa()) {
                        mesaAtualizada.setNumero(novoNumero);
                        break;
                    } else {
                        System.out.println("Erro: Já existe uma mesa cadastrada com este número. Tente outro.");
                    }
                }
            }

            System.out.println("Deseja alterar a capacidade da mesa? (S/N)");
            if (sc.nextLine().equalsIgnoreCase("S")) {
                mesaAtualizada.setCapacidade(ValidacaoHelper.lerInteiro(sc, "Nova capacidade: "));
            }

            mesaService.editarMesa(mesaAtualizada);
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

            int escolha;
            do {
                escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");
                if (escolha < 0 || escolha > mesas.size()) {
                    System.out.println("Opção inválida. Tente novamente!");
                }
            } while (escolha < 0 || escolha > mesas.size());

            if (escolha == 0) {
                System.out.println("Operação cancelada!");
                return;
            }

            Mesa mesaParaExcluir = mesas.get(escolha -1);

            System.out.println("Deseja realmente excluir a mesa " + mesaParaExcluir.getNumero() + "?");
            System.out.println("Todas as informações relacionadas com essa mesa serão excluidas.");
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
                mesaService.excluirMesa(mesaParaExcluir.getId_mesa());
            } else {
                System.out.println("Operação cancelada!");
            }
		}


}


