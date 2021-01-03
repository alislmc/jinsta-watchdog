package com.company;


import com.company.LocalDatabase.Download;
import com.company.LocalDatabase.LocalDatabase;
import com.company.LocalDatabase.UserConfig;
import com.company.objectView.CircleImageView;
import com.company.objectView.ProfileItem;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class MainForm extends Application{


    private ArrayList<String> updates = null;
    private Connection database = null;

    //make top line store section
    private void MakeTopLine(Pane root){


        HBox hBox = new HBox(3);
        ScrollPane scrollPane = new ScrollPane(hBox);
        scrollPane.fitToWidthProperty().setValue(true);
        scrollPane.setPrefSize(root.getPrefWidth(),scrollPane.getPrefHeight());
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        for(String image : new File("small_icons/").list()){
            hBox.getChildren().add(new CircleImageView().CircleImageView("small_icons/"+image));
        }

        root.getChildren().add(scrollPane);
    }


    private void MakeBottomLine(Pane root){

        Button btn_exit ;
        btn_exit = new Button("Exit");
        btn_exit.setFocusTraversable(false);



        HBox pane = new HBox();
        pane.setPrefSize(root.getPrefWidth(), 35);
        pane.setLayoutY(root.getPrefHeight() * 0.93);

        btn_exit.setPrefSize(root.getPrefWidth(),pane.getPrefHeight());

        btn_exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("exit");
                Platform.exit();
            }
        });
        pane.getChildren().add(btn_exit);
        root.getChildren().add(pane);
    }


    private void MakeCenterLine(Pane root){
        Pane pane = new Pane();
        pane.setLayoutY(root.getPrefHeight() * 0.19);
        pane.setPrefSize(root.getPrefWidth(),root.getPrefHeight()*0.74);
        ScrollPane scrollPane = new ScrollPane();
        VBox line = new VBox();
        scrollPane.setContent(line);
        pane.getChildren().add(scrollPane);
        scrollPane.setPrefSize(root.getPrefWidth(),root.getPrefHeight()*0.74);

        try {
            for(String image : new File("big_icons/").list())
            {

                if(updates != null)
                line.getChildren().addAll(
                        new ProfileItem().ProfileItem(image.substring(0,image.length()-5),new Image(new File("big_icons/"+image).toURL().toString())
                                ,updates.contains(image.substring(0,image.length()-5)))
                );

                else line.getChildren().addAll(
                        new ProfileItem().ProfileItem(image.substring(0,image.length()-5),new Image(new File("big_icons/"+image).toURL().toString())
                                ,false)
                );
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        root.getChildren().add(pane);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        root.setPrefSize(338, 510);
        //Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setTitle("JINSTA");

        {
            new File("small_icons").mkdir();
            new File("big_icons").mkdir();
                    System.out.println("make default directory");

            this.database = new LocalDatabase().getConnection();
            System.out.println("begin update user");
            this.updates = CheckForNewUpdates();
            System.out.println("update done.");
            System.out.println("show main window.");
        }

        this.MakeTopLine(root);
        this.MakeBottomLine(root);
        this.MakeCenterLine(root);
        primaryStage.show();
    }


//-------------------------------------------------------------------------------
    private ArrayList<String> CheckForNewUpdates(){
    ArrayList<String> updates = new ArrayList<String>();
    HashMap<String,String> map_ldb_user_slink = new LocalDatabase.UserTable(this.database).getAllSmallLinks();

    if(map_ldb_user_slink != null)
        for (String cnf_username : new UserConfig().getUserList()){
            if(map_ldb_user_slink.get(cnf_username) != null){
                String link = new InstaApi().getSmallImageProfileLink(cnf_username);
                if(!map_ldb_user_slink.get(cnf_username).equals(link))
                {
                    if(new Download().DownloadFile(link,"small_icons/"+cnf_username+".jpeg")){
                        new LocalDatabase.UserTable(this.database).updateUser(cnf_username, LocalDatabase.UserDataUpdatePart.SmallImageLink,link);
                    }else {System.out.println("small icon downloading is failure [Control center]"); System.exit(1);}


                    link = new InstaApi().getBigImageProfileLink(cnf_username);
                    if(new Download().DownloadFile(link,"big_icons/"+cnf_username+".jpeg")){
                        new LocalDatabase.UserTable(this.database).updateUser(cnf_username, LocalDatabase.UserDataUpdatePart.BigImageLink,link);
                    }else {System.out.println("big image downloading is failure [Control center]"); System.exit(1);}
                    updates.add(cnf_username);
                }

            }

            else {
                //add new user to db and download  images
                LocalDatabase.UserDataObject user_object = new LocalDatabase.UserDataObject();
                user_object.setUsername(cnf_username);
                user_object.setSmall_link(new InstaApi().getSmallImageProfileLink(cnf_username));
                user_object.setBig_link(new InstaApi().getBigImageProfileLink(cnf_username));
                user_object.setBig_img_name("big_icons/" + cnf_username + ".jpeg");
                user_object.setSmall_img_name("small_icons/" + cnf_username + ".jpeg");
                new LocalDatabase.UserTable(this.database).addUser(user_object);
                if(!new Download().DownloadFile(user_object.getSmall_link(),"small_icons/"+cnf_username+".jpeg"))
                    System.out.println("error in download small icon new user "+cnf_username);
                if(!new Download().DownloadFile(user_object.getBig_link(),"big_icons/"+cnf_username+".jpeg"))
                    System.out.println("error in download big icon new user "+cnf_username);
            }
        }

    return updates;
}
//-------------------------------------------------------------------------------
}
