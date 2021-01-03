package com.company.LocalDatabase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class UserConfig {
    public ArrayList<String> getUserList(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("username.txt"));
            char[] buffer = new char[1];
            StringBuilder str = new StringBuilder();
            while (reader.read(buffer,0,buffer.length) > -1){
                str.append(buffer[0]);
            }
            reader.close();

            ArrayList<String> list = new ArrayList<String>();
            String temp ="";
            for (char item : str.toString().toCharArray()){
                if(item != '\n' && item != ',' && item != '\r') temp+=item;
                else {
                    list.add(temp);
                    temp="";
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
