package view;

import model.Funcionario;
import repository.jdbc.JdbcFuncionarioRepository;
import service.FuncionarioService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MesaView {

		private final Scanner sc = new Scanner(System.in);
		private final FuncionarioService funcionarioService = new FuncionarioService(new JdbcFuncionarioRepository());

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

		}

		private void editar()  {

		}
		private List<Funcionario> listar(String metodo)  {
			return null;
		}

		private void excluir() {

		}


}


