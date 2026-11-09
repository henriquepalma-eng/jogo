package com.mycompany.mavenproject1.controller;

import com.mycompany.mavenproject1.App;
import com.mycompany.mavenproject1.dao.ItemDAO;
import com.mycompany.mavenproject1.model.Item;
import com.mycompany.mavenproject1.model.Jogador;
import com.mycompany.mavenproject1.util.GameContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller da Tela de Loja de Itens e Consumíveis.
 * Atende aos requisitos da Seção 4.4 do Edital IFMS.
 */
public class LojaController implements Initializable {

    @FXML private Label lblMoedasDisponiveis;
    @FXML private Label lblStatusLoja;

    @FXML private TableView<Item> tabelaLoja;
    @FXML private TableColumn<Item, String> colNome;
    @FXML private TableColumn<Item, String> colTipo;
    @FXML private TableColumn<Item, Integer> colPreco;
    @FXML private TableColumn<Item, String> colDescricao;

    private final GameContext context = GameContext.getInstance();
    private final ItemDAO itemDAO = new ItemDAO();
    private final ObservableList<Item> itensLojaList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColunas();
        carregarLoja();
    }

    private void configurarColunas() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tabelaLoja.setItems(itensLojaList);
    }

    private void carregarLoja() {
        itensLojaList.clear();
        Jogador j = context.getJogadorLogado();
        if (j != null) {
            lblMoedasDisponiveis.setText(String.valueOf(j.getMoedas()));
        }
        List<Item> itens = itemDAO.listarItensLoja();
        itensLojaList.addAll(itens);
    }

    @FXML
    private void comprarItemSelecionado() {
        Item selecionado = tabelaLoja.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            lblStatusLoja.setText("Selecione um produto da vitrine para comprar.");
            return;
        }

        Jogador j = context.getJogadorLogado();
        if (j != null) {
            if (j.gastarMoedas(selecionado.getPreco())) {
                itemDAO.adicionarItemInventario(j.getId(), selecionado.getId(), 1);
                context.salvarProgressoJogador();
                lblMoedasDisponiveis.setText(String.valueOf(j.getMoedas()));
                lblStatusLoja.setText("Compra realizada com sucesso! " + selecionado.getNome() + " enviado para a despensa.");
            } else {
                lblStatusLoja.setText("Moedas insuficientes! Você precisa de " + selecionado.getPreco() + " moedas.");
            }
        }
    }

    @FXML
    private void abrirInventario() {
        App.abrirInventario();
    }

    @FXML
    private void voltarAoMenu() throws IOException {
        App.setRoot("menu");
    }
}
