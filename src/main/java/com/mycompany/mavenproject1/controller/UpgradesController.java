package com.mycompany.mavenproject1.controller;

import com.mycompany.mavenproject1.App;
import com.mycompany.mavenproject1.dao.UpgradeDAO;
import com.mycompany.mavenproject1.model.Jogador;
import com.mycompany.mavenproject1.model.Upgrade;
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
 * Controller da Tela de Upgrades Permanentes.
 * Atende aos requisitos da Seção 4.3 do Edital IFMS.
 */
public class UpgradesController implements Initializable {

    @FXML private Label lblPontosDisponiveis;
    @FXML private Label lblStatusUpgrade;

    @FXML private TableView<Upgrade> tabelaUpgrades;
    @FXML private TableColumn<Upgrade, String> colNome;
    @FXML private TableColumn<Upgrade, String> colDescricao;
    @FXML private TableColumn<Upgrade, Integer> colNivelAtual;
    @FXML private TableColumn<Upgrade, Integer> colCustoProximo;

    private final GameContext context = GameContext.getInstance();
    private final UpgradeDAO upgradeDAO = new UpgradeDAO();
    private final ObservableList<Upgrade> upgradesList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColunas();
        carregarTabela();
    }

    private void configurarColunas() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colNivelAtual.setCellValueFactory(new PropertyValueFactory<>("nivelAtual"));
        colCustoProximo.setCellValueFactory(new PropertyValueFactory<>("custoProximoNivel"));
        tabelaUpgrades.setItems(upgradesList);
    }

    private void carregarTabela() {
        upgradesList.clear();
        Jogador j = context.getJogadorLogado();
        if (j != null) {
            lblPontosDisponiveis.setText(String.valueOf(j.getPontosReputacao()));
            List<Upgrade> lista = upgradeDAO.listarPorJogador(j.getId());
            upgradesList.addAll(lista);
        }
    }

    @FXML
    private void evoluirUpgradeSelecionado() {
        Upgrade selecionado = tabelaUpgrades.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            lblStatusUpgrade.setText("Selecione uma melhoria na tabela para evoluir.");
            return;
        }

        if (selecionado.isNivelMaximoAtingido()) {
            lblStatusUpgrade.setText("Esta melhoria já atingiu o nível máximo permitido!");
            return;
        }

        Jogador j = context.getJogadorLogado();
        int custo = selecionado.getCustoProximoNivel();

        if (j.gastarReputacao(custo)) {
            int novoNivel = selecionado.getNivelAtual() + 1;
            selecionado.setNivelAtual(novoNivel);
            upgradeDAO.salvarNivelUpgrade(j.getId(), selecionado.getId(), novoNivel);
            context.salvarProgressoJogador();
            context.recarregarUpgrades();

            lblStatusUpgrade.setText("Sucesso! " + selecionado.getNome() + " evoluído para o nível " + novoNivel + "!");
            carregarTabela();
        } else {
            lblStatusUpgrade.setText("Pontos de reputação insuficientes! Você precisa de " + custo + " pontos.");
        }
    }

    @FXML
    private void voltarAoMenu() throws IOException {
        App.setRoot("menu");
    }
}
