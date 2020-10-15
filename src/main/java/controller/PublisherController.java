package controller;

import utils.ControllerHelper;
import view.PublisherUI;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class PublisherController {

    private static final int port = 1222;
    private static PrintWriter pw;
    private static ServerSocket serverSocket;
    private static Socket localSocket;
    private static ArrayList<Socket> sockets; //контейнер для сбора сокетов подписчиков
    private static ArrayList<PrintWriter> writers; //контейнер для сбора потоков подписчиков
    private static JTextArea archiveNewsArea;

    public PublisherController() {

        PublisherUI publisherUI = new PublisherUI();
        publisherUI.start();
        archiveNewsArea = publisherUI.getArchiveNewsArea();

        sockets = new ArrayList<>();
        writers = new ArrayList<>();


        try {
            //открываем серверный сокет (ServerSocket)
            serverSocket = new ServerSocket(port);
            //входим в бесконечный цикл - ожидаем соединения
            while (true) {

                System.out.println("Waiting for a connection on " + port);

                try  {
                    //получаем соединение и начинаем работать с сокетом
                    //работаем с потоками ввода-вывода
                    localSocket = serverSocket.accept();
                    pw = new PrintWriter(localSocket.getOutputStream(), true); //создаем поток для записи символов в сокет
                    sockets.add(localSocket);
                    writers.add(pw);
                } catch (Exception ex) {
                    pw.close();
                    localSocket.close();
                    serverSocket.close();
                    ex.printStackTrace(System.out);
                }
            }
        } catch (IOException e) {
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public static void send(String str) {

        ControllerHelper.updateTextArea(archiveNewsArea, str, 2);

        try {
            for (PrintWriter writer: writers) {
                writer.println(str); //отправляем строку в поток
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }

    }

}
