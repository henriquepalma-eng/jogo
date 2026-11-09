package com.mycompany.mavenproject1.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa um pedido feito por um cliente no restaurante.
 */
public class Pedido {

    private String nomePrato;
    private List<String> ingredientes;
    private int recompensaMoedas;
    private int recompensaPontos;
    private double tempoMaximo;

    public Pedido(String nomePrato, List<String> ingredientes, int recompensaMoedas, int recompensaPontos, double tempoMaximo) {
        this.nomePrato = nomePrato;
        this.ingredientes = new ArrayList<>(ingredientes);
        this.recompensaMoedas = recompensaMoedas;
        this.recompensaPontos = recompensaPontos;
        this.tempoMaximo = tempoMaximo;
    }

    public boolean verificarReceita(List<String> ingredientesJogador) {
        if (ingredientesJogador.size() != ingredientes.size()) {
            return false;
        }
        List<String> copiaEsperada = new ArrayList<>(ingredientes);
        List<String> copiaJogador = new ArrayList<>(ingredientesJogador);
        Collections.sort(copiaEsperada);
        Collections.sort(copiaJogador);
        return copiaEsperada.equals(copiaJogador);
    }

    public String getNomePrato() {
        return nomePrato;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public int getRecompensaMoedas() {
        return recompensaMoedas;
    }

    public int getRecompensaPontos() {
        return recompensaPontos;
    }

    public double getTempoMaximo() {
        return tempoMaximo;
    }
}
