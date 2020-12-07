package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class WebLinkApi {
    public String getProfilePage(String UserName) throws IOException {
        URL url = new URL("https://www.instagram.com/"+UserName);
        URLConnection conn = url.openConnection();


        conn.setRequestProperty("Cookie", new UserListManager().getUserID_and_sessionID());

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String ll = "";
        StringBuilder temp = new StringBuilder();
        while ((ll = reader.readLine()) != null){
            temp.append(ll+"\n");
            if(ll.contains("og:image") )
            return ll;

        }
        Main.SaveErrorLog(temp.toString());
        return null;

    }
}
