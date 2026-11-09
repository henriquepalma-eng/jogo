package com.mycompany.mavenproject1.controller;

import com.mycompany.mavenproject1.App;
import com.mycompany.mavenproject1.dao.PartidaDAO;
import com.mycompany.mavenproject1.model.Jogador;
import com.mycompany.mavenproject1.model.Partida;
import com.mycompany.mavenproject1.model.Pedido;
import com.mycompany.mavenproject1.util.GameContext;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

/**
 * Controller da Tela de Boss / Finalização.
 * Atende aos requisitos da Seção 4.5 do Edital IFMS.
 * Desafio: O Grande Crítico Gastronômico Anton Gourmet.
 */
public class BossController implements Initializable {

    @FXML private Label lblFaseBoss;
    @FXML private Label lblTempoBoss;
    @FXML private Label lblNomePratoCritico;
    @FXML private Label lblIngredientesCritico;
    @FXML private ProgressBar barPacienciaBoss;
    @FXML private Label lblMensagemCritico;

    @FXML private ListView<String> listPratoMontado;
    @FXML private VBox painelVitoria;
    @FXML private VBox painelDerrota;

    private final GameContext context = GameContext.getInstance();
    private final PartidaDAO partidaDAO = new PartidaDAO();

    private Timeline bossLoop;
    private double tempoRestante = 45.0;
    private final double tempoMaximo = 45.0;
    private int etapaAtual = 1; // 3 etapas para derrotar o Boss

    private final List<String> pratoMontado = new ArrayList<>();
    private final List<Pedido> etapasBoss = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarDesafioBoss();
        iniciarBatalhaBoss();
    }

    private void configurarDesafioBoss() {
        etapasBoss.clear();
        // Etapa 1: Entrada refinada
        etapasBoss.add(new Pedido("1. Salada Gourmet Real", Arrays.asList("Alface", "Tomate", "Queijo", "Molho"), 100, 300, 35));
        // Etapa 2: Prato Principal Máximo
        etapasBoss.add(new Pedido("2. Grand Burger Imperial", Arrays.asList("Pão", "Hambúrguer", "Hambúrguer", "Queijo", "Tomate", "Molho"), 200, 500, 40));
        // Etapa 3: Banquete de Gala
        etapasBoss.add(new Pedido("3. Banquete Supremo", Arrays.asList("Pão", "Hambúrguer", "Queijo", "Batata Frita", "Refrigerante"), 300, 800, 45));
    }

    private void iniciarBatalhaBoss() {
        etapaAtual = 1;
        painelVitoria.setVisible(false);
        painelDerrota.setVisible(false);
        carregarEtapa(etapaAtual);

        if (bossLoop != null) bossLoop.stop();
        bossLoop = new Timeline(new KeyFrame(Duration.millis(500), e -> tickBoss()));
        bossLoop.setCycleCount(Timeline.INDEFINITE);
        bossLoop.play();
    }

    private void carregarEtapa(int etapa) {
        Pedido pedido = etapasBoss.get(etapa - 1);
        lblFaseBoss.setText("ETAPA " + etapa + " DE 3: AVALIAÇÃO DO CRÍTICO");
        lblNomePratoCritico.setText(pedido.getNomePrato());
        lblIngredientesCritico.setText("Ingredientes Exigidos: " + String.join(" + ", pedido.getIngredientes()));
        tempoRestante = pedido.getTempoMaximo();
        lblMensagemCritico.setText("O Crítico está aguardando com olhar severo... Não erre!");
        limparBancada();
    }

    private void tickBoss() {
        tempoRestante -= 0.5;
        lblTempoBoss.setText(String.format("%.0fs", Math.max(0, tempoRestante)));
        double progresso = Math.max(0, tempoRestante / etapasBoss.get(etapaAtual - 1).getTempoMaximo());
        barPacienciaBoss.setProgress(progresso);

        if (progresso < 0.25) {
            barPacienciaBoss.setStyle("-fx-accent: #e74c3c;");
            lblMensagemCritico.setText("O Crítico está prestes a reprovar seu restaurante no guia!");
        } else {
            barPacienciaBoss.setStyle("-fx-accent: #e67e22;");
        }

        if (tempoRestante <= 0) {
            derrotaBoss();
        }
    }

    @FXML
    private void adicionarIngrediente(javafx.event.ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button btn = (Button) event.getSource();
            String ingrediente = btn.getText();
            pratoMontado.add(ingrediente);
            listPratoMontado.getItems().add("✨ " + ingrediente);
        }
    }

    @FXML
    private void limparBancada() {
        pratoMontado.clear();
        listPratoMontado.getItems().clear();
    }

    @FXML
    private void servirAoCritico() {
        Pedido pedidoAtual = etapasBoss.get(etapaAtual - 1);
        if (pedidoAtual.verificarReceita(pratoMontado)) {
            if (etapaAtual < 3) {
                etapaAtual++;
                lblMensagemCritico.setText("Prato aceito! O Crítico assentiu com a cabeça... Próximo pedido!");
                carregarEtapa(etapaAtual);
            } else {
                vitoriaBoss();
            }
        } else {
            lblMensagemCritico.setText("❌ O Crítico rejeitou seu prato! Receita incorreta! Corrija rápido!");
        }
    }

    private void vitoriaBoss() {
        if (bossLoop != null) bossLoop.stop();
        Jogador j = context.getJogadorLogado();
        if (j != null) {
            j.adicionarMoedas(500);
            j.adicionarReputacao(800);
            context.salvarProgressoJogador();

            Partida p = new Partida(0, j.getId(), j.getNome(), 2500, 3, 500, true, new Timestamp(System.currentTimeMillis()));
            partidaDAO.salvar(p);
        }
        painelVitoria.setVisible(true);
    }

    private void derrotaBoss() {
        if (bossLoop != null) bossLoop.stop();
        painelDerrota.setVisible(true);
    }

    @FXML
    private void tentarNovamente() {
        iniciarBatalhaBoss();
    }

    @FXML
    private void voltarAoMenu() throws IOException {
        if (bossLoop != null) bossLoop.stop();
        App.setRoot("menu");
    }
}
