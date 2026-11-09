package com.mycompany.mavenproject1.model;

import java.sql.Timestamp;

/**
 * Entidade Jogador (Chef)
 * Projeto: Mestre da Cozinha
 */
public class Jogador {

    private int id;
    private String nome;
    private int moedas;
    private int pontosReputacao;
    private int nivel;
    private Timestamp dataCriacao;

    public Jogador() {
        this.moedas = 50;
        this.pontosReputacao = 0;
        this.nivel = 1;
    }

    public Jogador(int id, String nome, int moedas, int pontosReputacao, int nivel) {
        this.id = id;
        this.nome = nome;
        this.moedas = moedas;
        this.pontosReputacao = pontosReputacao;
        this.nivel = nivel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getMoedas() {
        return moedas;
    }

    public void setMoedas(int moedas) {
        this.moedas = moedas;
    }

    public void adicionarMoedas(int valor) {
        this.moedas += valor;
    }

    public boolean gastarMoedas(int valor) {
        if (this.moedas >= valor) {
            this.moedas -= valor;
            return true;
        }
        return false;
    }

    public int getPontosReputacao() {
        return pontosReputacao;
    }

    public void setPontosReputacao(int pontosReputacao) {
        this.pontosReputacao = pontosReputacao;
    }

    public void adicionarReputacao(int valor) {
        this.pontosReputacao += valor;
        // Evolução de nível a cada 200 pontos de reputação
        this.nivel = Math.max(1, (this.pontosReputacao / 200) + 1);
    }

    public boolean gastarReputacao(int valor) {
        if (this.pontosReputacao >= valor) {
            this.pontosReputacao -= valor;
            return true;
        }
        return false;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public Timestamp getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Timestamp dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
