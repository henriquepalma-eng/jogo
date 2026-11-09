package com.mycompany.mavenproject1.dao;

import com.mycompany.mavenproject1.database.DatabaseConnection;
import com.mycompany.mavenproject1.model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para Itens da Loja e Inventário do Jogador.
 */
public class ItemDAO {

    public List<Item> listarItensLoja() {
        List<Item> lista = new ArrayList<>();
        String sql = "SELECT * FROM itens ORDER BY preco ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Item(
                        rs.getInt("id"),
                        rs.getString("chave"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getInt("preco"),
                        rs.getString("tipo"),
                        0
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar itens da loja: " + e.getMessage());
            // Fallback
            lista.add(new Item(1, "cafe_espresso", "Café Expresso Turbo", "Dá velocidade de preparo dobrada por 15s.", 30, "CONSUMIVEL", 0));
            lista.add(new Item(2, "sobremesa_cortesia", "Sobremesa de Cortesia", "Restaura paciência de um cliente.", 25, "CONSUMIVEL", 0));
            lista.add(new Item(3, "tempero_dourado", "Tempero Gourmet Dourado", "Prato servido vale 2x mais moedas.", 40, "CONSUMIVEL", 0));
        }
        return lista;
    }

    public List<Item> listarInventarioJogador(int jogadorId) {
        List<Item> lista = new ArrayList<>();
        String sql = "SELECT i.*, ji.quantidade " +
                     "FROM itens i " +
                     "JOIN jogador_inventario ji ON i.id = ji.item_id " +
                     "WHERE ji.jogador_id = ? AND ji.quantidade > 0 " +
                     "ORDER BY i.nome ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jogadorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Item(
                            rs.getInt("id"),
                            rs.getString("chave"),
                            rs.getString("nome"),
                            rs.getString("descricao"),
                            rs.getInt("preco"),
                            rs.getString("tipo"),
                            rs.getInt("quantidade")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar inventário do jogador: " + e.getMessage());
            // Fallback
            lista.add(new Item(1, "cafe_espresso", "Café Expresso Turbo", "Dá velocidade de preparo dobrada por 15s.", 30, "CONSUMIVEL", 2));
            lista.add(new Item(2, "sobremesa_cortesia", "Sobremesa de Cortesia", "Restaura paciência de um cliente.", 25, "CONSUMIVEL", 1));
        }
        return lista;
    }

    public boolean adicionarItemInventario(int jogadorId, int itemId, int quantidade) {
        String sql = "INSERT INTO jogador_inventario (jogador_id, item_id, quantidade) " +
                     "VALUES (?, ?, ?) " +
                     "ON CONFLICT (jogador_id, item_id) DO UPDATE SET quantidade = jogador_inventario.quantidade + EXCLUDED.quantidade";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jogadorId);
            stmt.setInt(2, itemId);
            stmt.setInt(3, quantidade);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar item ao inventário: " + e.getMessage());
            return false;
        }
    }

    public boolean consumirItemInventario(int jogadorId, int itemId) {
        String sql = "UPDATE jogador_inventario SET quantidade = quantidade - 1 " +
                     "WHERE jogador_id = ? AND item_id = ? AND quantidade > 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jogadorId);
            stmt.setInt(2, itemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao consumir item do inventário: " + e.getMessage());
            return false;
        }
    }
}
