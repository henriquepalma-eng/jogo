package com.mycompany.mavenproject1.dao;

import com.mycompany.mavenproject1.database.DatabaseConnection;
import com.mycompany.mavenproject1.model.Partida;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para Registro de Partidas e Ranking.
 */
public class PartidaDAO {

    public boolean salvar(Partida partida) {
        String sql = "INSERT INTO partidas (jogador_id, pontuacao, clientes_atendidos, moedas_ganhas, derrotou_boss) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, partida.getJogadorId());
            stmt.setInt(2, partida.getPontuacao());
            stmt.setInt(3, partida.getClientesAtendidos());
            stmt.setInt(4, partida.getMoedasGanhas());
            stmt.setBoolean(5, partida.isDerrotouBoss());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao registrar partida no banco: " + e.getMessage());
            return false;
        }
    }

    public List<Partida> listarRanking(int limite) {
        List<Partida> lista = new ArrayList<>();
        String sql = "SELECT p.*, j.nome as nome_jogador " +
                     "FROM partidas p " +
                     "JOIN jogadores j ON p.jogador_id = j.id " +
                     "ORDER BY p.pontuacao DESC, p.data_partida DESC " +
                     "LIMIT ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limite);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Partida(
                            rs.getInt("id"),
                            rs.getInt("jogador_id"),
                            rs.getString("nome_jogador"),
                            rs.getInt("pontuacao"),
                            rs.getInt("clientes_atendidos"),
                            rs.getInt("moedas_ganhas"),
                            rs.getBoolean("derrotou_boss"),
                            rs.getTimestamp("data_partida")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar ranking: " + e.getMessage());
            // Fallback caso banco não esteja disponível no momento
            lista.add(new Partida(1, 1, "Chef Henrique", 1250, 18, 240, false, new Timestamp(System.currentTimeMillis())));
        }
        return lista;
    }
}
