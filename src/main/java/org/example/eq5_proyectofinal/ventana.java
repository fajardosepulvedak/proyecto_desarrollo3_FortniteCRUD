package org.example.eq5_proyectofinal;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public interface ventana{
    void initData(String url, String usuario, String password);
    default void setIconoVentana(Stage stage, String rutaIcono) {
        stage.getIcons().clear();
        stage.getIcons().add(new Image(
                getClass().getResourceAsStream(rutaIcono)
        ));
    }

}
