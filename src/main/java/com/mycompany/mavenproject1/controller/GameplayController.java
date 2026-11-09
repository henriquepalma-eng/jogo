package com.mycompany.mavenproject1.controller;

import com.mycompany.mavenproject1.App;
import com.mycompany.mavenproject1.dao.PartidaDAO;
import com.mycompany.mavenproject1.model.Cliente;
import com.mycompany.mavenproject1.model.Jogador;
import com.mycompany.mavenproject1.model.Partida;
import com.mycompany.mavenproject1.model.Pedido;
import com.mycompany.mavenproject1.util.GameContext;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
 * Controller da Tela de Gameplay (Ciclo da Run).
 * Atende aos requisitos da Seção 4.2 do Edital IFMS.
 */
public class GameplayController implements Initializable {

    @FXML private Label lblTempo;
    @FXML private Label lblPontuacao;
    @FXML private Label lblMoedasRun;
    @FXML private Label lblClientesAtendidos;
    @FXML private Label lblMensagemStatus;

    // Clientes no Balcão
    @FXML private VBox boxCliente1;
    @FXML private Label lblNomeCliente1;
    @FXML private Label lblPedidoCliente1;
    @FXML private ProgressBar barPaciencia1;
    @FXML private Button btnAtenderCliente1;

    @FXML private VBox boxCliente2;
    @FXML private Label lblNomeCliente2;
    @FXML private Label lblPedidoCliente2;
    @FXML private ProgressBar barPaciencia2;
    @FXML private Button btnAtenderCliente2;

    @FXML private VBox boxCliente3;
    @FXML private Label lblNomeCliente3;
    @FXML private Label lblPedidoCliente3;
    @FXML private ProgressBar barPaciencia3;
    @FXML private Button btnAtenderCliente3;

    // Bancada de Montagem
    @FXML private ListView<String> listPratoMontado;
    @FXML private Button btnLimparPrato;

    // Painel de Fim de Run
    @FXML private VBox painelFimRun;
    @FXML private Label lblResumoTitulo;
    @FXML private Label lblResumoPontos;
    @FXML private Label lblResumoMoedas;
    @FXML private Label lblResumoClientes;

    private final GameContext context = GameContext.getInstance();
    private final PartidaDAO partidaDAO = new PartidaDAO();

    private Timeline gameLoop;
    private int tempoRestanteSegundos = 60;
    private int pontuacaoTotal = 0;
    private int moedasGanhasRun = 0;
    private int clientesAtendidosCount = 0;

    private final List<String> pratoAtual = new ArrayList<>();
    private final Cliente[] slotsClientes = new Cliente[3];
    private final Random random = new Random();

    // Upgrades calculados
    private double bonusVelocidade = 1.0;
    private double bonusPaciencia = 1.0;
    private double bonusGorjeta = 1.0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarEfeitosUpgrades();
        iniciarNovaRun();
    }

    private void carregarEfeitosUpgrades() {
        int nivelFogao = context.getNivelUpgrade("fogao_rapido");
        int nivelPaciencia = context.getNivelUpgrade("paciencia_cliente");
        int nivelGorjeta = context.getNivelUpgrade("gorjeta_extra");

        bonusVelocidade = 1.0 + (nivelFogao * 0.15);
        bonusPaciencia = 1.0 + (nivelPaciencia * 0.20);
        bonusGorjeta = 1.0 + (nivelGorjeta * 0.25);
    }

    private void iniciarNovaRun() {
        tempoRestanteSegundos = 60;
        pontuacaoTotal = 0;
        moedasGanhasRun = 0;
        clientesAtendidosCount = 0;
        pratoAtual.clear();
        listPratoMontado.getItems().clear();
        painelFimRun.setVisible(false);
        lblMensagemStatus.setText("Prepare os pedidos antes que a paciência do cliente acabe!");

        // Inicializar clientes nos slots
        for (int i = 0; i < 3; i++) {
            gerarNovoCliente(i);
        }
        atualizarVisualClientes();

        // Iniciar Loop do Jogo (1 atualização a cada 0.5s)
        if (gameLoop != null) gameLoop.stop();
        gameLoop = new Timeline(new KeyFrame(Duration.millis(500), e -> atualizarTick()));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    private void atualizarTick() {
        // Decrementa paciência dos clientes
        double delta = 0.5;
        for (int i = 0; i < 3; i++) {
            Cliente c = slotsClientes[i];
            if (c != null && !c.isAtendido() && !c.isDesistiu()) {
                c.decrementarPaciencia(delta);
                if (c.isDesistiu()) {
                    lblMensagemStatus.setText("Um cliente cansou de esperar e foi embora!");
                    gerarNovoCliente(i);
                }
            }
        }

        // Atualiza timer da run a cada 2 ticks (1 segundo)
        if (System.currentTimeMillis() % 1000 < 550) {
            tempoRestanteSegundos--;
            lblTempo.setText(tempoRestanteSegundos + "s");
            if (tempoRestanteSegundos <= 0) {
                finalizarRun();
            }
        }

        atualizarVisualClientes();
        atualizarPlacar();
    }

    private void gerarNovoCliente(int slot) {
        List<Pedido> cardapio = criarCardapio();
        Pedido pedidoEscolhido = cardapio.get(random.nextInt(cardapio.size()));

        String[] nomes = {"Cliente Apressado", "Estudante Faminto", "Gourmet Exigente", "Turista Curioso", "Trabalhador no Almoço"};
        String nome = nomes[random.nextInt(nomes.length)];

        double pacienciaBase = pedidoEscolhido.getTempoMaximo() * bonusPaciencia;
        slotsClientes[slot] = new Cliente(nome, "🧑‍🍳", pedidoEscolhido, pacienciaBase);
    }

    private List<Pedido> criarCardapio() {
        List<Pedido> lista = new ArrayList<>();
        lista.add(new Pedido("Hambúrguer Simples", Arrays.asList("Pão", "Hambúrguer", "Alface"), 20, 50, 18));
        lista.add(new Pedido("Cheeseburger Completo", Arrays.asList("Pão", "Hambúrguer", "Queijo", "Tomate"), 30, 80, 20));
        lista.add(new Pedido("Combo Duplo", Arrays.asList("Pão", "Hambúrguer", "Queijo", "Batata Frita"), 40, 100, 22));
        lista.add(new Pedido("Super Lanche Mestre", Arrays.asList("Pão", "Hambúrguer", "Queijo", "Alface", "Tomate", "Molho"), 55, 150, 25));
        lista.add(new Pedido("Batata & Refrigerante", Arrays.asList("Batata Frita", "Refrigerante"), 18, 40, 15));
        return lista;
    }

    private void atualizarVisualClientes() {
        atualizarSlotVisual(boxCliente1, lblNomeCliente1, lblPedidoCliente1, barPaciencia1, slotsClientes[0]);
        atualizarSlotVisual(boxCliente2, lblNomeCliente2, lblPedidoCliente2, barPaciencia2, slotsClientes[1]);
        atualizarSlotVisual(boxCliente3, lblNomeCliente3, lblPedidoCliente3, barPaciencia3, slotsClientes[2]);
    }

    private void atualizarSlotVisual(VBox box, Label lblNome, Label lblPedido, ProgressBar bar, Cliente c) {
        if (c != null) {
            box.setDisable(false);
            lblNome.setText(c.getNome());
            lblPedido.setText(c.getPedido().getNomePrato() + "\n" + String.join(" + ", c.getPedido().getIngredientes()));
            double progresso = c.getProgressoPaciencia();
            bar.setProgress(progresso);

            if (progresso > 0.6) {
                bar.setStyle("-fx-accent: #2ecc71;"); // Verde
            } else if (progresso > 0.3) {
                bar.setStyle("-fx-accent: #f39c12;"); // Laranja
            } else {
                bar.setStyle("-fx-accent: #e74c3c;"); // Vermelho
            }
        }
    }

    private void atualizarPlacar() {
        lblPontuacao.setText(String.valueOf(pontuacaoTotal));
        lblMoedasRun.setText(String.valueOf(moedasGanhasRun));
        lblClientesAtendidos.setText(String.valueOf(clientesAtendidosCount));
    }

    @FXML
    private void adicionarIngrediente(javafx.event.ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button btn = (Button) event.getSource();
            String ingrediente = btn.getText();
            pratoAtual.add(ingrediente);
            listPratoMontado.getItems().add("➕ " + ingrediente);
        }
    }

    @FXML
    private void limparPrato() {
        pratoAtual.clear();
        listPratoMontado.getItems().clear();
        lblMensagemStatus.setText("Prato limpo! Monte os ingredientes da receita.");
    }

    @FXML
    private void tentarEntregarSlot1() { entregarPedido(0); }
    @FXML
    private void tentarEntregarSlot2() { entregarPedido(1); }
    @FXML
    private void tentarEntregarSlot3() { entregarPedido(2); }

    private void entregarPedido(int slot) {
        Cliente c = slotsClientes[slot];
        if (c == null) return;

        if (c.getPedido().verificarReceita(pratoAtual)) {
            // Sucesso!
            int moedas = (int) (c.getPedido().getRecompensaMoedas() * bonusGorjeta);
            int pontos = (int) (c.getPedido().getRecompensaPontos() * bonusVelocidade);

            moedasGanhasRun += moedas;
            pontuacaoTotal += pontos;
            clientesAtendidosCount++;

            lblMensagemStatus.setText("✅ Sucesso! Pedido de " + c.getNome() + " entregue com perfeição! (+" + moedas + " Moedas)");
            limparPrato();
            gerarNovoCliente(slot);
            atualizarPlacar();
        } else {
            lblMensagemStatus.setText("❌ Ops! A receita que você montou não combina com o pedido desse cliente!");
        }
    }

    private void finalizarRun() {
        if (gameLoop != null) gameLoop.stop();

        Jogador jogador = context.getJogadorLogado();
        if (jogador != null) {
            jogador.adicionarMoedas(moedasGanhasRun);
            jogador.adicionarReputacao(pontuacaoTotal / 10);
            context.salvarProgressoJogador();

            // Salvar no banco PostgreSQL
            Partida partida = new Partida(0, jogador.getId(), jogador.getNome(), pontuacaoTotal, clientesAtendidosCount, moedasGanhasRun, false, new Timestamp(System.currentTimeMillis()));
            partidaDAO.salvar(partida);
        }

        // Exibir painel de resultados
        lblResumoTitulo.setText("FIM DO TURNO DE TRABALHO!");
        lblResumoPontos.setText("Pontuação: " + pontuacaoTotal);
        lblResumoMoedas.setText("Moedas Ganhas: +" + moedasGanhasRun);
        lblResumoClientes.setText("Clientes Atendidos: " + clientesAtendidosCount);
        painelFimRun.setVisible(true);
    }

    @FXML
    private void jogarNovamente() {
        iniciarNovaRun();
    }

    @FXML
    private void irParaUpgrades() throws IOException {
        if (gameLoop != null) gameLoop.stop();
        App.setRoot("upgrades");
    }

    @FXML
    private void voltarAoMenu() throws IOException {
        if (gameLoop != null) gameLoop.stop();
        App.setRoot("menu");
    }

    @FXML
    private void abrirInventario() {
        App.abrirInventario();
    }
}
