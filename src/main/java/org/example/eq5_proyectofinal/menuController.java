package org.example.eq5_proyectofinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.eq5_proyectofinal.controlers.armaControler;
import org.example.eq5_proyectofinal.controlers.claseControler;
import org.example.eq5_proyectofinal.controlers.habilidadControler;
import org.example.eq5_proyectofinal.controlers.personajeController;

public class menuController extends controladorVentanas {

    private String url;
    private String usuario;
    private String password;

    @FXML
    private Parent rootMenu;

    // Recibe la conexión desde LoginController
    public void setConexion(String url, String usuario, String password) {
        this.url = url;
        this.usuario = usuario;
        this.password = password;
    }

    @FXML
    private void abrirPersonajes() {
        abrirVentanaConConexion(
                "/org/example/eq5_proyectofinal/editPersonaje-view.fxml",
                "Gestión de Personajes",
                personajeController.class
        );
    }

    @FXML
    private void abrirArmas() {
        abrirVentanaConConexion(
                "/org/example/eq5_proyectofinal/createArmas-view.fxml",
                "Gestión de Armas",
                armaControler.class
        );
    }

    @FXML
    private void abrirHabilidades() {
        abrirVentanaConConexion(
                "/org/example/eq5_proyectofinal/habilidades-view.fxml",
                "Gestión de Habilidades",
                habilidadControler.class
        );
    }

    @FXML
    private void abrirClases() {
        abrirVentanaConConexion(
                "/org/example/eq5_proyectofinal/clases-view.fxml",
                "Gestión de Clases",
                claseControler.class
        );
    }

    @FXML
    private void salir() {
        System.exit(0);
    }


    private <T> void abrirVentanaConConexion(String rutaFXML, String titulo, Class<T> controllerClass) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            T controller = loader.getController();

            // Intentar llamar initData(url, usuario, password) o setConexion(url, usuario, password)
            try {
                controllerClass.getMethod("initData", String.class, String.class, String.class)
                        .invoke(controller, url, usuario, password);
            } catch (NoSuchMethodException e1) {
                try {
                    controllerClass.getMethod("setConexion", String.class, String.class, String.class)
                            .invoke(controller, url, usuario, password);
                } catch (NoSuchMethodException ignored) {
                }
            }

            // Abrir nueva ventana
            Stage newStage = new Stage();
            newStage.setTitle(titulo);
            newStage.setScene(new Scene(root));
            newStage.show();

            Stage actualStage = (Stage) rootMenu.getScene().getWindow();
            actualStage.close();

        } catch (Exception e) {
            mostrarAlerta("No se pudo abrir la ventana: " + titulo + "\nError: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
