package org.example.eq5_proyectofinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.eq5_proyectofinal.controlers.personajeController;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usuarioText;
    @FXML
    private TextField urlText;
    @FXML
    private PasswordField passwordText;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void ingresar() {
        String usuario = usuarioText.getText();
        String password = passwordText.getText();
        String url = urlText.getText();

//        if (usuario == null || usuario.trim().isEmpty() ||
//                password == null || password.trim().isEmpty() ||
//                url == null || url.trim().isEmpty()) {
//            mostrarAlerta("Por favor, ingresa tu usuario, contraseña y URL.",
//                    "Campos Vacíos", Alert.AlertType.WARNING);
//            return;
//        }

        try {
            // Probar la conexión con la base de datos
            ManejadorFortniteDB manejador = new ManejadorFortniteDB(url, usuario, password);
            if (!manejador.probarConexion()) {
                mostrarAlerta("No se pudo establecer conexión con la base de datos.\n" +
                                "Verifica la red o los datos de conexión.",
                        "Error de Conexión", Alert.AlertType.ERROR);
                return;
            }

            // Cargar el FXML del Menú Principal
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("editPersonaje-view.fxml"));
            Scene menuScene = new Scene(fxmlLoader.load());

            // Obtener el controlador y pasar la conexión
            personajeController personajeController = fxmlLoader.getController();
            personajeController.initData(url, usuario, password);

            // Reutilizar el Stage actual para mostrar el Menú Principal
            primaryStage.setTitle("Menú Principal");
            primaryStage.setScene(menuScene);
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir la ventana del Menú Principal.",
                    "Error", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String mensaje, String titulo, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        // Centrar la alerta sobre la ventana principal
        if (primaryStage != null) {
            alerta.initOwner(primaryStage);
        }

        alerta.showAndWait();
    }


}
