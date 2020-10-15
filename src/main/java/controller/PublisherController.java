package controller;

import controller.thread.PublisherSocketThread;
import utils.ControllerHelper;
import view.PublisherUI;
import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class PublisherController {

    private static final int port = 1222;
    private static PrintWriter pw;
    private static ServerSocket serverSocket;
    private static Socket localSocket;
    private static HashMap<Socket, PrintWriter> container; //контейнер для сбора сокетов и потоков подписчиков
    private static JTextArea archiveNewsArea;

    public PublisherController() {

        PublisherUI publisherUI = new PublisherUI();
        publisherUI.start();
        archiveNewsArea = publisherUI.getArchiveNewsArea();
        container = new HashMap<>();


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
                    container.put(localSocket, pw);
                } catch (Exception ex) {
                    pw.close();
                    localSocket.close();
                    serverSocket.close();
                    ex.printStackTrace(System.out);
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void send(String str) {

        ControllerHelper.updateTextArea(archiveNewsArea, str, 2); //обновляем view в основном потоке
        /*
            TODO нужно реализовать удаление из контейнера сокетов для уже закрытых подписчиков
         */
        new PublisherSocketThread(container, str).start(); //передаем данные в другом потоке

    }

}
