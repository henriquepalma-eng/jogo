package com.mycompany.mavenproject1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Classe Principal da Aplicação JavaFX.
 * Jogo: Mestre da Cozinha
 * Autor: Henrique Jara Palma
 * Disciplina: Tópicos em Linguagem de Programação 2
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;
    private static Stage inventarioStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setTitle("Mestre da Cozinha - IFMS");
        scene = new Scene(loadFXML("menu"), 950, 680);

        // Aplica o tema CSS se existir
        String css = App.class.getResource("styles.css") != null ? App.class.getResource("styles.css").toExternalForm() : null;
        if (css != null) {
            scene.getStylesheets().add(css);
        }

        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(640);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Requisito Obrigatório 5:
     * "O inventário deverá abrir em uma nova janela (Stage separado)."
     */
    public static void abrirInventario() {
        try {
            if (inventarioStage == null) {
                inventarioStage = new Stage();
                inventarioStage.setTitle("Despensa e Inventário do Chef");
                inventarioStage.initModality(Modality.APPLICATION_MODAL);
                inventarioStage.initOwner(primaryStage);
                Scene invScene = new Scene(loadFXML("inventario"), 650, 480);
                String css = App.class.getResource("styles.css") != null ? App.class.getResource("styles.css").toExternalForm() : null;
                if (css != null) {
                    invScene.getStylesheets().add(css);
                }
                inventarioStage.setScene(invScene);
                inventarioStage.setOnCloseRequest(e -> inventarioStage = null);
            }
            inventarioStage.show();
            inventarioStage.toFront();
        } catch (IOException e) {
            System.err.println("Erro ao abrir tela de inventário em novo Stage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void fecharInventario() {
        if (inventarioStage != null) {
            inventarioStage.close();
            inventarioStage = null;
        }
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}
