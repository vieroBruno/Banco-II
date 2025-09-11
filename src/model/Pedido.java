package model;

import java.time.LocalDate;

public class Pedido {
    private int id_mesa;
    private int id_pedido;
    private int id_funcionario;
    private LocalDate data_pedido;
    private String status;

    public Pedido(int id_mesa, int id_pedido, LocalDate data_pedido, String status) {
        this.id_mesa = id_mesa;
        this.id_pedido = id_pedido;
        this.data_pedido = data_pedido;
        this.status = status;
    }

    public int getId_mesa() {
        return id_mesa;
    }

    public void setId_mesa(int id_mesa) {
        this.id_mesa = id_mesa;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getId_funcionario() {
        return id_funcionario;
    }

    public void setId_funcionario(int id_funcionario) {
        this.id_funcionario = id_funcionario;
    }

    public LocalDate getData_pedido() {
        return data_pedido;
    }

    public void setData_pedido(LocalDate data_pedido) {
        this.data_pedido = data_pedido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
