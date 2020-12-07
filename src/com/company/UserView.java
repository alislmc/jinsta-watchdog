package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class UserView extends Panel{


    private JLabel label_image, label_text;

    public void setUsername(String UserName){this.label_text.setText(UserName);}
    public void setImage(BufferedImage bufferedImage){
        this.label_image.setIcon(new ImageIcon(new ImageIcon(bufferedImage).getImage().getScaledInstance(150,150,Image.SCALE_DEFAULT)));}

    public UserView() throws IOException {
        super();
        this.makeUI();
    }

    private void makeUI() throws IOException {
        this.setLayout(new BorderLayout());

        this.label_image = new JLabel();
        this.label_text = new JLabel();
        label_text.setText("User Name");
        this.add(label_image,BorderLayout.NORTH);
        this.add(label_text, BorderLayout.SOUTH);
    }
}
