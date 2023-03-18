package com.example._2522_game_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class Controller {

    @FXML
    protected void onButtonClick(ActionEvent event) throws IOException {
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        setToLime(btn);
    }

    @FXML
    protected void setToLime(Button btn) throws IOException {
        Image limeImage = new Image(LimesweeperApplication.class.getResource("lime.png").openStream());
        ImageView limeView = new ImageView(limeImage);
        limeView.setFitWidth(14);
        limeView.setFitHeight(14);
        btn.setText("");
        btn.setGraphic(limeView); // how an image is loaded in the cell (I need to adjust the size down)
    }
}