package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public final class ValidacaoHelper {

    private ValidacaoHelper() {}

    public static String isStringValida(Scanner sc, String mensagem) {
        boolean valido = false;
        System.out.print(mensagem);
        String texto = sc.nextLine();

        while(!valido) {
            if (texto != null && !texto.trim().isEmpty()) {
                valido = true;
            } else {
                System.out.println("Esse campo não pode ser nulo. Tente Novamente");
                System.out.print(mensagem);
                texto = sc.nextLine();
            }

        }
        return texto;
    }

    public static double lerDouble(Scanner sc, String mensagem) {
        double valor = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensagem);
            String input = sc.nextLine();

            try {
                String inputFormatado = input.replace(',', '.');

                valor = Double.parseDouble(inputFormatado);

                if (valor > 0) {
                    valido = true;
                } else {
                    System.out.println("Erro: Por favor, digite um número positivo maior que zero.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Entrada inválida. Por favor, digite um número válido (ex: 1500.50 ou 0,5).");
            }
        }
        return valor;
    }


    public static String formatarTelefone(String telefone) {

        String digitos = telefone.replaceAll("\\D", "");

        if (digitos.length() == 11) {
            return String.format("(%s) %s-%s",
                    digitos.substring(0, 2),
                    digitos.substring(2, 7),
                    digitos.substring(7));
        } else if (digitos.length() == 10) {
            return String.format("(%s) %s-%s",
                    digitos.substring(0, 2),
                    digitos.substring(2, 6),
                    digitos.substring(6));
        } else {
            return telefone;
        }
    }

    public static String lerTelefone(Scanner sc, String mensagem) {
        String telefone = "";
        boolean valido = false;
        while (!valido) {
            System.out.print(mensagem);
            String input = sc.nextLine();

            String digitos = input.replaceAll("\\D", "");

            if (digitos.length() == 10 || digitos.length() == 11) {
                telefone = digitos;
                valido = true;
            } else {
                System.out.println("Erro: O telefone deve conter 10 ou 11 dígitos (incluindo DDD). Tente novamente.");
            }
        }
        return telefone;
    }

    public static int lerInteiro(Scanner sc, String mensagem) {
        int valor = -1;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensagem);
            try {
                valor = sc.nextInt();
                if (valor >= 0) {
                    valido = true;
                } else {
                    System.out.println("Erro: Por favor, digite um número positivo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erro: Por favor, digite um número inteiro válido.");
                sc.next();
            }
        }
        sc.nextLine();
        return valor;
    }

    public static LocalDate lerData(Scanner sc, String mensagem) {
        LocalDate data = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (data == null) {
            System.out.print(mensagem);
            String input = sc.nextLine();
            try {
                data = LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Erro: Formato de data inválido. Use o formato DD/MM/YYYY.");
            }
        }
        return data;
    }

    public static String lerUnidadeMedidaValida(Scanner sc, String mensagem) {
        String unidade = "";
        boolean valido = false;
        List<String> unidadesValidas = Arrays.asList("Quilogramas", "Litros", "Mililitros", "Gramas", "Unidades");

        while (!valido) {
            System.out.print(mensagem);
            unidade = sc.nextLine();
            String unidadeFormatada = unidade.substring(0, 1).toUpperCase() + unidade.substring(1).toLowerCase();

            if (unidadesValidas.contains(unidadeFormatada)) {
                unidade = unidadeFormatada;
                valido = true;
            } else {
                System.out.println("Erro: Unidade de medida inválida. Use: Quilogramas, Litros, Mililitros, Gramas ou Unidades.");
            }
        }
        return unidade;
    }
}