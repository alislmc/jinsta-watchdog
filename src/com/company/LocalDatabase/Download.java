package com.company.LocalDatabase;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URL;

public class Download {
    public boolean DownloadFile(String FileUrl, String saveTo){
        try {
            URL url = new URL(FileUrl);
            BufferedInputStream stream =  new BufferedInputStream(url.openConnection().getInputStream());
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(saveTo));


            byte[] buffer = new byte[1024];
            int pos = 0;
            while ((pos = stream.read(buffer,0,buffer.length)) > -1){
                outputStream.write(buffer,0,pos);
            }
            outputStream.flush();
            outputStream.close();
            stream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
