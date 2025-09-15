package view;

import model.Funcionario;
import repository.jdbc.JdbcFuncionarioRepository;
import service.FuncionarioService;

import java.util.*;

public class FuncionarioView {

    private final Scanner sc = new Scanner(System.in);
    private final FuncionarioService funcionarioService = new FuncionarioService(new JdbcFuncionarioRepository());

    public void exibirMenu(){
        while (true) {
            System.out.println("\n=== Gestão de Funcionários ===");
            System.out.println("1. Cadastrar Funcionário");
            System.out.println("2. Listar Funcionários");
            System.out.println("3. Editar Funcionários");
            System.out.println("4. Excluir Funcionários");
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
                case 0 : { return; }
                default : System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrar() {

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Cargo: ");
        String cargo = sc.nextLine();

        System.out.print("Telefone: ");
        String telefone = sc.nextLine();

        System.out.print("Salário: ");
        double salario = sc.nextDouble();

        Funcionario funcionario = new Funcionario(0,nome, cargo, salario, telefone);
        funcionarioService.cadastrarFuncionario(funcionario);
    }

    private void editar()  {
        System.out.println("\n--- Selecione o Funcionário para editar ---");
        List<Funcionario> funcionarios = listar("editar");
        System.out.println("0 - Cancelar");

        int escolha = -1;
        while (escolha < 0 || escolha > funcionarios.size()) {
            System.out.println("Escolha uma opção:");
            try {
                escolha = sc.nextInt();
                if (escolha < 0 || escolha > funcionarios.size()) {
                    System.out.println("Opção inválida. Tente novamente!");
                }
            } catch(InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                sc.next();
            }
        }
        sc.nextLine();
        if (escolha == 0) {
            System.out.println("Operação cancelada!");
            return;
        }

        Funcionario funcionarioParaEditar = funcionarios.get(escolha - 1);

        System.out.println("Editando dados de: " + funcionarioParaEditar.getNome());

        System.out.print("Novo nome: ");
        String nome = sc.nextLine();

        System.out.print("Novo cargo: ");
        String cargo = sc.nextLine();

        System.out.print("Novo salário: ");
        double salario = sc.nextDouble();
        sc.nextLine();

        System.out.print("Telefone: ");
        String telefone = sc.nextLine();

        Funcionario funcionario = new Funcionario(funcionarioParaEditar.getIdFuncionario(), nome, cargo, salario, telefone);
        funcionarioService.editarFuncionario(funcionario);
    }
    private List<Funcionario> listar(String metodo)  {
        List<Funcionario> funcionarios = funcionarioService.listarFuncionario();

        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário disponível para "+ metodo);
            return funcionarios;
        }

        int cont = 0;
        for (Funcionario f : funcionarios) {
            cont++;
            System.out.println("Funcionario {"+cont+"}"+f.toString());
        }

        return funcionarios;

    }


}
