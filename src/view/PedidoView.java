package view;

import model.Funcionario;
import model.Item;
import model.Mesa;
import model.Pedido;
import model.PedidoItem;
import repository.jdbc.JdbcFuncionarioRepository;
import repository.jdbc.JdbcItemRepository;
import repository.jdbc.JdbcMesaRepository;
import repository.jdbc.JdbcPedidoItemRepository;
import repository.jdbc.JdbcPedidoRepository;
import service.FuncionarioService;
import service.ItemService;
import service.MesaService;
import service.PedidoItemService;
import service.PedidoService;
import util.ValidacaoHelper;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PedidoView {

    private final Scanner sc = new Scanner(System.in);
    private final PedidoService pedidoService = new PedidoService(new JdbcPedidoRepository());
    private final MesaService mesaService = new MesaService(new JdbcMesaRepository());
    private final FuncionarioService funcionarioService = new FuncionarioService(new JdbcFuncionarioRepository());
    private final ItemService itemService = new ItemService(new JdbcItemRepository());
    private final PedidoItemService pedidoItemService = new PedidoItemService(new JdbcPedidoItemRepository());

    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Gestão de Pedidos ===");
            System.out.println("1. Novo Pedido");
            System.out.println("2. Listar Todos os Pedidos");
            System.out.println("3. Listar Pedidos Ativos");
            System.out.println("4. Editar Status do Pedido");
            System.out.println("5. Excluir Pedido Ativo");
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
                    editar();
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
        } else {
            System.out.println("Erro: Não foi possível criar o pedido.");
        }
    }

    private void adicionarItensAoPedido(int idPedido) {
        int querAdicionar = 1;
        while (querAdicionar == 1) {
            Item itemSelecionado = selecionarItem();
            if (itemSelecionado == null) {
                break;
            }

            int quantidade = ValidacaoHelper.lerInteiro(sc, "Quantidade: ");

            PedidoItem pedidoItem = new PedidoItem(idPedido, itemSelecionado.getId_item(), quantidade);
            pedidoItemService.adicionarItemAoPedido(pedidoItem);
            System.out.println("Item '" + itemSelecionado.getNome() + "' adicionado.");

            System.out.println("\nDeseja adicionar outro item?");
            System.out.println("1. Sim");
            System.out.println("2. Não");
            querAdicionar = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ");
        }
    }

    private void editar() {
        System.out.println("\n--- Selecione o Pedido para Editar o Status ---");
        List<Pedido> pedidosAtivos = pedidoService.listarPedidosAtivos();

        if (pedidosAtivos.isEmpty()) {
            System.out.println("Nenhum pedido ativo para editar.");
            return;
        }

        int cont = 0;
        for (Pedido p : pedidosAtivos) {
            cont++;
            System.out.printf("%d - Pedido ID: %d, Mesa ID: %d, Status: %s\n", cont, p.getId_pedido(), p.getId_mesa(), p.getStatus());
        }
        System.out.println("0 - Cancelar");

        int escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ") -1;

        if (escolha < 0 || escolha >= pedidosAtivos.size()) {
            System.out.println("Operação cancelada ou opção inválida.");
            return;
        }

        Pedido pedidoParaEditar = pedidosAtivos.get(escolha);

        System.out.print("Novo Status (ex: Finalizado, Cancelado): ");
        String novoStatus = sc.nextLine();

        pedidoParaEditar.setStatus(novoStatus);
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
            System.out.printf("ID: %d, Mesa ID: %d, Func. ID: %d, Data: %s, Status: %s\n",
                    p.getId_pedido(), p.getId_mesa(), p.getId_funcionario(), p.getData_pedido(), p.getStatus());
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
    }

    private void excluir() {
        System.out.println("\n--- Selecione o Pedido Ativo para Excluir ---");
        List<Pedido> pedidosAtivos = pedidoService.listarPedidosAtivos();

        if (pedidosAtivos.isEmpty()) {
            System.out.println("Nenhum pedido ativo para excluir.");
            return;
        }

        int cont = 0;
        for (Pedido p : pedidosAtivos) {
            cont++;
            System.out.printf("%d - Pedido ID: %d, Mesa ID: %d\n", cont, p.getId_pedido(), p.getId_mesa());
        }
        

        System.out.println("0 - Cancelar");

        int escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ") - 1;

        if (escolha < 0 || escolha >= pedidosAtivos.size()) {
            System.out.println("Operação cancelada ou opção inválida.");
            return;
        }

        Pedido pedidoParaExcluir = pedidosAtivos.get(escolha);

        System.out.println("Deseja realmente excluir o pedido " + pedidoParaExcluir.getId_pedido() + "?");
        System.out.println("1. Sim");
        System.out.println("2. Não");
        int escolhafinal = ValidacaoHelper.lerInteiro(sc, "Confirme: ");

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

        int escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ") - 1;

        if (escolha < 0 || escolha >= mesas.size()) {
            System.out.println("Operação cancelada ou opção inválida.");
            return null;
        }
        return mesas.get(escolha);
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

        int escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ") - 1;

        if (escolha < 0 || escolha >= funcionarios.size()) {
            System.out.println("Operação cancelada ou opção inválida.");
            return null;
        }
        return funcionarios.get(escolha);
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

        int escolha = ValidacaoHelper.lerInteiro(sc, "Escolha uma opção: ") - 1;

        if (escolha < 0 || escolha >= items.size()) {
            System.out.println("Operação cancelada ou opção inválida.");
            return null;
        }
        return items.get(escolha);
    }
}