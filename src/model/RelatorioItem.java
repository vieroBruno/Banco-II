package model;

public class RelatorioItem {
    private String nome;
    private String descricao;
    private double precoVenda;
    private int quantidadeVendida;
    private double receitaGerada;

    // Getters e Setters para todos os campos

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public int getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(int quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }

    public double getReceitaGerada() {
        return receitaGerada;
    }

    public void setReceitaGerada(double receitaGerada) {
        this.receitaGerada = receitaGerada;
    }
}