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
import org.example.eq5_proyectofinal.modelos.Clase;
import org.example.eq5_proyectofinal.ventana;

public class claseControler implements ventana {
    @FXML
    private TextField idText;

    @FXML
    private TextField nombreText;

    @FXML
    private TextField descripcionText;

    @FXML
    private TableView<Clase> tablaClases;

    @FXML
    private TableColumn<Clase, Integer> columnaId;

    @FXML
    private TableColumn<Clase, String> columnaNombre;

    @FXML
    private TableColumn<Clase, String> columnaDescripcion;

    private ManejadorFortniteDB manejadorProyectoDB;
    private ObservableList<Clase> listaObservable;
    @FXML
    private Parent rootClases;

    private String url;
    private String usuario;
    private String password;



    @Override
    public void initData(String url, String user, String password) {
        this.url = url;
        this.usuario = user;
        this.password = password;
        manejadorProyectoDB = new ManejadorFortniteDB(url, user, password);
        Platform.runLater(this::aplicarIcono);
        listaObservable = FXCollections.observableArrayList(manejadorProyectoDB.getClasePS());
        tablaClases.setItems(listaObservable);

        tablaClases.getSelectionModel().selectedItemProperty().addListener
                ((obs, oldSel, newSel) -> {
                    if (newSel != null) {
                        cargarSeleccionado(newSel);
                    }
                });
        limpiarForm();
    }



    @FXML
    public void initialize() {
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id_Clase"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre_Clase"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }


    //Metodo mixto para agregar y actualizar la clase segun se de el caso
    @FXML
    private void guardarClase() {
        String nombre = nombreText.getText();
        String descripcion = descripcionText.getText();

        if (nombre.isEmpty()) {
            mostrarAlerta("Debe ingresar un nombre", "Error", Alert.AlertType.ERROR);
            nombreText.requestFocus();
            return;
        }

        if (descripcion.isEmpty()) {
            mostrarAlerta("Debe ingresar una descripción", "Error", Alert.AlertType.ERROR);
            descripcionText.requestFocus();
            return;
        }

        int id = Integer.parseInt(idText.getText());

        Clase clase = new Clase(id, nombre, descripcion);

        int resultado;

        if (id == 0) {
            resultado = manejadorProyectoDB.insertarClasePS(clase);

            if (resultado > 0) {
                mostrarAlerta("Clase agregada correctamente.", "Correcto", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No se pudo agregar la clase.", "Error", Alert.AlertType.ERROR);
            }
        }else {//Significa que esta actualizando un arma existente

            resultado = manejadorProyectoDB.actualizarClasePS(clase);

            if (resultado > 0) {
                mostrarAlerta("Clase actualizada correctamente.", "Correcto", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No se pudo actualizar la clase.", "Error", Alert.AlertType.ERROR);
            }
        }

        recargarDatos();
    }


    //Metodo de eliminar clase
    @FXML
    private void eliminarClase() {
        String idClaseStr = idText.getText();
        if (idClaseStr.isEmpty() || idClaseStr.equals("0")) {
            mostrarAlerta("Debe seleccionar una Clase válida para eliminar.", "Aviso", Alert.AlertType.WARNING);
            return;
        }
        try {
            int idEliminar = Integer.parseInt(idClaseStr);
            int resultado = manejadorProyectoDB.eliminarClasePS(idEliminar);
            if (resultado > 0) {
                mostrarAlerta("Clase eliminada correctamente.", "Correcto", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No se pudo eliminar la Clase (ID no encontrado o error de BD).", "ERROR", Alert.AlertType.ERROR);
            }
            recargarDatos();
        } catch (NumberFormatException e) {
            mostrarAlerta("El ID debe ser un número válido.", "Error", Alert.AlertType.ERROR);
        }
    }


    private void cargarSeleccionado(Clase clase) {
        idText.setText(String.valueOf(clase.getId_Clase()));
        nombreText.setText(clase.getNombre_Clase());
        descripcionText.setText(clase.getDescripcion());

    }

    @FXML
    private void recargarDatos() {
        listaObservable.setAll(manejadorProyectoDB.getClasePS());
        limpiarForm();
    }


    private void limpiarForm() {
        idText.setText(String.valueOf(0));//Convertir el id de tipo int a String
        nombreText.clear();
        descripcionText.clear();

        //Deseleccionar el renglon seleccionado
        tablaClases.getSelectionModel().clearSelection();

    }

    private void mostrarAlerta(String mensaje, String titulo, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        //Obtener la ventana principal para centrar la alerta
        Stage stage = (Stage) tablaClases.getScene().getWindow();
        if (stage != null) {
            alerta.initOwner(stage);
        }

        //alerta.show();
        alerta.showAndWait();
    }


    //----------SECCION DE MENU---------------------
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
        Stage actualStage = (Stage) rootClases.getScene().getWindow();
        actualStage.close();
    }

    private void aplicarIcono() {
        Stage stage = (Stage) rootClases.getScene().getWindow();
        setIconoVentana(stage, "/imagesIcons/escritura.png");
    }

}






