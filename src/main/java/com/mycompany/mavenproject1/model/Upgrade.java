package com.mycompany.mavenproject1.model;

/**
 * Entidade Upgrade (Melhoria permanente do restaurante)
 */
public class Upgrade {

    private int id;
    private String chave;
    private String nome;
    private String descricao;
    private int custoBase;
    private int nivelMaximo;
    private int nivelAtual;

    public Upgrade() {
    }

    public Upgrade(int id, String chave, String nome, String descricao, int custoBase, int nivelMaximo, int nivelAtual) {
        this.id = id;
        this.chave = chave;
        this.nome = nome;
        this.descricao = descricao;
        this.custoBase = custoBase;
        this.nivelMaximo = nivelMaximo;
        this.nivelAtual = nivelAtual;
    }

    public int getCustoProximoNivel() {
        return custoBase * (nivelAtual + 1);
    }

    public boolean isNivelMaximoAtingido() {
        return nivelAtual >= nivelMaximo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

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

    public int getCustoBase() {
        return custoBase;
    }

    public void setCustoBase(int custoBase) {
        this.custoBase = custoBase;
    }

    public int getNivelMaximo() {
        return nivelMaximo;
    }

    public void setNivelMaximo(int nivelMaximo) {
        this.nivelMaximo = nivelMaximo;
    }

    public int getNivelAtual() {
        return nivelAtual;
    }

    public void setNivelAtual(int nivelAtual) {
        this.nivelAtual = nivelAtual;
    }
}
