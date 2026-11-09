package com.mycompany.mavenproject1.dao;

import com.mycompany.mavenproject1.database.DatabaseConnection;
import com.mycompany.mavenproject1.model.Jogador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para a entidade Jogador.
 */
public class JogadorDAO {

    public Jogador buscarOuCriar(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            nome = "Chef Henrique";
        }
        nome = nome.trim();

        String sqlBusca = "SELECT * FROM jogadores WHERE nome = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBusca)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairJogador(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar jogador: " + e.getMessage());
            // Fallback caso o banco não esteja rodando
            return new Jogador(1, nome, 100, 50, 1);
        }

        // Se não encontrou, insere
        String sqlInsert = "INSERT INTO jogadores (nome, moedas, pontos_reputacao, nivel) VALUES (?, 100, 0, 1) RETURNING *";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Jogador novo = extrairJogador(rs);
                    // Inicializar upgrades padrão para o novo jogador
                    inicializarUpgradesJogador(novo.getId(), conn);
                    return novo;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao criar jogador: " + e.getMessage());
        }

        return new Jogador(1, nome, 100, 0, 1);
    }

    public boolean atualizar(Jogador jogador) {
        String sql = "UPDATE jogadores SET moedas = ?, pontos_reputacao = ?, nivel = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jogador.getMoedas());
            stmt.setInt(2, jogador.getPontosReputacao());
            stmt.setInt(3, jogador.getNivel());
            stmt.setInt(4, jogador.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar jogador: " + e.getMessage());
            return false;
        }
    }

    public List<Jogador> listarTodos() {
        List<Jogador> lista = new ArrayList<>();
        String sql = "SELECT * FROM jogadores ORDER BY pontos_reputacao DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(extrairJogador(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar jogadores: " + e.getMessage());
        }
        return lista;
    }

    private void inicializarUpgradesJogador(int jogadorId, Connection conn) {
        String sql = "INSERT INTO jogador_upgrades (jogador_id, upgrade_id, nivel_atual) " +
                     "SELECT ?, id, 0 FROM upgrades ON CONFLICT DO NOTHING";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jogadorId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar upgrades: " + e.getMessage());
        }
    }

    private Jogador extrairJogador(ResultSet rs) throws SQLException {
        Jogador j = new Jogador();
        j.setId(rs.getInt("id"));
        j.setNome(rs.getString("nome"));
        j.setMoedas(rs.getInt("moedas"));
        j.setPontosReputacao(rs.getInt("pontos_reputacao"));
        j.setNivel(rs.getInt("nivel"));
        j.setDataCriacao(rs.getTimestamp("data_criacao"));
        return j;
    }
}
