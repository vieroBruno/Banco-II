package view;


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
            System.out.println("2 - Pedidos");
            System.out.println("3 - Mesas");
            System.out.println("4 - Item");
            System.out.println("5 - Receitas");
            System.out.println("6 - Produtos");
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
                    new MesaView().exibirMenu();
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }


        } while (opcao != 0);
    }

    public static void main(String[] args) throws SQLException {
        new Menu().start();
    }

}
