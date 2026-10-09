package org.example.eq5_proyectofinal.modelos;

public class Habilidad {
    private final int id_Habilidad;
    private String nombre_Habilidad;
    private String descripcion;
    private String tipo_Habilidad;
    private int poder_Base;

    public Habilidad(int id_Habilidad, String nombre_Habilidad, String descripcion, int poder_Base) {
        this.id_Habilidad = id_Habilidad;
        this.nombre_Habilidad = nombre_Habilidad;
        this.descripcion = descripcion;
        this.poder_Base = poder_Base;
    }

    public int getId_Habilidad() {
        return id_Habilidad;
    }

    public String getNombre_Habilidad() {
        return nombre_Habilidad;
    }

    public void setNombre_Habilidad(String nombre_Habilidad) {
        this.nombre_Habilidad = nombre_Habilidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo_Habilidad() {
        return tipo_Habilidad;
    }

    public void setTipo_Habilidad(String tipo_Habilidad) {
        this.tipo_Habilidad = tipo_Habilidad;
    }

    public int getPoder_Base() {
        return poder_Base;
    }

    public void setPoder_Base(int poder_Base) {
        this.poder_Base = poder_Base;
    }

    @Override
    public String toString() {
        return nombre_Habilidad; // Mostrará el nombre del arma en el ComboBox
    }
}
