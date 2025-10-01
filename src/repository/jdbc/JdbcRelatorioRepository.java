package repository.jdbc;

import model.Produto;
import model.RelatorioItem;
import repository.RelatorioRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcRelatorioRepository implements RelatorioRepository {

    @Override
    public double vendasPorPeriodo(LocalDate inicio, LocalDate fim) {
        String query = "SELECT SUM(pi.quantidade * i.preco_venda) AS faturamento_total " +
                "FROM pedidos p " +
                "JOIN pedido_itens pi ON p.id_pedido = pi.fk_pedidos_id_pediido " +
                "JOIN item i ON pi.fk_item_id_items = i.id_items " +
                "WHERE p.status = 'Pago' AND p.data_pedido BETWEEN ? AND ?";
        double faturamento = 0;

        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setDate(1, java.sql.Date.valueOf(inicio));
            st.setDate(2, java.sql.Date.valueOf(fim));

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    faturamento = rs.getDouble("faturamento_total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao gerar relatório de vendas por período", e);
        }
        return faturamento;
    }

    @Override
    public List<RelatorioItem> itensMaisVendidos(LocalDate inicio, LocalDate fim) {
        // SQL ATUALIZADA para buscar mais campos
        String query = "SELECT i.nome, i.descricao, i.preco_venda, SUM(pi.quantidade) AS quantidade_total " +
                "FROM pedido_itens pi " +
                "JOIN item i ON pi.fk_item_id_items = i.id_items " +
                "JOIN pedidos p ON pi.fk_pedidos_id_pediido = p.id_pedido " +
                "WHERE p.data_pedido BETWEEN ? AND ? " +
                "GROUP BY i.nome, i.descricao, i.preco_venda " + // Agrupando pelos novos campos
                "ORDER BY quantidade_total DESC";
        List<RelatorioItem> ranking = new ArrayList<>();

        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setDate(1, java.sql.Date.valueOf(inicio));
            st.setDate(2, java.sql.Date.valueOf(fim));

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    RelatorioItem item = new RelatorioItem();
                    item.setNome(rs.getString("nome"));
                    item.setDescricao(rs.getString("descricao"));
                    item.setPrecoVenda(rs.getDouble("preco_venda"));
                    item.setQuantidadeVendida(rs.getInt("quantidade_total"));
                    ranking.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao gerar relatório de itens mais vendidos", e);
        }
        return ranking;
    }

    @Override
    public List<RelatorioItem> itensQueMaisGeramReceita(LocalDate inicio, LocalDate fim) {
        // SQL ATUALIZADA para buscar mais campos
        String query = "SELECT i.nome, i.descricao, i.preco_venda, SUM(pi.quantidade * i.preco_venda) AS receita_total " +
                "FROM pedido_itens pi " +
                "JOIN item i ON pi.fk_item_id_items = i.id_items " +
                "JOIN pedidos p ON pi.fk_pedidos_id_pediido = p.id_pedido " +
                "WHERE p.data_pedido BETWEEN ? AND ? " +
                "GROUP BY i.nome, i.descricao, i.preco_venda " + // Agrupando pelos novos campos
                "ORDER BY receita_total DESC";
        List<RelatorioItem> ranking = new ArrayList<>();

        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setDate(1, java.sql.Date.valueOf(inicio));
            st.setDate(2, java.sql.Date.valueOf(fim));

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    RelatorioItem item = new RelatorioItem();
                    item.setNome(rs.getString("nome"));
                    item.setDescricao(rs.getString("descricao"));
                    item.setPrecoVenda(rs.getDouble("preco_venda"));
                    item.setReceitaGerada(rs.getDouble("receita_total"));
                    ranking.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao gerar relatório de itens que mais geram receita", e);
        }
        return ranking;
    }


    @Override
    public List<Produto> relatorioEstoqueBaixo(double nivelMinimo) {
        String query = "SELECT id_produto, nome, unidade_medida, quantidade FROM produtos WHERE quantidade <= ?";
        List<Produto> produtos = new ArrayList<>();

        try (Connection con = new Conexao().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setDouble(1, nivelMinimo);

            try (ResultSet result = st.executeQuery()) {
                while (result.next()) {
                    Produto produto = new Produto(
                            result.getString("nome"),
                            result.getString("unidade_medida"),
                            result.getDouble("quantidade")
                    );
                    produto.setId_produto(result.getInt("id_produto"));
                    produtos.add(produto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao gerar relatório de estoque baixo", e);
        }
        return produtos;
    }
}