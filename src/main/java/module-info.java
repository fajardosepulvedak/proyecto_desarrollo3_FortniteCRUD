module org.example.eq5_proyectofinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.eq5_proyectofinal to javafx.fxml;
    exports org.example.eq5_proyectofinal;
    exports org.example.eq5_proyectofinal.controlers;
    opens org.example.eq5_proyectofinal.controlers to javafx.fxml;
    exports org.example.eq5_proyectofinal.modelos;
    opens org.example.eq5_proyectofinal.modelos to javafx.fxml;

}