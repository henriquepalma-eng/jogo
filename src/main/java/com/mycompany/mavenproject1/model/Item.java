package com.mycompany.mavenproject1.model;

/**
 * Entidade Item (Loja e Inventário em Stage separado)
 */
public class Item {

    private int id;
    private String chave;
    private String nome;
    private String descricao;
    private int preco;
    private String tipo; // CONSUMIVEL ou EQUIPAMENTO
    private int quantidade; // Quantidade no inventário

    public Item() {
    }

    public Item(int id, String chave, String nome, String descricao, int preco, String tipo, int quantidade) {
        this.id = id;
        this.chave = chave;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.tipo = tipo;
        this.quantidade = quantidade;
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

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
