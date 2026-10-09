package org.example.eq5_proyectofinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Cargar FXML del login
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500); // Ajuste de tamaño

        primaryStage.setTitle("Login");
        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("hongos.png")));
        primaryStage.setScene(scene);
        primaryStage.show();

        // Obtener el controlador y pasarle la referencia del stage
        LoginController controller = fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);
    }

    public static void main(String[] args) {
        launch();
    }
}