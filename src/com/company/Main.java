package com.company;


import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) throws IOException {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new WindowFormMain();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void SaveErrorLog(String msg) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("error.log"));
        writer.append(msg);
        writer.close();
    }

}
