package org.example.eq5_proyectofinal.modelos;


public class Clase {
    private final int id_clase;
    private String nombre_clase;
    private String descripcion;

    public Clase( int id_clase, String nombre_clase , String descripcion) {
        this.id_clase = id_clase;
        this.nombre_clase = nombre_clase;
        this.descripcion = descripcion;
    }

    public int getId_Clase() {
        return id_clase;
    }

    public String getNombre_Clase() {
        return nombre_clase;
    }

    public void setNombre_Clase(String nombre_Clase) {
        this.nombre_clase = nombre_Clase;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return nombre_clase; // Mostrará el nombre de la clase en el ComboBox
    }
}