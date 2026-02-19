open module com.jorgematias {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires lombok;
    requires java.sql;
    requires mysql.connector.j;

    requires org.hibernate.orm.core;
    requires javafx.base; 

    exports com.jorgematias;
}