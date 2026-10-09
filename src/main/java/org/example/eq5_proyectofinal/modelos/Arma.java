package org.example.eq5_proyectofinal.modelos;


public class Arma {
    private final int id_Arma;
    private int daño;
    private String nombre_Arma;
    private String descripcion_Arma;

    public Arma(int id_Arma, String nombre_Arma, String descripcion_Arma, int daño) {
        this.id_Arma = id_Arma;
        this.nombre_Arma = nombre_Arma;
        this.descripcion_Arma = descripcion_Arma;
        this.daño = daño;
    }

    public int getId_Arma() {
        return id_Arma;
    }

    public int getDaño() {
        return daño;
    }

    public void setDaño(int daño) {
        this.daño = daño;
    }

    public String getNombre_Arma() {
        return nombre_Arma;
    }

    public void setNombre_Arma(String nombre_Arma) {
        this.nombre_Arma = nombre_Arma;
    }

    public String getDescripcion_Arma() {
        return descripcion_Arma;
    }

    public void setDescripcion_Arma(String descripcion_Arma) {
        this.descripcion_Arma = descripcion_Arma;
    }

    @Override
    public String toString() {
        return nombre_Arma; // Mostrará el nombre del arma en el ComboBox
    }
}
