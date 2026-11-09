package com.mycompany.mavenproject1.model;

/**
 * Representa um cliente na fila ou mesa do restaurante.
 */
public class Cliente {

    private String nome;
    private String icone;
    private Pedido pedido;
    private double pacienciaTotal;
    private double pacienciaAtual;
    private boolean atendido;
    private boolean desistiu;

    public Cliente(String nome, String icone, Pedido pedido, double pacienciaTotal) {
        this.nome = nome;
        this.icone = icone;
        this.pedido = pedido;
        this.pacienciaTotal = pacienciaTotal;
        this.pacienciaAtual = pacienciaTotal;
        this.atendido = false;
        this.desistiu = false;
    }

    public void decrementarPaciencia(double deltaSegundos) {
        if (!atendido && !desistiu) {
            this.pacienciaAtual -= deltaSegundos;
            if (this.pacienciaAtual <= 0) {
                this.pacienciaAtual = 0;
                this.desistiu = true;
            }
        }
    }

    public void restaurarPaciencia(double porcentagem) {
        this.pacienciaAtual = Math.min(this.pacienciaTotal, this.pacienciaAtual + (this.pacienciaTotal * porcentagem));
        this.desistiu = false;
    }

    public double getProgressoPaciencia() {
        if (pacienciaTotal <= 0) return 0;
        return Math.max(0, Math.min(1.0, pacienciaAtual / pacienciaTotal));
    }

    public String getNome() {
        return nome;
    }

    public String getIcone() {
        return icone;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public double getPacienciaTotal() {
        return pacienciaTotal;
    }

    public double getPacienciaAtual() {
        return pacienciaAtual;
    }

    public boolean isAtendido() {
        return atendido;
    }

    public void setAtendido(boolean atendido) {
        this.atendido = atendido;
    }

    public boolean isDesistiu() {
        return desistiu;
    }
}
