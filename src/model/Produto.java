package model;

public class Produto {
    private int id_produto;
    private String nome;
    private String unidade_medida;
    private double quantidade;


    public Produto(String nome, String unidade_medida, double quantidade) {
        this.nome = nome;
        this.unidade_medida = unidade_medida;
        this.quantidade = quantidade;
    }

    public int getId_produto() {
        return id_produto;
    }

    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUnidade_medida() {
        return unidade_medida;
    }

    public void setUnidade_medida(String unidade_medida) {
        this.unidade_medida = unidade_medida;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public String toString() {
        return
            " : nome='" + nome + '\'' +
            ", unidade de medida='" + unidade_medida + '\'' +
            ", quantidade=" + quantidade;
    }
}
