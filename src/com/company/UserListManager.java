package com.company;

import java.io.*;
import java.util.ArrayList;

public class UserListManager {

    public UserListManager(){
    }

    public void write(String username) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("user.txt",true)));
        writer.print(username + "\n");
        writer.close();
    }

    public ArrayList<String> read() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("user.txt"));
        String temp = "";
        ArrayList<String> list = new ArrayList<String>();
        while ((temp = reader.readLine()) != null){
            list.add(temp);
        }
        reader.close();
        return list;
    }




    public String getUserID_and_sessionID() throws IOException {
        BufferedReader reader1 = new BufferedReader(new FileReader("info.conf"));
        StringBuilder str = new StringBuilder();
        String line = "";
        while ( (line=reader1.readLine()) != null){
            if(!line.equals("\n") || !line.equals("\r") || !line.equals("\r\n") || !line.equals(""))
            str.append(line);
        }
        reader1.close();
        return str.toString();
    }
}
