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
import org.example.eq5_proyectofinal.modelos.Arma;
import org.example.eq5_proyectofinal.modelos.Clase;
import org.example.eq5_proyectofinal.modelos.Personaje;
import org.example.eq5_proyectofinal.ventana;

public class personajeController implements ventana {
    @FXML
    private TextField idPersonajeText;
    @FXML
    private TextField nombreText;
    @FXML
    private TextField descripcionText;
    @FXML
    private ComboBox<Clase> claseComboBox;

    @FXML
    private ComboBox<Arma> armaComboBox;

    @FXML
    private CheckBox aliadoCheckBox;

    //TABLA
    @FXML
    private TableColumn<Personaje, Integer> columnaId;
    @FXML
    private TableColumn<Personaje, String> columnaNombre;
    @FXML
    private TableColumn<Personaje, String> columnaDescripcion;
    @FXML
    private TableColumn<Personaje, String> columnaClase;
    @FXML
    private TableColumn<Personaje, String> columnaArma;
    @FXML
    private TableColumn<Personaje, Boolean> columnaAliado;
    @FXML
    private TableView<Personaje> tablaPersonajes;
    @FXML
    private Parent rootPersonajes;

    //Variables para la conexion
    private String url;
    private String usuario;
    private String password;


    private ManejadorFortniteDB manejadorProyectoDB;
    private ObservableList<Personaje> listaObservable;

    @Override
    public void initData(String url, String user, String password) {

        this.url = url;
        this.usuario = user;
        this.password = password;
        manejadorProyectoDB = new ManejadorFortniteDB(url, user, password);
        Platform.runLater(this::aplicarIcono);
        listaObservable = FXCollections.observableArrayList(manejadorProyectoDB.getPersonajePS());
        tablaPersonajes.setItems(listaObservable);

        cargarClases();
        cargarArmas();

        tablaPersonajes.getSelectionModel().selectedItemProperty().addListener
                ((obs, oldSel, newSel) -> {
                    if (newSel != null) {
                        cargarSeleccionado(newSel);
                    }
                });
        limpiarForm();
    }



    @FXML
    public void initialize() {
        //Metodo que inicializa las columnas
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id_Personaje"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre_P"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion_P"));
        columnaClase.setCellValueFactory(new PropertyValueFactory<>("nombreClase"));
        columnaArma.setCellValueFactory(new PropertyValueFactory<>("nombreArma"));
        columnaAliado.setCellValueFactory(new PropertyValueFactory<>("ally"));
        limpiarForm();
    }

    private void cargarClases() {
        ObservableList<Clase> clases =
                FXCollections.observableArrayList(manejadorProyectoDB.getClasePS());
        claseComboBox.setItems(clases);
    }

    private void cargarArmas() {
        ObservableList<Arma> armas =
                FXCollections.observableArrayList(manejadorProyectoDB.getArmasPS());
        armaComboBox.setItems(armas);
    }



    //Metodo mixto para agregar y actualizar el personaje segun se de el caso
    @FXML
    private void guardarPersonaje() {

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

        if (claseComboBox.getValue() == null) {
            mostrarAlerta("Debe seleccionar una clase", "Error", Alert.AlertType.ERROR);
            claseComboBox.requestFocus();
            return;
        }

        if (armaComboBox.getValue() == null) {
            mostrarAlerta("Debe seleccionar un arma", "Error", Alert.AlertType.ERROR);
            armaComboBox.requestFocus();
            return;
        }

        int id = 0;
        try {
            id = Integer.parseInt(idPersonajeText.getText());
        } catch (NumberFormatException ignored) {
            id = 0; // si está vacío, lo toma como insertar
        }

        String nombreClase = claseComboBox.getValue().getNombre_Clase();
        String nombreArma = armaComboBox.getValue().getNombre_Arma();
        boolean ally = aliadoCheckBox.isSelected();

        Personaje pj = new Personaje(
                id,
                nombre,
                descripcion,
                nombreClase,
                nombreArma,
                ally
        );

        int resultado;

        if (id == 0) {
            resultado = manejadorProyectoDB.insertarPersonajePS(pj);

            if (resultado > 0) {
                mostrarAlerta("Personaje agregado correctamente.", "Correcto", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No se pudo agregar el personaje.", "Error", Alert.AlertType.ERROR);
            }
        }else {//Significa que esta actualizando un personaje existente

            resultado = manejadorProyectoDB.actualizarPersonajePS(pj);

            if (resultado > 0) {
                mostrarAlerta("Personaje actualizado correctamente.", "Correcto", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No se pudo actualizar el Personaje.", "Error", Alert.AlertType.ERROR);
            }
        }

        recargarDatos();

    }


    //Metodo para eliminar personaje
    @FXML
    private void eliminarPersonaje() {
        String idPersonajeStr = idPersonajeText.getText();
        if (idPersonajeStr.isEmpty() || idPersonajeStr.equals("0")) {
            mostrarAlerta("Debe seleccionar un Personaje válido para eliminar.", "Aviso", Alert.AlertType.WARNING);
            return;
        }
        try {
            int idEliminar = Integer.parseInt(idPersonajeStr);
            int resultado = manejadorProyectoDB.eliminarPersonajePS(idEliminar);

            if (resultado > 0) {
                mostrarAlerta("Personaje y sus habilidades asociadas eliminados correctamente.", "Correcto", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No se pudo eliminar el Personaje (ID no encontrado o error de BD).", "ERROR", Alert.AlertType.ERROR);
            }
            recargarDatos();
        } catch (NumberFormatException e) {
            mostrarAlerta("El ID debe ser un número válido.", "Error", Alert.AlertType.ERROR);
        }

    }


    //Metodo de cargar datos adaptado para trabajar con los checkbox
    @FXML
    private void cargarSeleccionado(Personaje pj) {
        idPersonajeText.setText(String.valueOf(pj.getId_Personaje()));
        nombreText.setText(pj.getNombre_P());
        descripcionText.setText(pj.getDescripcion_P());
        aliadoCheckBox.setSelected(pj.isAlly());
        if (pj.getNombreClase() != null) {
            for (Clase c : claseComboBox.getItems()) {
                if (c.getNombre_Clase().equalsIgnoreCase(pj.getNombreClase())) {
                    claseComboBox.setValue(c);
                    break;
                }
            }
        }
        if (pj.getNombreArma() != null) {
            for (Arma a : armaComboBox.getItems()) {
                if (a.getNombre_Arma().equalsIgnoreCase(pj.getNombreArma())) {
                    armaComboBox.setValue(a);
                    break;
                }
            }
        }
    }



    @FXML
    private void recargarDatos() {
        listaObservable.setAll(manejadorProyectoDB.getPersonajePS());
        limpiarForm();
    }


    private void limpiarForm() {
        // Limpiar campos de texto o controles de entrada
        idPersonajeText.clear();          // Si es TextField para mostrar el ID
        nombreText.clear();      // TextField del nombre del personaje
        descripcionText.clear(); // TextArea o TextField para la descripción
        claseComboBox.getSelectionModel().clearSelection(); // ComboBox de clases
        armaComboBox.getSelectionModel().clearSelection();  // ComboBox de armas
        aliadoCheckBox.setSelected(false); // CheckBox para aliado

        // Deseleccionar el renglon seleccionado en la tabla
        tablaPersonajes.getSelectionModel().clearSelection();
    }


    //---------------Metodo Generico de alerta------------------------------------
    private void mostrarAlerta(String mensaje, String titulo, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        //Obtener la ventana principal para centrar la alerta
        Stage stage = (Stage) tablaPersonajes.getScene().getWindow();
        if (stage != null) {
            alerta.initOwner(stage);
        }

        //alerta.show();
        alerta.showAndWait();
    }


    //----------------SECCION DE MENU---------------------------------------
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
    private void abrirHabilidadesP() {

        Personaje sel = tablaPersonajes.getSelectionModel().getSelectedItem();

        if (sel == null) {
            mostrarAlerta("Debe seleccionar un personaje para asignar habilidades.",
                    "Aviso", Alert.AlertType.WARNING);
            return;
        }

        controladorVentanas.abrirVentanaPersonajeHabilidades(
                "/org/example/eq5_proyectofinal/habilidadesPj-view.fxml",
                "Habilidades de " + sel.getNombre_P(),
                url, usuario, password,
                sel.getId_Personaje(),
                sel.getNombre_P()
        );

        cerrarVentana();
    }

    @FXML
    private void salir() {
        System.exit(0);
    }


    //Metodo creado para cerrar la ventana actual en caso de que se abra cualquier ventana del menu
    public void cerrarVentana() {
        Stage actualStage = (Stage) rootPersonajes.getScene().getWindow();
        actualStage.close();
    }
    private void aplicarIcono() {
        Stage stage = (Stage) rootPersonajes.getScene().getWindow();
        setIconoVentana(stage, "/imagesIcons/personaje.png");
    }

}
