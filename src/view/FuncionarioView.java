package view;

import model.Funcionario;
import repository.jdbc.Conexao;
import repository.jdbc.JdbcFuncionarioRepository;
import service.FuncionarioService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class FuncionarioView {

    private final Scanner sc = new Scanner(System.in);
    private final FuncionarioService funcionarioService = new FuncionarioService(new JdbcFuncionarioRepository());

    public void exibirMenu() throws SQLException {
        Conexao c = new Conexao();
        Connection con = c.getConnection();
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
                        cadastrar(con);
                    break;
                case 2 :
                        listar(con);
                    break;
                case 3 :
                        editar(con);
                    break;
                case 0 : { return; }
                default : System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrar(Connection con) throws SQLException {

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Cargo: ");
        String cargo = sc.nextLine();

        System.out.print("Telefone: ");
        String telefone = sc.nextLine();

        System.out.print("Salário: ");
        double salario = sc.nextDouble();

        Funcionario funcionario = new Funcionario(0,nome, cargo, salario, telefone);
        funcionarioService.cadastrarFuncionario(funcionario, con);
    }

    private void editar(Connection con) throws SQLException {

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Cargo: ");
        String cargo = sc.nextLine();

        System.out.print("Salário: ");
        double salario = sc.nextDouble();

        System.out.print("Telefone: ");
        String telefone = sc.nextLine();

        System.out.print("Id Funcionario: ");
        int id_funcionario = sc.nextInt();

        Funcionario funcionario = new Funcionario(id_funcionario, nome, cargo, salario, telefone);
        funcionarioService.editarFuncionario(funcionario, con);
    }
    private void listar(Connection con) throws SQLException {
        HashSet all = funcionarioService.listarFuncionario(con);
        Iterator<Funcionario> it = all.iterator();
        int cont = 0;
        while(it.hasNext()){
            cont++;
            System.out.println(it.next().toString());
        }
    }


}
