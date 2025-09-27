package util;

import java.util.InputMismatchException;
import java.util.Scanner;

public final class ValidacaoHelper {

    private ValidacaoHelper() {}

    public static boolean isStringValida(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static double lerDouble(Scanner sc, String mensagem) {
        double valor = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensagem);
            try {
                valor = sc.nextDouble();
                valido = true;
            } catch (InputMismatchException e) {
                System.out.println("Erro: Por favor, digite um número válido (ex: 1500.50).");
            } finally {
                sc.nextLine();
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
                sc.next(); // Limpa o buffer do scanner para evitar loop infinito
            }
        }
        sc.nextLine(); // Limpa a quebra de linha restante do buffer
        return valor;
    }

}