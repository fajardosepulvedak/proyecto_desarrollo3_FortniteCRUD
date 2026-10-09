package org.example.eq5_proyectofinal.modelos;

public class Personaje {
    private final int id_Personaje;
    private String nombre_P;
    private String descripcion_P;
    private String nombreClase;
    private String nombreArma;
    private boolean ally;

    public Personaje(int id_Personaje, String nombre_P, String descripcion_P,
                     String nombreClase, String nombreArma, boolean ally) {
        this.id_Personaje = id_Personaje;
        this.nombre_P = nombre_P;
        this.descripcion_P = descripcion_P;
        this.nombreClase = nombreClase;
        this.nombreArma = nombreArma;
        this.ally = ally;
    }

    public int getId_Personaje() {
        return id_Personaje;
    }

    public String getNombre_P() {
        return nombre_P;
    }

    public void setNombre_P(String nombre_P) {
        this.nombre_P = nombre_P;
    }

    public String getDescripcion_P() {
        return descripcion_P;
    }

    public void setDescripcion_P(String descripcion_P) {
        this.descripcion_P = descripcion_P;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public boolean isAlly() {
        return ally;
    }

    public void setAlly(boolean ally) {
        this.ally = ally;
    }

    public String getNombreArma() {
        return nombreArma;
    }

    public void setNombreArma(String nombreArma) {
        this.nombreArma = nombreArma;
    }
}