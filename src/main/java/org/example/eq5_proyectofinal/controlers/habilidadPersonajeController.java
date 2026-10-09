package org.example.eq5_proyectofinal.controlers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.eq5_proyectofinal.ManejadorFortniteDB;
import org.example.eq5_proyectofinal.controladorVentanas;
import org.example.eq5_proyectofinal.modelos.Habilidad;
import org.example.eq5_proyectofinal.modelos.HabilidadPersonaje;
import org.example.eq5_proyectofinal.ventana;

public class habilidadPersonajeController implements ventana {

    @FXML
    private Label tituloPersonaje;

    @FXML
    private Label cabeceraPersonaje;

    @FXML
    private TableView<HabilidadPersonaje> tablaHabilidadesPersonaje;

    @FXML
    private TableColumn<HabilidadPersonaje, String> columnaHabilidad;

    @FXML
    private TableColumn<HabilidadPersonaje, Integer> columnaNivel;

    @FXML
    private ComboBox<Habilidad> habilidadComboBox;

    @FXML
    private TextField nivelText;

    private int idPersonaje;
    private String nombrePersonaje;

    @FXML
    private Parent rootHP;

    private String url;
    private String usuario;
    private String password;

    private ManejadorFortniteDB manejadorDB;
    private ObservableList<HabilidadPersonaje> listaObservable;
    private ObservableList<Habilidad> listaHabilidadesDisponibles;

    @Override
    public void initData(String url, String user, String password) {
        this.url = url;
        this.usuario = user;
        this.password = password;

        manejadorDB = new ManejadorFortniteDB(url, user, password);
        Platform.runLater(this::aplicarIcono);

        // Carga solo si ya existe personaje
        if (idPersonaje > 0) {
            actualizarTitulo();
            cargarCabecera(idPersonaje, nombrePersonaje);
            cargarHabilidadesPersonaje();
        }

        cargarListaHabilidades(); // Siempre se puede cargar
    }


    //Reibe personaje y actualiza la vista
    public void setPersonaje(int idPersonaje, String nombre) {
        this.idPersonaje = idPersonaje;
        this.nombrePersonaje = nombre;

        cargarCabecera(idPersonaje, nombre);
        actualizarTitulo();
        cargarHabilidadesPersonaje();
    }

    @FXML
    public void cargarCabecera(int id, String nombre) {
        cabeceraPersonaje.setText("Personaje " + id + " | " + nombre);
    }


    @FXML
    private void initialize() {
        columnaHabilidad.setCellValueFactory(new PropertyValueFactory<>("nombreHabilidad"));
        columnaNivel.setCellValueFactory(new PropertyValueFactory<>("nivel_Habilidad"));
        tablaHabilidadesPersonaje
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> cargarDatos());

    }


    //Actualiza el titulo superior
    private void actualizarTitulo() {
        if (tituloPersonaje != null && nombrePersonaje != null) {
            tituloPersonaje.setText("Habilidades de " + nombrePersonaje);
        }
    }

    //Carga las habilidades del personaje
    private void cargarHabilidadesPersonaje() {
        if (manejadorDB == null || idPersonaje <= 0) return;

        listaObservable = FXCollections.observableArrayList(
                manejadorDB.getHabilidadesDePersonaje(idPersonaje)
        );

        tablaHabilidadesPersonaje.setItems(listaObservable);
    }

    //Carga lista de habilidades disponibles
    private void cargarListaHabilidades() {
        if (manejadorDB == null) return;

        listaHabilidadesDisponibles = FXCollections.observableArrayList(
                manejadorDB.getHabilidadPS()
        );

        habilidadComboBox.setItems(listaHabilidadesDisponibles);
    }

    //Asignar nueva habilidad
    @FXML
    private void asignarHabilidad() {

        Habilidad seleccionadaCombo = habilidadComboBox.getValue();
        String nivelStr = nivelText.getText();

        if (seleccionadaCombo == null) {
            mostrarAlerta("Debe seleccionar una habilidad.", "Error", Alert.AlertType.ERROR);
            return;
        }

        int nivel;
        try {
            nivel = Integer.parseInt(nivelStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("El nivel debe ser un número entre 1 y 10.", "Error", Alert.AlertType.ERROR);
            return;
        }

        if (nivel < 1 || nivel > 10) {
            mostrarAlerta("El nivel debe estar entre 1 y 10.", "Error", Alert.AlertType.ERROR);
            return;
        }

        HabilidadPersonaje seleccionTabla =
                tablaHabilidadesPersonaje.getSelectionModel().getSelectedItem();

        int resultado;

        if (seleccionTabla == null) {
            // INSERTAR
            resultado = manejadorDB.insertarHabilidadPersonaje(
                    idPersonaje,
                    seleccionadaCombo.getId_Habilidad(),
                    nivel
            );

            if (resultado > 0) {
                mostrarAlerta("Habilidad asignada correctamente.", "Correcto", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No se pudo asignar la habilidad.", "Error", Alert.AlertType.ERROR);
            }

        } else {
            // ACTUALIZAR
            resultado = manejadorDB.actualizarHabilidadPersonaje(
                    idPersonaje,
                    seleccionadaCombo.getId_Habilidad(),
                    nivel
            );

            if (resultado > 0) {
                mostrarAlerta("Habilidad actualizada correctamente.", "Correcto", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No se pudo actualizar la habilidad.", "Error", Alert.AlertType.ERROR);
            }
        }

        cargarHabilidadesPersonaje();
        limpiarForm();
    }





    @FXML
    private void eliminarHabilidad() {
        HabilidadPersonaje seleccion = tablaHabilidadesPersonaje.getSelectionModel().getSelectedItem();

        if (seleccion == null) {
            mostrarAlerta("Debe seleccionar una habilidad en la tabla.", "Aviso", Alert.AlertType.WARNING);
            return;
        }

        int resultado = manejadorDB.eliminarHabilidadPersonaje(
                idPersonaje,
                seleccion.getId_Habilidad()
        );

        if (resultado > 0) {
            mostrarAlerta("Habilidad removida correctamente.", "Correcto", Alert.AlertType.INFORMATION);
            cargarHabilidadesPersonaje();
        } else {
            mostrarAlerta("No se pudo remover la habilidad.", "Error", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void cargarDatos() {
        HabilidadPersonaje seleccion = tablaHabilidadesPersonaje.getSelectionModel().getSelectedItem();

        if (seleccion != null) {
            // Seleccionar habilidad en el ComboBox
            for (Habilidad h : listaHabilidadesDisponibles) {
                if (h.getId_Habilidad() == seleccion.getId_Habilidad()) {
                    habilidadComboBox.getSelectionModel().select(h);
                    break;
                }
            }

            nivelText.setText(String.valueOf(seleccion.getNivel_Habilidad()));
        }
    }

    private void limpiarForm() {
        habilidadComboBox.getSelectionModel().clearSelection();
        nivelText.clear();
        tablaHabilidadesPersonaje.getSelectionModel().clearSelection();
    }




    //Boton de cerrar
    @FXML
    private void cerrar() {
        controladorVentanas.abrirVentana(
                "/org/example/eq5_proyectofinal/editPersonaje-view.fxml",
                "Personajes",
                url, usuario, password
        );

        cerrarVentana();
    }

    public void cerrarVentana() {
        Stage actualStage = (Stage) rootHP.getScene().getWindow();
        actualStage.close();
    }

    private void aplicarIcono() {
        Stage stage = (Stage) rootHP.getScene().getWindow();
        setIconoVentana(stage, "/imagesIcons/editor.png");
    }


    //Mostrar alertas
    private void mostrarAlerta(String mensaje, String titulo, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        Stage stage = (Stage) tablaHabilidadesPersonaje.getScene().getWindow();
        if (stage != null) {
            alerta.initOwner(stage);
        }

        alerta.showAndWait();
    }
}
