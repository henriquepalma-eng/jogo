package com.mycompany.mavenproject1.util;

import com.mycompany.mavenproject1.model.Partida;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Utilitário para exportação do Ranking em formatos CSV e TXT formatado.
 * Atende aos requisitos da Seção 8 do Edital IFMS.
 */
public class ExportadorRanking {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static void exportarCSV(File destino, List<Partida> partidas) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(destino))) {
            // Cabeçalho CSV
            writer.println("Posicao;Jogador;Pontuacao;ClientesAtendidos;MoedasGanhas;DerrotouBoss;DataPartida");
            int posicao = 1;
            for (Partida p : partidas) {
                String dataStr = p.getDataPartida() != null ? sdf.format(p.getDataPartida()) : "-";
                writer.printf("%d;%s;%d;%d;%d;%s;%s%n",
                        posicao++,
                        p.getNomeJogador(),
                        p.getPontuacao(),
                        p.getClientesAtendidos(),
                        p.getMoedasGanhas(),
                        p.isDerrotouBoss() ? "SIM" : "NAO",
                        dataStr
                );
            }
        }
    }

    public static void exportarTXT(File destino, List<Partida> partidas) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(destino))) {
            writer.println("==========================================================================");
            writer.println("              MESTRE DA COZINHA - RANKING OFICIAL DE CHEFS                ");
            writer.println("   Desenvolvedor: Henrique Jara Palma | IFMS - Topicos de Programacao 2   ");
            writer.println("==========================================================================");
            writer.printf("%-4s | %-20s | %-8s | %-8s | %-8s | %-6s | %-19s%n",
                    "POS", "JOGADOR", "PONTOS", "CLIENTES", "MOEDAS", "BOSS", "DATA");
            writer.println("--------------------------------------------------------------------------");

            int posicao = 1;
            for (Partida p : partidas) {
                String dataStr = p.getDataPartida() != null ? sdf.format(p.getDataPartida()) : "-";
                writer.printf("#%-3d | %-20s | %-8d | %-8d | %-8d | %-6s | %-19s%n",
                        posicao++,
                        p.getNomeJogador(),
                        p.getPontuacao(),
                        p.getClientesAtendidos(),
                        p.getMoedasGanhas(),
                        p.isDerrotouBoss() ? "SIM" : "NAO",
                        dataStr
                );
            }
            writer.println("==========================================================================");
            writer.println("Gerado automaticamente pelo sistema.");
        }
    }
}
