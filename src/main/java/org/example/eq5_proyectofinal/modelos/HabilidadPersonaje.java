package org.example.eq5_proyectofinal.modelos;

public class HabilidadPersonaje {
    private int nivel_Habilidad;
    private int id_Habilidad;
    private int id_Personaje;
    private String nombreHabilidad; // Nuevo campo para la vista

    public HabilidadPersonaje(int id_Habilidad, int nivel_Habilidad, int id_Personaje, String nombreHabilidad) {
        this.id_Habilidad = id_Habilidad;
        this.nivel_Habilidad = nivel_Habilidad;
        this.id_Personaje = id_Personaje;
        this.nombreHabilidad = nombreHabilidad;
    }

    public int getNivel_Habilidad() {
        return nivel_Habilidad;
    }

    public void setNivel_Habilidad(int nivel_Habilidad) {
        this.nivel_Habilidad = nivel_Habilidad;
    }

    public int getId_Habilidad() {
        return id_Habilidad;
    }

    public void setId_Habilidad(int id_Habilidad) {
        this.id_Habilidad = id_Habilidad;
    }

    public int getId_Personaje() {
        return id_Personaje;
    }

    public void setId_Personaje(int id_Personaje) {
        this.id_Personaje = id_Personaje;
    }

    public String getNombreHabilidad() {
        return nombreHabilidad;
    }

    public void setNombreHabilidad(String nombreHabilidad) {
        this.nombreHabilidad = nombreHabilidad;
    }
}
