package com.mycompany.mavenproject1.controller;

import com.mycompany.mavenproject1.App;
import com.mycompany.mavenproject1.dao.PartidaDAO;
import com.mycompany.mavenproject1.model.Partida;
import com.mycompany.mavenproject1.util.ExportadorRanking;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller da Tela de Ranking Oficial.
 * Atende aos requisitos da Seção 8 do Edital IFMS (Consulta ao banco e Exportação CSV/TXT).
 */
public class RankingController implements Initializable {

    @FXML private TableView<Partida> tabelaRanking;
    @FXML private TableColumn<Partida, Integer> colPosicao;
    @FXML private TableColumn<Partida, String> colJogador;
    @FXML private TableColumn<Partida, Integer> colPontuacao;
    @FXML private TableColumn<Partida, Integer> colClientes;
    @FXML private TableColumn<Partida, Integer> colMoedas;
    @FXML private TableColumn<Partida, String> colDerrotouBoss;
    @FXML private TableColumn<Partida, String> colData;

    @FXML private Label lblStatusExportacao;

    private final PartidaDAO partidaDAO = new PartidaDAO();
    private final ObservableList<Partida> rankingList = FXCollections.observableArrayList();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColunas();
        carregarRanking();
    }

    private void configurarColunas() {
        colJogador.setCellValueFactory(new PropertyValueFactory<>("nomeJogador"));
        colPontuacao.setCellValueFactory(new PropertyValueFactory<>("pontuacao"));
        colClientes.setCellValueFactory(new PropertyValueFactory<>("clientesAtendidos"));
        colMoedas.setCellValueFactory(new PropertyValueFactory<>("moedasGanhas"));

        colDerrotouBoss.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isDerrotouBoss() ? "⭐ Venceu" : "Não"));

        colData.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDataPartida() != null) {
                return new SimpleStringProperty(sdf.format(cellData.getValue().getDataPartida()));
            }
            return new SimpleStringProperty("-");
        });

        tabelaRanking.setItems(rankingList);
    }

    @FXML
    public void carregarRanking() {
        rankingList.clear();
        List<Partida> partidas = partidaDAO.listarRanking(50);
        rankingList.addAll(partidas);
        lblStatusExportacao.setText("Total de partidas registradas no banco: " + partidas.size());
    }

    @FXML
    private void exportarCSV() {
        if (rankingList.isEmpty()) {
            lblStatusExportacao.setText("Não há registros para exportar.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Ranking em CSV");
        fileChooser.setInitialFileName("ranking_mestre_da_cozinha.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo CSV (*.csv)", "*.csv"));

        File file = fileChooser.showSaveDialog(App.getPrimaryStage());
        if (file != null) {
            try {
                ExportadorRanking.exportarCSV(file, rankingList);
                lblStatusExportacao.setText("Arquivo CSV exportado com sucesso: " + file.getName());
            } catch (IOException e) {
                lblStatusExportacao.setText("Erro ao exportar CSV: " + e.getMessage());
            }
        }
    }

    @FXML
    private void exportarTXT() {
        if (rankingList.isEmpty()) {
            lblStatusExportacao.setText("Não há registros para exportar.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Ranking em TXT");
        fileChooser.setInitialFileName("ranking_mestre_da_cozinha.txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo de Texto (*.txt)", "*.txt"));

        File file = fileChooser.showSaveDialog(App.getPrimaryStage());
        if (file != null) {
            try {
                ExportadorRanking.exportarTXT(file, rankingList);
                lblStatusExportacao.setText("Arquivo TXT exportado com sucesso: " + file.getName());
            } catch (IOException e) {
                lblStatusExportacao.setText("Erro ao exportar TXT: " + e.getMessage());
            }
        }
    }

    @FXML
    private void voltarAoMenu() throws IOException {
        App.setRoot("menu");
    }
}
