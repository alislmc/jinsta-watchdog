package com.company.objectView;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;

public class CircleImageView {
    public Circle CircleImageView(String ImagePath){
        Image image = new Image(new File(ImagePath).toURI().toString(),80,80,false,false);
        Circle circle = new Circle(40);
        ImagePattern imagePattern = new ImagePattern(image);
        circle.setFill(imagePattern);
        return circle;
    }
    public Circle CircleImageView(String ImagePath, int size){
        Image image = new Image(new File(ImagePath).toURI().toString(),80,80,false,false);
        Circle circle = new Circle(size);
        ImagePattern imagePattern = new ImagePattern(image);
        circle.setFill(imagePattern);
        return circle;
    }
}
