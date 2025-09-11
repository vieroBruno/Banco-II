package model;

public class Receita {
    private int id_item;
    private int id_produto;
    private double quantidade;

    public Receita(int id_item, int id_produto, double quantidade) {
        this.id_item = id_item;
        this.id_produto = id_produto;
        this.quantidade = quantidade;
    }

    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
    }

    public int getId_produto() {
        return id_produto;
    }

    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }
}
