package com.company;



import org.json.JSONObject;

import java.io.*;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class InstaApi {
    private String baseLink="https://www.instagram.com/";
    private String sendHttpReq(String uri) throws Exception {
        URL url = new URL(uri);
        URLConnection conn = url.openConnection();


        conn.setRequestProperty("Cookie", new UserListManager().getUserID_and_sessionID());

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String ll = "";
        StringBuilder temp = new StringBuilder();
        while ((ll = reader.readLine()) != null){
            temp.append(ll+"\n");
        }

        return temp.toString();
    }


    private class UserListManager {

        public UserListManager() {
        }

        public void write(String username) throws IOException {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("user.txt", true)));
            writer.print(username + "\n");
            writer.close();
        }

        public ArrayList<String> read() throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader("user.txt"));
            String temp = "";
            ArrayList<String> list = new ArrayList<String>();
            while ((temp = reader.readLine()) != null) {
                list.add(temp);
            }
            reader.close();
            return list;
        }


        public String getUserID_and_sessionID() throws IOException {
            BufferedReader reader1 = new BufferedReader(new FileReader("info.conf"));
            StringBuilder str = new StringBuilder();
            String line = "";
            while ((line = reader1.readLine()) != null) {
                if (!line.equals("\n") || !line.equals("\r") || !line.equals("\r\n") || !line.equals(""))
                    str.append(line);
            }
            reader1.close();
            return str.toString();
        }
    }




    public String getSmallImageProfileLink(String username){
        try {
            String res = this.sendHttpReq("https://www.instagram.com/{username}/?__a=1".replace("{username}",username));
            if(res.contains("profile_pic_url_hd"))
            {
                return new JSONObject(res).getJSONObject("graphql").getJSONObject("user").getString("profile_pic_url");
            }
            else return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getBigImageProfileLink(String username){
        try {
            String res = this.sendHttpReq("https://www.instagram.com/{username}/?__a=1".replace("{username}",username));
            if(res.contains("profile_pic_url_hd"))
            {
                return new JSONObject(res).getJSONObject("graphql").getJSONObject("user").getString("profile_pic_url_hd");
            }
            else return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
