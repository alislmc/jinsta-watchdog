package com.company.objectView;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class ProfileItem {
    public VBox ProfileItem(String UserName, Image bigImage, boolean Change){
        VBox base = new VBox();
        HBox head = new HBox();
        head.setPrefSize(320,head.getPrefHeight());
        head.setStyle("-fx-background-color: black;");


        Label username = new Label(UserName);

        username.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; ");
        username.setTextFill(Paint.valueOf("#FFFFFF"));
        head.getChildren().add(username);
        if (Change){
            Label change = new Label("update");

            change.setStyle("-fx-font-size: 10px; -fx-font-weight:bold;");
            change.setTextFill(Paint.valueOf("#00FF0B"));
            head.getChildren().add(change);
        }

        base.getChildren().add(head);
        base.getChildren().add(new ImageView(bigImage));
        head.setPrefSize(base.getPrefWidth(),head.getPrefHeight());
        return base;

    }
}
