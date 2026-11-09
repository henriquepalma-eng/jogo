package com.mycompany.mavenproject1.controller;

import com.mycompany.mavenproject1.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller da Tela de Créditos.
 * Atende aos requisitos da Seção 4.6 do Edital IFMS.
 */
public class CreditosController implements Initializable {

    @FXML private Label lblNomeJogo;
    @FXML private Label lblDesenvolvedor;
    @FXML private Label lblDisciplina;
    @FXML private Label lblInstituicao;
    @FXML private Label lblTecnologias;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNomeJogo.setText("Mestre da Cozinha");
        lblDesenvolvedor.setText("Henrique Jara Palma");
        lblDisciplina.setText("Tópicos em Linguagem de Programação 2");
        lblInstituicao.setText("IFMS - Instituto Federal de Mato Grosso do Sul");
        lblTecnologias.setText("Java 21, JavaFX 21, FXML, PostgreSQL 16, JDBC, Padrão MVC e DAO");
    }

    @FXML
    private void voltarAoMenu() throws IOException {
        App.setRoot("menu");
    }
}
