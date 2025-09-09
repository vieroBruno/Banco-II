package view;

import service.FuncionarioService;

import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    private final Scanner sc = new Scanner(System.in);

    public void start() throws SQLException {
        int opcao;

        do {
            System.out.println("===================================");
            System.out.println("     SISTEMA PARA RESTAURANTE");
            System.out.println("===================================");
            System.out.println("1 - Funcionários");
            System.out.println("2 - Fornecedores");
            System.out.println("3 - Produtos / Estoque");
            System.out.println("4 - Compras");
            System.out.println("5 - Cardápio");
            System.out.println("6 - Itens do Cardápio");
            System.out.println("7 - Mesas");
            System.out.println("8 - Pedidos");
            System.out.println("9 - Reservas");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    new FuncionarioView().exibirMenu();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }

            System.out.println();

        } while (opcao != 0);
    }

    public static void main(String[] args) throws SQLException {
        new Menu().start();
    }

    private void mostrarMenuCrud(String MenuOption) throws SQLException {
        int option;
        do {
        System.out.println("  Escolha uma opção:  ");
        System.out.println("1 - Cadastrar " + MenuOption );
        System.out.println("2 - Listar "  + MenuOption);
        System.out.println("3 - Alterar " + MenuOption);
        System.out.println("4 - Deletar " + MenuOption);
        System.out.println("0 - Voltar " + MenuOption);
        option = sc.nextInt();
        sc.nextLine();

        switch (option) {
            case 1:

                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 0:
                new Menu().start();
                break;
        }
        } while ( option != 0);
    }

}
