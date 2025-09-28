package view;

import model.Funcionario;
import repository.jdbc.JdbcFuncionarioRepository;
import service.FuncionarioService;
import util.ValidacaoHelper;

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
        String nome = ValidacaoHelper.isStringValida(sc, "Nome: ");

        String cargo = ValidacaoHelper.isStringValida(sc,"Cargo: ");

        String telefone = ValidacaoHelper.lerTelefone(sc, "Telefone (com DDD): ");

        double salario = ValidacaoHelper.lerDouble(sc, "Salário: ");

        Funcionario funcionario = new Funcionario(0,nome, cargo, salario, telefone);
        funcionarioService.cadastrarFuncionario(funcionario);
    }

    private void editar()  {
        System.out.println("\n--- Selecione o Funcionário para editar ---");
        List<Funcionario> funcionarios = listar("editar");

        if(funcionarios.isEmpty()) {
            return;
        }
        System.out.println("0 - Cancelar");

        int escolha;

        do {
            escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");
            if (escolha < 0 || escolha > funcionarios.size()) {
                System.out.println("Opção inválida. Tente novamente!");
            }
        } while (escolha < 0 || escolha > funcionarios.size());

        if (escolha == 0) {
            System.out.println("Operação cancelada!");
            return;
        }

        Funcionario fParaEditar = funcionarios.get(escolha - 1);
        Funcionario fAtualizado = new Funcionario(fParaEditar.getIdFuncionario(), fParaEditar.getNome(), fParaEditar.getCargo(), fParaEditar.getSalario(), fParaEditar.getTelefone());

        System.out.println("Editando dados de: " + fAtualizado.getNome());


        System.out.println("Deseja alterar o nome? (S/N)");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            fAtualizado.setNome(ValidacaoHelper.isStringValida(sc,"Novo nome: "));
        }

        System.out.println("Deseja alterar o cargo? (S/N)");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            fAtualizado.setCargo(ValidacaoHelper.isStringValida(sc,"Novo cargo: "));
        }

        System.out.println("Deseja alterar o salário? (S/N)");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            fAtualizado.setSalario(ValidacaoHelper.lerDouble(sc, "Novo salário: "));
        }

        System.out.println("Deseja alterar o telefone? (S/N)");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            fAtualizado.setTelefone(ValidacaoHelper.lerTelefone(sc, "Novo telefone (com DDD): "));
        }

        funcionarioService.editarFuncionario(fAtualizado);
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

    private void excluir() {
        System.out.println("\n--- Selecione o Funcionário para excluir ---");
        List<Funcionario> funcionarios = listar("excluir");

        if(funcionarios.isEmpty()) {
            return;
        }

        System.out.println("0 - Cancelar");

        int escolha;
        do {
            escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");
            if (escolha < 0 || escolha > funcionarios.size()) {
                System.out.println("Opção inválida. Tente novamente!");
            }
        } while (escolha < 0 || escolha > funcionarios.size());

        if (escolha == 0) {
            System.out.println("Operação cancelada!");
            return;
        }

        Funcionario funcionarioParaExcluir = funcionarios.get(escolha - 1);

        System.out.println("Deseja realmente excluir esse funcionário? : " + funcionarioParaExcluir.getNome());
        System.out.println("Todas as informações relacionadas a esse funcionário serão excluidas.");
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
            funcionarioService.excluirFuncionario(funcionarioParaExcluir.getIdFuncionario());
        } else {
            System.out.println("Operação cancelada!");
        }
    }


}
