package org.example.eq5_proyectofinal.controlers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.eq5_proyectofinal.ManejadorFortniteDB;
import org.example.eq5_proyectofinal.controladorVentanas;
import org.example.eq5_proyectofinal.modelos.Habilidad;
import org.example.eq5_proyectofinal.ventana;

public class habilidadControler implements ventana {
    @FXML
    private TextField descripcionText;
    @FXML
    private TextField idTextH;
    @FXML
    private TextField nombreText;
    @FXML
    private TextField poderBaseText;


    //TABLA
    @FXML
    private TableView<Habilidad> tablaHabilidades;
    @FXML
    private TableColumn<Habilidad, Integer> columnaId;
    @FXML
    private TableColumn<Habilidad, String> columnaNombre;
    @FXML
    private TableColumn<Habilidad, String> columnaDescripcion;
    @FXML
    private TableColumn<Habilidad, Integer> columnaPoderBase;
    @FXML
    private Parent rootHabilidades;

    private String url;
    private String usuario;
    private String password;


    private ManejadorFortniteDB manejadorProyectoDB;
    private ObservableList<Habilidad> listaObservable;


    @Override
    public void initData(String url, String user, String password) {
        this.url = url;
        this.usuario = user;
        this.password = password;
        manejadorProyectoDB = new ManejadorFortniteDB(url, user, password);
        Platform.runLater(this::aplicarIcono);
        listaObservable = FXCollections.observableArrayList(manejadorProyectoDB.getHabilidadPS());
        tablaHabilidades.setItems(listaObservable);

        tablaHabilidades.getSelectionModel().selectedItemProperty().addListener
                ((obs, oldSel, newSel) -> {
                    if (newSel != null) {
                        cargarSeleccionado(newSel);
                    }
                });
        limpiarForm();
    }


    @FXML
    public void initialize() {
        //manejadorEmpleadosDB = new ManejadorEmpleadoDB("jdbc:mysql://localhost:3306/empresaDS3","DS3" , "ds3");
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id_Habilidad"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre_Habilidad"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaPoderBase.setCellValueFactory(new PropertyValueFactory<>("poder_Base"));
    }



    //Metodo mixto para agregar y actualizar el arma segun se de el caso
    @FXML
    private void guardarHabilidad() {
        String nombre = nombreText.getText();
        String descripcion = descripcionText.getText();
        String poderStr = poderBaseText.getText();


        if (nombre.isEmpty()) {
            mostrarAlerta("Debe ingresar un nombre.", "Error", Alert.AlertType.ERROR);
            nombreText.requestFocus();
            return;

        } else if (descripcion.isEmpty()) {
            mostrarAlerta("Debe ingresar una descripción.", "Error", Alert.AlertType.ERROR);
            descripcionText.requestFocus();
            return;
        }

        int poder;

        try {
            poder = Integer.parseInt(poderStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("El poder base debe ser un número válido.",
                    "Error", Alert.AlertType.ERROR);
            poderBaseText.requestFocus();
            return;
        }


        int idGuardar = Integer.parseInt(idTextH.getText());
        Habilidad habilidad = new Habilidad(idGuardar, nombre, descripcion, poder);

        int resultado;

        if (idGuardar == 0) {

            resultado = manejadorProyectoDB.insertarHabilidadPS(habilidad);

            if (resultado > 0) {
                mostrarAlerta("Habilidad agregada correctamente.",
                        "Correcto", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No se pudo agregar la habilidad.",
                        "Error", Alert.AlertType.ERROR);
            }

        }else {//Significa que esta actualizando una habilidad existente

            resultado = manejadorProyectoDB.actualizarHabilidadPS(habilidad);

            if (resultado > 0) {
                mostrarAlerta("Habilidad actualizada correctamente.", "Correcto", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No se pudo actualizar la Habilidad.", "Error", Alert.AlertType.ERROR);
            }
        }
        recargarDatos();
    }


    //Metodo para eliminar la habilidad
    @FXML
    private void eliminarHabilidad() {
        //TODO implementar metodo - Axel
        String idHabilidadStr = idTextH.getText();
        if (idHabilidadStr.isEmpty() || idHabilidadStr.equals("0")) {
            mostrarAlerta("Debe seleccionar una Habilidad válida para eliminar.", "Aviso", Alert.AlertType.WARNING);
            return;
        }
        try {
            int idEliminar = Integer.parseInt(idHabilidadStr);
            int resultado = manejadorProyectoDB.eliminarHabilidadPS(idEliminar);
            if (resultado > 0) {
                mostrarAlerta("Habilidad eliminada correctamente.", "Correcto", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No se pudo eliminar la Habilidad (ID no encontrado o error de BD).", "ERROR", Alert.AlertType.ERROR);
            }
            recargarDatos();
        } catch (NumberFormatException e) {
            mostrarAlerta("El ID debe ser un número válido.", "Error", Alert.AlertType.ERROR);
        }
    }


    private void cargarSeleccionado(Habilidad habilidad) {
        idTextH.setText(String.valueOf(habilidad.getId_Habilidad()));
        nombreText.setText(habilidad.getNombre_Habilidad());
        descripcionText.setText(habilidad.getDescripcion());
        poderBaseText.setText(String.valueOf(habilidad.getPoder_Base()));

    }

    @FXML
    private void recargarDatos() {
        listaObservable.setAll(manejadorProyectoDB.getHabilidadPS());
        limpiarForm();
    }


    private void limpiarForm() {
        idTextH.setText(String.valueOf(0));//Convertir el id de tipo int a String
        nombreText.clear();
        descripcionText.clear();
        poderBaseText.setText(String.valueOf(0));

        //Deseleccionar el renglon seleccionado
        tablaHabilidades.getSelectionModel().clearSelection();

    }

    private void mostrarAlerta(String mensaje, String titulo, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        //Obtener la ventana principal para centrar la alerta
        Stage stage = (Stage) tablaHabilidades.getScene().getWindow();
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
        Stage actualStage = (Stage) rootHabilidades.getScene().getWindow();
        actualStage.close();
    }

    private void aplicarIcono() {
        Stage stage = (Stage) rootHabilidades.getScene().getWindow();
        setIconoVentana(stage, "/imagesIcons/habilidades.png");
    }

}


