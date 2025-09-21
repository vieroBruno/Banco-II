package model;

public class Mesa {
    private int id_mesa;
    private int numero;
    private int capacidade;

    public Mesa(int id_mesa,int numero, int capacidade) {
        this.id_mesa = id_mesa;
        this.numero = numero;
        this.capacidade = capacidade;
    }

    public int getId_mesa() {
        return id_mesa;
    }

    public void setId_mesa(int id_mesa) {
        this.id_mesa = id_mesa;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public String toString() {
        return
                " : numero=" + numero +
                ", capacidade=" + capacidade;
    }
}
