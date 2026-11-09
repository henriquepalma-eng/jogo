package com.mycompany.mavenproject1.controller;

import com.mycompany.mavenproject1.App;
import com.mycompany.mavenproject1.model.Jogador;
import com.mycompany.mavenproject1.util.GameContext;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller da Tela de Menu Principal.
 * Atende aos requisitos da Seção 4.1 do Edital IFMS.
 */
public class MenuController implements Initializable {

    @FXML private Label lblNomeChef;
    @FXML private Label lblMoedas;
    @FXML private Label lblReputacao;
    @FXML private Label lblNivel;
    @FXML private TextField txtNomeChef;
    @FXML private Button btnBoss;

    private final GameContext context = GameContext.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        atualizarDadosInterface();
    }

    private void atualizarDadosInterface() {
        Jogador j = context.getJogadorLogado();
        if (j != null) {
            lblNomeChef.setText(j.getNome());
            lblMoedas.setText(String.valueOf(j.getMoedas()));
            lblReputacao.setText(String.valueOf(j.getPontosReputacao()));
            lblNivel.setText("Nível " + j.getNivel());
            txtNomeChef.setText(j.getNome());
        }

        // Habilita ou avisa sobre o Boss
        if (btnBoss != null) {
            boolean desbloqueado = context.isBossDesbloqueado();
            btnBoss.setDisable(!desbloqueado);
            if (desbloqueado) {
                btnBoss.setText("⭐ Desafio do Boss (Disponível!)");
                btnBoss.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold;");
            } else {
                btnBoss.setText("🔒 Boss: O Crítico (Desbloqueia no Nível 2)");
            }
        }
    }

    @FXML
    private void alterarNomeChef() {
        String novoNome = txtNomeChef.getText();
        if (novoNome != null && !novoNome.trim().isEmpty()) {
            context.inicializarJogador(novoNome.trim());
            atualizarDadosInterface();
        }
    }

    @FXML
    private void abrirGameplay() throws IOException {
        App.setRoot("gameplay");
    }

    @FXML
    private void abrirUpgrades() throws IOException {
        App.setRoot("upgrades");
    }

    @FXML
    private void abrirLoja() throws IOException {
        App.setRoot("loja");
    }

    @FXML
    private void abrirInventario() {
        App.abrirInventario();
    }

    @FXML
    private void abrirBoss() throws IOException {
        if (context.isBossDesbloqueado()) {
            App.setRoot("boss");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Boss Bloqueado");
            alert.setHeaderText("Ainda não é hora do Crítico!");
            alert.setContentText("Complete runs e acumule reputação para atingir o Nível 2 e desbloquear a visita do Crítico Gastronômico.");
            alert.showAndWait();
        }
    }

    @FXML
    private void abrirRanking() throws IOException {
        App.setRoot("ranking");
    }

    @FXML
    private void abrirCreditos() throws IOException {
        App.setRoot("creditos");
    }

    @FXML
    private void encerrarJogo() {
        context.salvarProgressoJogador();
        Platform.exit();
        System.exit(0);
    }
}
