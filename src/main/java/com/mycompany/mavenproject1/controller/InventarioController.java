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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller do Inventário do Jogador.
 * Atende ao Requisito Obrigatório 5: "O inventário deverá abrir em uma nova janela (Stage separado)."
 */
public class InventarioController implements Initializable {

    @FXML private TableView<Item> tabelaInventario;
    @FXML private TableColumn<Item, String> colNome;
    @FXML private TableColumn<Item, String> colTipo;
    @FXML private TableColumn<Item, Integer> colQuantidade;
    @FXML private TableColumn<Item, String> colDescricao;

    @FXML private Label lblStatusInventario;
    @FXML private Button btnUsarItem;

    private final GameContext context = GameContext.getInstance();
    private final ItemDAO itemDAO = new ItemDAO();
    private final ObservableList<Item> itensList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColunas();
        carregarInventario();
    }

    private void configurarColunas() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tabelaInventario.setItems(itensList);
    }

    public void carregarInventario() {
        itensList.clear();
        Jogador j = context.getJogadorLogado();
        if (j != null) {
            List<Item> itens = itemDAO.listarInventarioJogador(j.getId());
            itensList.addAll(itens);
            lblStatusInventario.setText("Total de itens na despensa: " + itens.size());
        }
    }

    @FXML
    private void usarItemSelecionado() {
        Item selecionado = tabelaInventario.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            lblStatusInventario.setText("Selecione um item da tabela primeiro.");
            return;
        }

        Jogador j = context.getJogadorLogado();
        if (j != null && selecionado.getQuantidade() > 0) {
            if ("CONSUMIVEL".equalsIgnoreCase(selecionado.getTipo())) {
                itemDAO.consumirItemInventario(j.getId(), selecionado.getId());
                lblStatusInventario.setText("Efeito ativado: " + selecionado.getNome() + " utilizado!");
                carregarInventario();
            } else {
                lblStatusInventario.setText(selecionado.getNome() + " é um equipamento passivo e já está ativo!");
            }
        }
    }

    @FXML
    private void fecharJanela() {
        App.fecharInventario();
    }
}
