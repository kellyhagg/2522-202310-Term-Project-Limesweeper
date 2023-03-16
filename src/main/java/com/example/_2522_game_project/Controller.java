package com.example._2522_game_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.control.TableView;

public class Controller {

    @FXML
    protected void onButtonClick(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        btn.setText("x");
    }
}