package org.example.eq5_proyectofinal;
import org.example.eq5_proyectofinal.modelos.*;

import java.sql.*;
import java.util.ArrayList;


public class ManejadorFortniteDB {

    private String url;
    private String user;
    private String password;

    public ManejadorFortniteDB(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    //metodos para realizar la conexion a la base de datos
    public Connection abrirConexion() {
        try {
            DriverManager.setLoginTimeout(10);
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void cerrarConexion(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean probarConexion() {
        Connection conn = abrirConexion();

        if (conn != null) {
            cerrarConexion(conn);
            return true;
        } else {
            return false;
        }
    }


    //APARTADO DE PERSONAJE
    public ArrayList<Personaje> getPersonajePS() {
        ArrayList<Personaje> personajes = new ArrayList<>();

        // Usamos JOIN para traer los nombres de clase y arma
        String query = """
        SELECT p.id_personaje, p.nombre_personaje, p.descripcion_personaje,
               p.is_ally,
               c.nombre_clase,
               a.nombre_arma
        FROM personaje p
        LEFT JOIN clase c ON p.id_clase = c.id_clase
        LEFT JOIN arma a ON p.id_arma = a.id_arma
    """;

        try (Connection conn = abrirConexion();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idPj = rs.getInt("id_personaje");
                String nombreP = rs.getString("nombre_personaje");
                String descripcionP = rs.getString("descripcion_personaje");
                String nombreClase = rs.getString("nombre_clase"); // nombre en vez de id
                String nombreArma = rs.getString("nombre_arma");   // nombre en vez de id
                boolean ally = rs.getBoolean("is_ally");

                // Aquí puedes usar el constructor actual y reemplazar idClase/idArma por nombres
                // O bien, si quieres guardar los nombres, necesitas modificar Personaje para aceptar Strings
                personajes.add(new Personaje(idPj, nombreP, descripcionP, nombreClase, nombreArma, ally));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return personajes;
    }

    public int insertarPersonajePS(Personaje personaje) {

        String sqlClase = "SELECT id_clase FROM clase WHERE nombre_clase = ?";
        int idClase = 0;

        try (Connection conn = abrirConexion();
             PreparedStatement ps = conn.prepareStatement(sqlClase)) {

            ps.setString(1, personaje.getNombreClase());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idClase = rs.getInt("id_clase");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sqlArma = "SELECT id_arma FROM arma WHERE nombre_arma = ?";
        int idArma = 0;

        try (Connection conn = abrirConexion();
             PreparedStatement ps = conn.prepareStatement(sqlArma)) {

            ps.setString(1, personaje.getNombreArma());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idArma = rs.getInt("id_arma");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sqlInsert = "INSERT INTO personaje " +
                "(nombre_personaje, descripcion_personaje, id_clase, id_arma, is_ally) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = abrirConexion();
             PreparedStatement ps = conn.prepareStatement(sqlInsert)) {

            ps.setString(1, personaje.getNombre_P());
            ps.setString(2, personaje.getDescripcion_P());
            ps.setInt(3, idClase);
            ps.setInt(4, idArma);
            ps.setInt(5, personaje.isAlly() ? 1 : 0);

            return ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public int actualizarPersonajePS(Personaje personaje) {

        String sqlClase = "SELECT id_clase FROM clase WHERE nombre_clase = ?";
        String sqlArma = "SELECT id_arma FROM arma WHERE nombre_arma = ?";
        String sqlUpdate = "UPDATE personaje SET " +
                "nombre_personaje = ?, descripcion_personaje = ?, id_clase = ?, id_arma = ?, is_ally = ? " +
                "WHERE id_personaje = ?";

        int idClase = 0;
        int idArma = 0;

        try (Connection conn = abrirConexion()) {
            //Buscara el id de la clase por nombre
            try (PreparedStatement psClase = conn.prepareStatement(sqlClase)) {
                psClase.setString(1, personaje.getNombreClase());
                ResultSet rs = psClase.executeQuery();

                if (rs.next()) {
                    idClase = rs.getInt("id_clase");
                }
            }
            //buscara el id del arma tambien por nombre
            try (PreparedStatement psArma = conn.prepareStatement(sqlArma)) {
                psArma.setString(1, personaje.getNombreArma());
                ResultSet rs = psArma.executeQuery();

                if (rs.next()) {
                    idArma = rs.getInt("id_arma");
                }
            }
            //Actualizara el personaje una vez obtenido el id del arma y el id de la clase
            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {

                psUpdate.setString(1, personaje.getNombre_P());
                psUpdate.setString(2, personaje.getDescripcion_P());

                // Clase NULL si no existe
                if (idClase == 0)
                    psUpdate.setNull(3, java.sql.Types.INTEGER);
                else
                    psUpdate.setInt(3, idClase);

                // Arma NULL si no existe
                if (idArma == 0)
                    psUpdate.setNull(4, java.sql.Types.INTEGER);
                else
                    psUpdate.setInt(4, idArma);

                psUpdate.setInt(5, personaje.isAlly() ? 1 : 0);

                // ID del personaje a actualizar
                psUpdate.setInt(6, personaje.getId_Personaje());

                return psUpdate.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    //eliminar personaje
    public int eliminarPersonajePS(int idPersonaje) {
        String query = "DELETE FROM personaje WHERE id_personaje = ?";
        try (
                Connection conn = abrirConexion();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, idPersonaje);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



    // APARTADO DE ARMA
    public ArrayList<Arma> getArmasPS() {
        ArrayList<Arma> armas = new ArrayList<>();
        String query = "SELECT * FROM arma";

        try (Connection conn = abrirConexion();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idArma = rs.getInt("id_arma");
                String nombreArma = rs.getString("nombre_arma");
                String descripcionArma = rs.getString("descripcion_arma");
                int danio = rs.getInt("danio");

                armas.add(new Arma(idArma, nombreArma,descripcionArma ,danio));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return armas;
    }

    public int insertarArmaPS(Arma arma) {
        String query = "INSERT INTO arma (nombre_arma, descripcion_arma, danio) VALUES (?, ?, ?)";

        try (Connection conn = abrirConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, arma.getNombre_Arma());
            ps.setString(2, arma.getDescripcion_Arma());
            ps.setInt(3, arma.getDaño());

            return ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // eliminar arma
    public int eliminarArmaPS(int idArma) {
        String query = "DELETE FROM arma WHERE id_arma = ?";
        try (
                Connection conn = abrirConexion();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, idArma);
            return ps.executeUpdate();
        } catch (SQLException e) {

            e.printStackTrace();
            return 0;
        }
    }



    public int actualizarArmaPS(Arma arma) {
        String query = "UPDATE arma SET nombre_arma=?, descripcion_arma=?, danio=? WHERE id_arma=?";

        try (Connection conn = abrirConexion();
             //Statement stmt = conn.createStatement()
             PreparedStatement ps=conn.prepareStatement(query)) {

            ps.setString(1, arma.getNombre_Arma());
            ps.setString(2, arma.getDescripcion_Arma());
            ps.setInt(3, arma.getDaño());
            ps.setInt(4, arma.getId_Arma());

            return ps.executeUpdate(); //Devuelve el numero de registros agregados en la base de datos

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }


    }

    //APARTADO DE HABILIDAD
    public ArrayList<Habilidad> getHabilidadPS() {
        ArrayList<Habilidad> habilidades = new ArrayList<>();
        String query = "SELECT * FROM habilidad";
        try (Connection conn = abrirConexion();//Abrir conexion
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {//Setencia que arroja un valor booleano//Recorrer cada linea del resultSet

                int idHabilidad = rs.getInt("id_habilidad");
                String nombreHabilidad = rs.getString("nombre_habilidad");
                String descripcionHabilidad = rs.getString("descripcion_habilidad");
                int poder_base = rs.getInt("poder_base");

                habilidades.add(new Habilidad(idHabilidad,nombreHabilidad,descripcionHabilidad,poder_base));//Crea un objeto dentro de la lista

            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return habilidades;
    }

    public int insertarHabilidadPS(Habilidad habilidad) {
        String query = "INSERT INTO habilidad (nombre_habilidad, descripcion_habilidad, poder_base) VALUES (?, ?, ?)";

        try (Connection conn = abrirConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, habilidad.getNombre_Habilidad());
            ps.setString(2, habilidad.getDescripcion());
            ps.setInt(3, habilidad.getPoder_Base());

            return ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int actualizarHabilidadPS(Habilidad habilidad) {
        String query = "UPDATE habilidad SET nombre_habilidad = ?, descripcion_habilidad = ?, poder_base = ? " +
                "WHERE id_habilidad = ?";

        try (Connection conn = abrirConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, habilidad.getNombre_Habilidad());
            ps.setString(2, habilidad.getDescripcion());
            ps.setInt(3, habilidad.getPoder_Base());
            ps.setInt(4, habilidad.getId_Habilidad());

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //eliminar habilidad
    public int eliminarHabilidadPS(int idHabilidad) {
        String query = "DELETE FROM habilidad WHERE id_habilidad = ?";
        try (
                Connection conn = abrirConexion();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, idHabilidad);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }


    }


    //APARTADO DE CLASE
    public ArrayList<Clase> getClasePS() {
        ArrayList<Clase> clase = new ArrayList<>();
        String query = "SELECT * FROM clase";
        try (Connection conn = abrirConexion();//Abrir conexion
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {//Setencia que arroja un valor booleano//Recorrer cada linea del resultSet

                int idClase = rs.getInt("id_clase");
                String nombreClase = rs.getString("nombre_clase");
                String descripcionClase = rs.getString("descripcion_clase");


                clase.add(new Clase(idClase, nombreClase, descripcionClase));//Crea un objeto dentro de la lista

            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return clase;
    }

    public int insertarClasePS(Clase clase) {
        String query = "INSERT INTO clase (nombre_clase, descripcion_clase) VALUES (?, ?)";

        try (Connection conn = abrirConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, clase.getNombre_Clase());
            ps.setString(2, clase.getDescripcion());

            return ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public int actualizarClasePS(Clase clase) {
        String query = "UPDATE clase SET nombre_clase = ?, descripcion_clase = ? WHERE id_clase = ?";

        try (Connection conn = abrirConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, clase.getNombre_Clase());
            ps.setString(2, clase.getDescripcion());
            ps.setInt(3, clase.getId_Clase());

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    //eliminar clase
    public int eliminarClasePS(int idClase) {
        // La consulta DELETE segura
        String query = "DELETE FROM clase WHERE id_clase = ?";
        try (
                Connection conn = abrirConexion();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, idClase);
            return ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //SECCION DE HABILIDADES DE PERSONAJES

    public ArrayList<HabilidadPersonaje> getHabilidadesDePersonaje(int idPersonaje) {
        ArrayList<HabilidadPersonaje> lista = new ArrayList<>();

        String query =
                "SELECT ph.id_habilidad, ph.nivel_habilidad, ph.id_personaje, h.nombre_habilidad " +
                        "FROM personaje_habilidad ph " +
                        "JOIN habilidad h ON ph.id_habilidad = h.id_habilidad " +
                        "WHERE ph.id_personaje = ?";

        try (Connection conn = abrirConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idPersonaje);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idH = rs.getInt("id_habilidad");
                    int nivel = rs.getInt("nivel_habilidad");
                    int idP = rs.getInt("id_personaje");
                    String nombre = rs.getString("nombre_habilidad");

                    HabilidadPersonaje hp = new HabilidadPersonaje(idH, nivel, idP, nombre);
                    lista.add(hp);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public int insertarHabilidadPersonaje(int idPersonaje, int idHabilidad, int nivel) {
        String check = "SELECT COUNT(*) FROM personaje_habilidad WHERE id_personaje = ? AND id_habilidad = ?";
        String insert = "INSERT INTO personaje_habilidad (id_personaje, id_habilidad, nivel_habilidad) VALUES (?, ?, ?)";

        try (Connection conn = abrirConexion();
             PreparedStatement psCheck = conn.prepareStatement(check)) {

            //comprueba la existencia
            psCheck.setInt(1, idPersonaje);
            psCheck.setInt(2, idHabilidad);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        return -1; // ya existe
                    }
                }
            }

            //insertar
            try (PreparedStatement psInsert = conn.prepareStatement(insert)) {
                psInsert.setInt(1, idPersonaje);
                psInsert.setInt(2, idHabilidad);
                psInsert.setInt(3, nivel);
                return psInsert.executeUpdate(); // 1 si OK
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int actualizarHabilidadPersonaje(int idPersonaje, int idHabilidad, int nivel) {

        String query = "UPDATE personaje_habilidad " +
                "SET nivel_habilidad = ? " +
                "WHERE id_personaje = ? AND id_habilidad = ?";

        try (Connection conn = abrirConexion();
             //Statement stmt = conn.createStatement()
             PreparedStatement ps=conn.prepareStatement(query)) {

            ps.setInt(1, nivel);
            ps.setInt(2, idPersonaje);
            ps.setInt(3, idHabilidad);

            return ps.executeUpdate(); //Devuelve el numero de registros agregados en la base de datos

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int eliminarHabilidadPersonaje(int idPersonaje, int idHabilidad) {
        String query = "DELETE FROM personaje_habilidad WHERE id_personaje = ? AND id_habilidad = ?";
        try (Connection conn = abrirConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idPersonaje);
            ps.setInt(2, idHabilidad);
            return ps.executeUpdate(); // 1 si borró, 0 si no había
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


}




