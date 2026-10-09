package org.example.eq5_proyectofinal;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.eq5_proyectofinal.controlers.habilidadPersonajeController;


public class controladorVentanas {

    public static void abrirVentana(String ruta, String titulo,
                                    String url, String usuario, String password) {
        try {
            FXMLLoader loader = new FXMLLoader(controladorVentanas.class.getResource(ruta));
            Parent root = loader.load();

            Object controller = loader.getController();

            // Si el controlador implementa la interfaz "ventana", se le pasa la conexión
            if (controller instanceof ventana controladorConDatos) {
                controladorConDatos.initData(url, usuario, password);
            }

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.setResizable(false); // No redimensionable si quieres evitar problemas
            stage.show();

        } catch (Exception e) {
            System.err.println("Error al abrir ventana: " + ruta);
            e.printStackTrace();
        }
    }

    public static void abrirVentanaPersonajeHabilidades(
            String ruta, String titulo,
            String url, String usuario, String password,
            int idPersonaje, String nombrePersonaje
    ) {
        try {
            FXMLLoader loader = new FXMLLoader(controladorVentanas.class.getResource(ruta));
            Parent root = loader.load();

            Object controller = loader.getController();

            // Primero ejecutamos initData() si aplica
            if (controller instanceof ventana controladorConDatos) {
                controladorConDatos.initData(url, usuario, password);
            }

            // Luego enviamos los datos específicos de personaje
            if (controller instanceof habilidadPersonajeController hc) {
                hc.setPersonaje(idPersonaje, nombrePersonaje);
            }

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            System.err.println("Error al abrir ventana: " + ruta);
            e.printStackTrace();
        }
    }

}

