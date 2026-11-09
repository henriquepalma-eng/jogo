package com.mycompany.mavenproject1.model;

import java.sql.Timestamp;

/**
 * Entidade Partida (Histórico de Run e Ranking)
 */
public class Partida {

    private int id;
    private int jogadorId;
    private String nomeJogador;
    private int pontuacao;
    private int clientesAtendidos;
    private int moedasGanhas;
    private boolean derrotouBoss;
    private Timestamp dataPartida;

    public Partida() {
    }

    public Partida(int id, int jogadorId, String nomeJogador, int pontuacao, int clientesAtendidos, int moedasGanhas, boolean derrotouBoss, Timestamp dataPartida) {
        this.id = id;
        this.jogadorId = jogadorId;
        this.nomeJogador = nomeJogador;
        this.pontuacao = pontuacao;
        this.clientesAtendidos = clientesAtendidos;
        this.moedasGanhas = moedasGanhas;
        this.derrotouBoss = derrotouBoss;
        this.dataPartida = dataPartida;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJogadorId() {
        return jogadorId;
    }

    public void setJogadorId(int jogadorId) {
        this.jogadorId = jogadorId;
    }

    public String getNomeJogador() {
        return nomeJogador;
    }

    public void setNomeJogador(String nomeJogador) {
        this.nomeJogador = nomeJogador;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public int getClientesAtendidos() {
        return clientesAtendidos;
    }

    public void setClientesAtendidos(int clientesAtendidos) {
        this.clientesAtendidos = clientesAtendidos;
    }

    public int getMoedasGanhas() {
        return moedasGanhas;
    }

    public void setMoedasGanhas(int moedasGanhas) {
        this.moedasGanhas = moedasGanhas;
    }

    public boolean isDerrotouBoss() {
        return derrotouBoss;
    }

    public void setDerrotouBoss(boolean derrotouBoss) {
        this.derrotouBoss = derrotouBoss;
    }

    public Timestamp getDataPartida() {
        return dataPartida;
    }

    public void setDataPartida(Timestamp dataPartida) {
        this.dataPartida = dataPartida;
    }
}
