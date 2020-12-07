package com.company;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class WindowFormMain extends JFrame {
    public void writeLog(String text){
        this.ConsoleLog.setText(this.ConsoleLog.getText()+text+"\n");
    }




    private  Dimension screenSize;
    private JTextArea ConsoleLog;
    public WindowFormMain() throws IOException {
        super();
        this.setTitle("Jinsta WatchDog");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(170*3,screenSize.width/3);
        this.setResizable(false);
        this.makeUI();
        this.show();
    }
    private void makeUI() throws IOException {
        javax.swing.JTabbedPane tabbedPane = new JTabbedPane();
        ScrollPane[] scrollPane = new ScrollPane[3];
        final Panel[] panels = new Panel[scrollPane.length];

        for(byte i=0;i<scrollPane.length;i++){
            scrollPane[i] = new ScrollPane();
            panels[i] = new Panel();
            scrollPane[i].add(panels[i]);
        }

        scrollPane[0].setBackground(Color.lightGray);
        scrollPane[1].setBackground(Color.white);
        scrollPane[2].setBackground(Color.black);

        tabbedPane.addTab("Profile View",scrollPane[0]);
        tabbedPane.addTab("Add Username",scrollPane[1]);
        tabbedPane.addTab("Console Log",scrollPane[2]);


        this.add(tabbedPane);
        tabbedPane.setSelectedIndex(0);





        this.ConsoleLog = new JTextArea();
        ConsoleLog.setBackground(Color.BLACK);
        ConsoleLog.setForeground(Color.lightGray);
        ConsoleLog.setAutoscrolls(true);
        ConsoleLog.setEditable(true);
        ConsoleLog.setDoubleBuffered(false);
        ConsoleLog.setBounds(0,0,485,380);




        panels[2].add(ConsoleLog);
        ConsoleLog.setText("@Start App on : "+new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(Calendar.getInstance().getTime())+"\n");







        final GridLayout gridLayout_tab0 = new GridLayout();
        panels[0].setLayout(gridLayout_tab0);
        gridLayout_tab0.setVgap(15);
        gridLayout_tab0.setHgap(7);








        panels[1].setLayout(null);

        final TextField txt_add_username = new TextField();
        txt_add_username.setBounds(0,0,300,27);
        panels[1].add(txt_add_username);



        JButton button_add = new JButton("Add Username");
        button_add.setBounds(310,0,170,27);
        panels[1].add(button_add);


        final JTextArea textArea_exists_user = new JTextArea();
        textArea_exists_user.setBounds(0, 32, 300, 300);
        panels[1].add(textArea_exists_user);

        Label lbl_info = new Label("empty");
        lbl_info.setText("All username\'s saved in ./user.txt");
        lbl_info.setBounds(0,300,300,150);
        panels[1].add(lbl_info);


        writeLog("# load exists username (maybe)!");
        try{
            StringBuilder str = new StringBuilder();
            for(String item  : new UserListManager().read()){
                str.append(item+"\n");
            }
            textArea_exists_user.setText(str.toString());
        }
        catch (Exception e){
            e.printStackTrace();
            writeLog(e.getMessage());
        }



        button_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!txt_add_username.getText().equals(""))
                    try {
                        new UserListManager().write(txt_add_username.getText());

                        StringBuilder str = new StringBuilder();
                        for(String item  : new UserListManager().read()){
                            str.append(item+"\n");
                        }
                        textArea_exists_user.setText(str.toString());
                        txt_add_username.setText("");
                    } catch (IOException e1) {
                        writeLog(e1.getMessage());
                        e1.printStackTrace();
                    }
            }
        });





new Thread(new Runnable() {
    @Override
    public void run() {
        try {
            writeLog("# Start Profile Picture Grabber");
            ArrayList<String> list = new UserListManager().read();
            if(list.size()>=1) {

                //------------- user view

                int pos = 0;
                for (int line = 1; line < list.size(); line++) {
                    if(pos == list.size())break;
                    gridLayout_tab0.setRows(line);
                    for (int col = 0; col < 3; col++) {
                        if(pos == list.size())break;
                        UserView view = new UserView();
                        BufferedImage image = getImageByLink(new WebLinkApi().getProfilePage(list.get(pos)));
                        if(image != null)
                        view.setImage(image);
                        else {writeLog(list.get(pos)+" not image found!");}
                        view.setUsername(list.get(pos));
                        gridLayout_tab0.setColumns(col);
                        panels[0].add(view);
                        writeLog("get picture -> "+list.get(pos));
                        pos++;

                    }
                }
            }
            else {
                writeLog("err: username list < 1");
            }
            writeLog("# end grabbing");
        }
        catch (Exception as){
            writeLog(as.toString());
            as.printStackTrace();
        }
    }
}).start();


    }

    private BufferedImage getImageByLink(String text) throws IOException {
        if(text == null){System.err.println("web Text source is null");
            return null; }

        URL url = new URL(text.split("\"")[3]);
        BufferedInputStream inputStream = new BufferedInputStream(url.openConnection().getInputStream());
        byte[] buffer = new byte[1];
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream("temp.jpg"));
        while ((inputStream.read(buffer,0,buffer.length))  > -1){
            stream.write(buffer,0,buffer.length);
        }
        stream.flush();
        stream.close();
        inputStream.close();
        return ImageIO.read(new File("temp.jpg"));
    }



}
