package view;

import model.*;
import repository.jdbc.*;
import service.*;
import util.ValidacaoHelper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Optional;


public class PedidoView {

    private final Scanner sc = new Scanner(System.in);
    private final PedidoService pedidoService = new PedidoService(new JdbcPedidoRepository());
    private final MesaService mesaService = new MesaService(new JdbcMesaRepository());
    private final FuncionarioService funcionarioService = new FuncionarioService(new JdbcFuncionarioRepository());
    private final ItemService itemService = new ItemService(new JdbcItemRepository());
    private final PedidoItemService pedidoItemService = new PedidoItemService(new JdbcPedidoItemRepository());
    private final ProdutoService produtoService = new ProdutoService(new JdbcProdutoRepository());
    private final ReceitaService receitaService = new ReceitaService(new JdbcReceitaRepository());

    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Gestão de Pedidos ===");
            System.out.println("1. Novo Pedido");
            System.out.println("2. Listar Todos os Pedidos");
            System.out.println("3. Listar Pedidos Ativos");
            System.out.println("4. Editar Pedido");
            System.out.println("5. Excluir Pedido");
            System.out.println("0. Voltar");

            int opcao = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    cadastrar();
                    break;
                case 2:
                    listarTodos();
                    break;
                case 3:
                    listarAtivosComTotal();
                    break;
                case 4:
                    editarPedidoCompleto();
                    break;
                case 5:
                    excluir();
                    break;
                case 0: {
                    return;
                }
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrar() {
        System.out.println("\n--- Novo Pedido ---");

        Mesa mesaSelecionada = selecionarMesa("para adicionar um novo pedido");
        if (mesaSelecionada == null) return;

        Funcionario funcionarioSelecionado = selecionarFuncionario();
        if (funcionarioSelecionado == null) return;

        Pedido novoPedido = new Pedido(mesaSelecionada.getId_mesa(), 0, LocalDate.now(), "Ativo");
        novoPedido.setId_funcionario(funcionarioSelecionado.getIdFuncionario());
        int idNovoPedido = pedidoService.cadastrarPedido(novoPedido);

        if (idNovoPedido > 0) {
            System.out.println("Pedido criado com sucesso.Agora, adicione os itens.");
            adicionarItensAoPedido(idNovoPedido);
        } else if (idNovoPedido == -1) {
            System.out.println("Erro: Esta mesa já possui um pedido ativo.");
        } else {
            System.out.println("Erro: Não foi possível criar o pedido.");
        }
    }

    private void editarPedidoCompleto() {
        System.out.println("\n--- Selecione o Pedido para Editar ---");
        List<Pedido> pedidos = pedidoService.listarPedido();

        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido para editar.");
            return;
        }

        int cont = 0;
        for (Pedido p : pedidos) {
            cont++;
            Mesa m = mesaService.findById(p.getId_mesa());
            Funcionario f = funcionarioService.findById(p.getId_funcionario());
            System.out.printf("%d - Mesa: %d, Garçom: %s, Data: %s, Status: %s\n", cont, m.getNumero(), f.getNome(), p.getData_pedido(), p.getStatus());
        }
        System.out.println("0 - Cancelar");

        int escolha;
        do {
            escolha = ValidacaoHelper.lerInteiro(sc, "Confirme: ");
            if (escolha < 0 || escolha > pedidos.size() ){
                System.out.println("Opção inválida tente novamente");
            }
        } while (escolha < 0 || escolha > pedidos.size() );

        Pedido pedidoParaEditar = pedidos.get(escolha -1);

        System.out.println("\nEditando Pedido da Mesa " + mesaService.findById(pedidoParaEditar.getId_mesa()).getNumero());

        System.out.println("Deseja alterar a mesa do pedido? (S/N)");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            Mesa novaMesa = selecionarMesa("para qual deseja mover o pedido");
            if(novaMesa != null) {
                pedidoParaEditar.setId_mesa(novaMesa.getId_mesa());
            }
        }

        System.out.println("Deseja alterar o funcionário do pedido? (S/N)");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            Funcionario novoFuncionario = selecionarFuncionario();
            if(novoFuncionario != null) {
                pedidoParaEditar.setId_funcionario(novoFuncionario.getIdFuncionario());
            }
        }

        System.out.println("Deseja alterar a data do pedido? (S/N)");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            pedidoParaEditar.setData_pedido(ValidacaoHelper.lerData(sc, "Nova data do pedido (DD/MM/YYYY): "));
        }

        System.out.println("Deseja alterar o status do pedido? (S/N)");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            System.out.print("Novo Status (ex: Pago, Cancelado, Ativo): ");
            pedidoParaEditar.setStatus(sc.nextLine());
        }

        pedidoService.editarPedido(pedidoParaEditar);
    }


    private void listarTodos() {
        List<Pedido> pedidos = pedidoService.listarPedido();

        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido encontrado.");
            return;
        }
        System.out.println("\n--- Todos os Pedidos ---");
        for (Pedido p : pedidos) {
            Mesa m = mesaService.findById(p.getId_mesa());
            Funcionario f = funcionarioService.findById(p.getId_funcionario());
            if (m != null && f != null) {
                System.out.printf("Mesa: %d, Garçom: %s, Data: %s, Status: %s\n",
                        m.getNumero(), f.getNome(), p.getData_pedido(), p.getStatus());
            }
        }
    }

    private void listarAtivosComTotal() {
        Map<Integer, Double> totais = pedidoService.listarPedidosAtivosComTotal();
        if (totais.isEmpty()) {
            System.out.println("Nenhum pedido ativo no momento.");
            return;
        }
        System.out.println("\n--- Pedidos Ativos e Totais ---");
        for (Map.Entry<Integer, Double> entry : totais.entrySet()) {
            System.out.printf("Mesa Número: %d - Total do Pedido: R$ %.2f\n", entry.getKey(), entry.getValue());
        }

        System.out.println("\nDeseja encerrar algum pedido?");
        int numeroMesa = ValidacaoHelper.lerInteiro(sc, "Se sim, digite o número da mesa. Se não, digite 0: ");

        if (numeroMesa > 0) {
            List<Pedido> pedidosAtivos = pedidoService.listarPedidosAtivos();
            Optional<Pedido> pedidoParaEncerrarOpt = pedidosAtivos.stream()
                    .filter(p -> mesaService.findById(p.getId_mesa()).getNumero() == numeroMesa)
                    .findFirst();

            if (pedidoParaEncerrarOpt.isPresent()) {
                Pedido pedidoParaEncerrar = pedidoParaEncerrarOpt.get();
                pedidoParaEncerrar.setStatus("Pago");
                pedidoService.editarPedido(pedidoParaEncerrar);
                System.out.println("Pedido da mesa " + numeroMesa + " encerrado com sucesso!");
            } else {
                System.out.println("Nenhum pedido ativo encontrado para a mesa " + numeroMesa + ".");
            }
        }
    }

    private void excluir() {
        System.out.println("\n--- Selecione o Pedido para Excluir ---");
        List<Pedido> pedidos = pedidoService.listarPedido();

        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido para excluir.");
            return;
        }

        int cont = 0;
        for (Pedido p : pedidos) {
            cont++;
            Mesa m = mesaService.findById(p.getId_mesa());
            Funcionario f = funcionarioService.findById(p.getId_funcionario());
            if (m != null && f != null) {
                System.out.printf("%d - Mesa: %d, Garçom: %s, Data: %s, Status: %s\n", cont, m.getNumero(), f.getNome(), p.getData_pedido(), p.getStatus());
            }
        }
        System.out.println("0 - Cancelar");

        int escolha;
        do {
            escolha = ValidacaoHelper.lerInteiro(sc, "Confirme: ");
            if (escolha < 0 || escolha > pedidos.size() ){
                System.out.println("Opção inválida tente novamente");
            }
        } while (escolha < 0 || escolha > pedidos.size() );


        Pedido pedidoParaExcluir = pedidos.get(escolha -1);

        System.out.println("\nTem certeza que deseja deletar esse pedido? Todas as informações relacionadas a esse pedido serão excluídas.");
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
            pedidoService.excluirPedido(pedidoParaExcluir.getId_pedido());
        } else {
            System.out.println("Operação cancelada!");
        }
    }

    private Mesa selecionarMesa(String acao) {
        System.out.println("\n--- Selecione a Mesa " + acao + " ---");
        List<Mesa> mesas = mesaService.listarMesa();

        if (mesas.isEmpty()) {
            System.out.println("Nenhuma mesa cadastrada.");
            return null;
        }

        int cont = 0;
        for (Mesa m : mesas) {
            cont++;
            System.out.println(cont + " - Mesa Número: " + m.getNumero());
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
            return null;
        }

        return mesas.get(escolha - 1);
    }

    private Funcionario selecionarFuncionario() {
        System.out.println("\n--- Selecione o Funcionario ---");
        List<Funcionario> funcionarios = funcionarioService.listarFuncionario();

        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return null;
        }

        int cont = 0;
        for (Funcionario f : funcionarios) {
            cont++;
            System.out.println(cont + " - " + f.getNome());
        }
        System.out.println("0 - Cancelar");

        int escolha;
        do {
            escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");
            if (escolha < 0 || escolha > funcionarios.size()) {
                System.out.println("Opção inválida. Tente novamente!");
            }
        } while (escolha < 0 || escolha > funcionarios.size());

        return funcionarios.get(escolha - 1);
    }

    private Item selecionarItem() {
        System.out.println("\n--- Selecione o Item ---");
        List<Item> items = itemService.listarItem();

        if (items.isEmpty()) {
            System.out.println("Nenhum item cadastrado.");
            return null;
        }

        int cont = 0;
        for (Item i : items) {
            cont++;
            System.out.printf("%d - %s (R$ %.2f)\n", cont, i.getNome(), i.getPreco_venda());
        }
        System.out.println("0 - Cancelar");

        int escolha;
        do {
            escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");
            if (escolha < 0 || escolha > items.size()) {
                System.out.println("Opção inválida. Tente novamente!");
            }
        } while (escolha < 0 || escolha > items.size());

        return items.get(escolha - 1);
    }

    private void adicionarItensAoPedido(int idPedido) {
        int querAdicionar = 1;
        while (querAdicionar == 1) {
            Item itemSelecionado = selecionarItem();
            if (itemSelecionado == null) break;

            int quantidadePedida = ValidacaoHelper.lerInteiro(sc, "Quantidade: ");

            List<Produto> receita = receitaService.listarReceita(itemSelecionado.getId_item());
            boolean estoqueSuficiente = true;

            for (Produto produtoDaReceita : receita) {
                Produto produtoEmEstoque = produtoService.findById(produtoDaReceita.getId_produto());
                double quantidadeNecessaria = produtoDaReceita.getQuantidade() * quantidadePedida;

                if (produtoEmEstoque.getQuantidade() < quantidadeNecessaria) {
                    System.out.printf("Erro: Estoque insuficiente para '%s'. Necessário: %.2f, Disponível: %.2f\n",
                            produtoEmEstoque.getNome(), quantidadeNecessaria, produtoEmEstoque.getQuantidade());
                    estoqueSuficiente = false;
                    break;
                }
            }

            if (estoqueSuficiente) {
                for (Produto produtoDaReceita : receita) {
                    Produto produtoEmEstoque = produtoService.findById(produtoDaReceita.getId_produto());
                    double quantidadeADeduzir = produtoDaReceita.getQuantidade() * quantidadePedida;
                    double novoEstoque = produtoEmEstoque.getQuantidade() - quantidadeADeduzir;
                    produtoEmEstoque.setQuantidade(novoEstoque);
                    produtoService.editarProduto(produtoEmEstoque);
                }

                PedidoItem pedidoItem = new PedidoItem(idPedido, itemSelecionado.getId_item(), quantidadePedida);
                pedidoItemService.adicionarItemAoPedido(pedidoItem);
                System.out.println("Item '" + itemSelecionado.getNome() + "' adicionado e estoque atualizado.");

            } else {
                System.out.println("O item não foi adicionado ao pedido por falta de estoque.");
            }

            querAdicionar = ValidacaoHelper.lerInteiro(sc, "\nDeseja adicionar outro item?\n1. Sim\n2. Não\nEscolha uma opção: ");
        }
    }
}