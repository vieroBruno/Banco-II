package model;

public class Mesa {
    private int id_mesa;
    private int numero;
    private int capacacidade;

    public Mesa(int id_mesa,int numero, int capacacidade) {
        this.id_mesa = id_mesa;
        this.numero = numero;
        this.capacacidade = capacacidade;
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

    public int getCapacacidade() {
        return capacacidade;
    }

    public void setCapacacidade(int capacacidade) {
        this.capacacidade = capacacidade;
    }
}
