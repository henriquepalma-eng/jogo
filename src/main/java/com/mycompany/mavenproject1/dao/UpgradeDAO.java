package com.mycompany.mavenproject1.dao;

import com.mycompany.mavenproject1.database.DatabaseConnection;
import com.mycompany.mavenproject1.model.Upgrade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para Upgrades permanentes.
 */
public class UpgradeDAO {

    public List<Upgrade> listarPorJogador(int jogadorId) {
        List<Upgrade> lista = new ArrayList<>();
        String sql = "SELECT u.id, u.chave, u.nome, u.descricao, u.custo_base, u.nivel_maximo, " +
                     "COALESCE(ju.nivel_atual, 0) as nivel_atual " +
                     "FROM upgrades u " +
                     "LEFT JOIN jogador_upgrades ju ON u.id = ju.upgrade_id AND ju.jogador_id = ? " +
                     "ORDER BY u.id ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jogadorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Upgrade(
                            rs.getInt("id"),
                            rs.getString("chave"),
                            rs.getString("nome"),
                            rs.getString("descricao"),
                            rs.getInt("custo_base"),
                            rs.getInt("nivel_maximo"),
                            rs.getInt("nivel_atual")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar upgrades: " + e.getMessage());
            // Fallback padrão para testes
            lista.add(new Upgrade(1, "fogao_rapido", "Fogão Industrial", "Prepara pratos 15% mais rápido por nível.", 100, 5, 0));
            lista.add(new Upgrade(2, "paciencia_cliente", "Ambiente Confortável", "Aumenta a paciência dos clientes em 20%.", 80, 5, 0));
            lista.add(new Upgrade(3, "gorjeta_extra", "Charme do Garçom", "Gorjetas 25% maiores por nível.", 120, 5, 0));
            lista.add(new Upgrade(4, "capacidade_mesas", "Expansão do Salão", "Aumenta a capacidade de atendimento.", 150, 3, 0));
        }
        return lista;
    }

    public boolean salvarNivelUpgrade(int jogadorId, int upgradeId, int novoNivel) {
        String sql = "INSERT INTO jogador_upgrades (jogador_id, upgrade_id, nivel_atual) " +
                     "VALUES (?, ?, ?) " +
                     "ON CONFLICT (jogador_id, upgrade_id) DO UPDATE SET nivel_atual = EXCLUDED.nivel_atual";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jogadorId);
            stmt.setInt(2, upgradeId);
            stmt.setInt(3, novoNivel);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao salvar nível do upgrade: " + e.getMessage());
            return false;
        }
    }
}
