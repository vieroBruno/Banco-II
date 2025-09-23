package model;

public class Item {
    private int id_item;
    private String nome;
    private double preco_venda;
    private String descricao;

    public Item(String nome, double preco_venda, String descricao) {
        this.nome = nome;
        this.preco_venda = preco_venda;
        this.descricao = descricao;
    }

    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco_venda() {
        return preco_venda;
    }

    public void setPreco_venda(double preco_venda) {
        this.preco_venda = preco_venda;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String toString() {
        return
            " : nome='" + nome + '\'' +
            ", preco de venda='" + preco_venda + '\'' +
            ", descrição=" + descricao;
    }
}
