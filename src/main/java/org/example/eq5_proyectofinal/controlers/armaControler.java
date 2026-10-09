package org.example.eq5_proyectofinal.controlers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.eq5_proyectofinal.ManejadorFortniteDB;
import org.example.eq5_proyectofinal.controladorVentanas;
import org.example.eq5_proyectofinal.modelos.Arma;
import org.example.eq5_proyectofinal.ventana;

public class armaControler implements ventana {
    //declaracion de los componentes del form

    @FXML
    private TextField idText;
    @FXML
    private TextField nombreText;
    @FXML
    private TextField descripcionText;
    @FXML
    private TextField dañoText;


    //Pertenecientes a la tabla de armas
    @FXML
    private TableColumn<Arma, Integer> columnaId;
    @FXML
    private TableColumn<Arma, String> columnaNombre;
    @FXML
    private TableColumn<Arma, String> columnaDescipcion;
    @FXML
    private TableColumn<Arma, Integer> columnaDano;
    @FXML
    private TableView<Arma> tablaArmas;
    @FXML
    private Parent rootArmas;

    private String url;
    private String usuario;
    private String password;


    //Manejador y lista
    private ManejadorFortniteDB manejadorProyectoDB;
    private ObservableList<Arma> listaObservable;


    @Override
    public void initData(String url, String user, String password) {
        this.url = url;
        this.usuario = user;
        this.password = password;
        manejadorProyectoDB = new ManejadorFortniteDB(url, user, password);
        Platform.runLater(this::aplicarIcono);
        listaObservable = FXCollections.observableArrayList(manejadorProyectoDB.getArmasPS());
        tablaArmas.setItems(listaObservable);

        tablaArmas.getSelectionModel().selectedItemProperty().addListener
                ((obs, oldSel, newSel) -> {
                    if (newSel != null) {
                        cargarSeleccionado(newSel);
                    }
                });
        limpiarForm();
    }



    @FXML
    public void initialize() {
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id_Arma"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre_Arma"));
        columnaDescipcion.setCellValueFactory(new PropertyValueFactory<>("descripcion_Arma"));
        columnaDano.setCellValueFactory(new PropertyValueFactory<>("daño"));
        columnaDano.setText("Daño"); // Mantener el título de columna
    }


    //Metodo mixto para agregar y actualizar el arma segun se de el caso
    @FXML
    private void guardarArma() {
        String nombre = nombreText.getText();
        String descripcion = descripcionText.getText();
        String danioStr = dañoText.getText() ;


        if (nombre.isEmpty()) {
            mostrarAlerta("Debe ingresar un nombre", "Error", Alert.AlertType.ERROR);
            nombreText.requestFocus();
            return;


        } else if (descripcion.isEmpty()) {
            mostrarAlerta("Debe ingresar una descripcion", "Error", Alert.AlertType.ERROR);
            descripcionText.requestFocus();
            return;
        }

        int danio;

        try {
            danio = Integer.parseInt(danioStr);//Conversion a double el dato
        } catch (NumberFormatException e) {
            mostrarAlerta("El daño debe ser un numero valido", "Error", Alert.AlertType.ERROR);
            dañoText.requestFocus();
            return;
        }

        int idGuardar = Integer.parseInt(idText.getText());
        Arma arma = new Arma(idGuardar, nombre, descripcion, danio);

        int resultado;
        if (idGuardar == 0) {

            resultado = manejadorProyectoDB.insertarArmaPS(arma);

            if (resultado > 0) {//insert afecto uno o mas renglones
                mostrarAlerta("Arma agregada correctamente.", "Correcto", Alert.AlertType.INFORMATION);

            } else {
                mostrarAlerta("No se pudo agregar el Arma.", "Error", Alert.AlertType.ERROR);
            }

        }else {//Significa que esta actualizando un arma existente

            resultado = manejadorProyectoDB.actualizarArmaPS(arma);

            if (resultado > 0) {
                mostrarAlerta("Arma actualizada correctamente.", "Correcto", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No se pudo actualizar el Arma.", "Error", Alert.AlertType.ERROR);
            }
        }

        recargarDatos();
    }

    //Metodo de eliminar clase
    @FXML
    private void eliminarArma() {
        String idArmaStr = idText.getText();
        if (idArmaStr.equals("0") || idArmaStr.isEmpty()) {
            mostrarAlerta("Seleccione un arma válida para eliminar.", "Aviso", Alert.AlertType.INFORMATION);
            return;
        }
        try {
            int idEliminar = Integer.parseInt(idArmaStr);
            int resultado = manejadorProyectoDB.eliminarArmaPS(idEliminar);
            if (resultado > 0) {
                mostrarAlerta("Arma eliminada correctamente.", "Correcto", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No se pudo eliminar el arma o no fue encontrada.", "ERROR", Alert.AlertType.ERROR);
            }
            recargarDatos();
        } catch (NumberFormatException e) {
            mostrarAlerta("El ID debe ser un número válido.", "Error", Alert.AlertType.ERROR);
        }
    }


    private void cargarSeleccionado(Arma arma) {
        idText.setText(String.valueOf(arma.getId_Arma()));
        nombreText.setText(arma.getNombre_Arma());
        descripcionText.setText(arma.getDescripcion_Arma());
        dañoText.setText(String.valueOf(arma.getDaño()));

    }

    @FXML
    private void recargarDatos() {
        listaObservable.setAll(manejadorProyectoDB.getArmasPS());
        limpiarForm();
    }


    private void limpiarForm() {
        idText.setText(String.valueOf(0));//Convertir el id de tipo int a String
        nombreText.clear();
        descripcionText.clear();
        dañoText.clear();

        //Deseleccionar el renglon seleccionado
        tablaArmas.getSelectionModel().clearSelection();

    }

    private void mostrarAlerta(String mensaje, String titulo, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        //Obtener la ventana principal para centrar la alerta
        Stage stage = (Stage) tablaArmas.getScene().getWindow();
        if (stage != null) {
            alerta.initOwner(stage);
        }

        //alerta.show();
        alerta.showAndWait();
    }


    //-----------SECCION DE MENU--------------
    @FXML
    private void abrirPersonajes() {
        controladorVentanas.abrirVentana(
                "/org/example/eq5_proyectofinal/editPersonaje-view.fxml",
                "Personajes", url, usuario, password);
        cerrarVentana();
    }

    @FXML
    private void abrirArmas() {
        controladorVentanas.abrirVentana(
                "/org/example/eq5_proyectofinal/createArmas-view.fxml",
                "Armas", url, usuario, password);
        cerrarVentana();
    }

    @FXML
    private void abrirHabilidades() {
        controladorVentanas.abrirVentana(
                "/org/example/eq5_proyectofinal/habilidades-view.fxml",
                "Habilidades", url, usuario, password);
        cerrarVentana();
    }

    @FXML
    private void abrirClases() {
        controladorVentanas.abrirVentana(
                "/org/example/eq5_proyectofinal/clases-view.fxml",
                "Clases", url, usuario, password);
        cerrarVentana();

    }

    @FXML
    private void salir() {
        System.exit(0);
    }


    //Metodo para cerrar la ventana actual en caso de abrir cualquiera de los links
    public void cerrarVentana() {
        Stage actualStage = (Stage) rootArmas.getScene().getWindow();
        actualStage.close();
    }

    private void aplicarIcono() {
        Stage stage = (Stage) rootArmas.getScene().getWindow();
        setIconoVentana(stage, "/imagesIcons/armas.png");
    }


}
