package com.company.LocalDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocalDatabase {

    public static class UserDataObject{
        private String username,small_img_name,big_img_name, small_link,big_link;

        public void setUsername(String UserName){this.username = UserName;}
        public void setSmall_img_name(String SmallImageName){this.small_img_name=SmallImageName;}
        public void setBig_img_name(String BigImageName){this.big_img_name=BigImageName;}
        public void setSmall_link(String SmallLink){this.small_link = SmallLink;}
        public void setBig_link(String BigLink){this.big_link=BigLink;}

        public String getUsername(){return this.username;}
        public String getSmall_img_name(){return this.small_img_name;}
        public String getBig_img_name(){return this.big_img_name;}
        public String getSmall_link(){return this.small_link;}
        public String getBig_link(){return this.big_link;}
    }
    private  String db_path="jdbc:sqlite:database.db";

    public Connection getConnection(){
        try {
            Connection connection = DriverManager.getConnection(db_path);
            System.out.println("Local Database Connected.");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public  enum UserDataUpdatePart{UserName,BigImageName,SmallImageName,BigImageLink,SmallImageLink}


    public static class UserTable{



        private Statement statement;
        private Connection connection;

        public UserTable(Connection LocalDatabaseConnection){
            if(LocalDatabaseConnection != null){
                try {
                    this.statement = LocalDatabaseConnection.createStatement();
                    String sql = "create table if not exists user_table ( user_name text, small_image_name text,big_image_name text, small_image_link text, big_image_link text ) ;";
                    this.statement.execute(sql);
                    this.connection = LocalDatabaseConnection;
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
            else {
                System.out.println("! local database connection is null");
                System.exit(1);
            }
        }

        public UserDataObject getUser(String username){
            try {
                ResultSet resultSet = this.statement.executeQuery("select * from user_table where user_name = "+username);
                UserDataObject object = new UserDataObject();
                object.setUsername(resultSet.getString("user_name"));
                object.setBig_img_name(resultSet.getString("big_image_name"));
                object.setSmall_img_name(resultSet.getString("small_image_name"));
                object.setSmall_link(resultSet.getString("small_image_link"));
                object.setBig_link(resultSet.getString("big_image_link"));
                return object;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }



        public HashMap<String,String> getAllSmallLinks(){
            try {
                ResultSet resultSet = this.statement.executeQuery("select user_name, small_image_link from user_table");
                HashMap<String,String> list = new HashMap<String, String>();
                while (resultSet.next()){
                    list.put(resultSet.getString("user_name"),resultSet.getString("small_image_link"));
                }
                return list;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void updateUser(String UserName, UserDataUpdatePart part, String NewValue){
            String sql = "update user_table set ";
            switch (part){
                case UserName:sql+=" user_name "; break;
                case BigImageName:sql+=" big_image_name "; break;
                case SmallImageName:sql+=" small_image_name "; break;
                case BigImageLink:sql+=" big_image_link "; break;
                case SmallImageLink:sql+=" small_image_link "; break;
            }

            sql+=" = ? where user_name = \'"+UserName+"\' ;";
            try {
                PreparedStatement ps = this.connection.prepareStatement(sql);
                ps.setString(1,NewValue);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void addUser(UserDataObject user){
            try
            {
                String sql = "insert into user_table(user_name,big_image_name,small_image_name,big_image_link,small_image_link) values (?,?,?,?,?)";
                PreparedStatement ps = this.connection.prepareStatement(sql);
                ps.setString(1,user.getUsername());
                ps.setString(2,user.getBig_img_name());
                ps.setString(3,user.getSmall_img_name());
                ps.setString(4,user.getBig_link());
                ps.setString(5,user.getSmall_link());
                ps.executeUpdate();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
