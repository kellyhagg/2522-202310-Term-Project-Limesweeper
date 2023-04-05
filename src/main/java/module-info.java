module com.example._2522_game_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;
    requires org.testng;


    opens com.example._2522_game_project to javafx.fxml;
    exports com.example._2522_game_project;
}