package controller;

import controller.thread.PublisherReadThread;
import controller.thread.PublisherWriteThread;
import utils.ControllerHelper;
import view.PublisherUI;
import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class PublisherController {

    private static final int port = 1222;
    private static PrintWriter pw;
    private static ServerSocket serverSocket;
    private static Socket localSocket;
    private static volatile HashMap<Socket, PrintWriter> container; //контейнер для сбора сокетов и потоков подписчиков
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
                    startReceive(container, localSocket); //запускаем чтение сообщений из сокета на предмет закрытия подписчика
                } catch (Exception ex) {
                    pw.close();
                    localSocket.close();
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
        new PublisherWriteThread(container, str).start(); //передаем данные в другом потоке

    }

    private void startReceive(HashMap<Socket, PrintWriter> container, Socket socket) {

        new PublisherReadThread(container, socket);

    }

    public static void doBeforeExit() {
        container.forEach((k, v) -> {
            try {
                v.close();
                k.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }); //закрываем все потоки и сокеты в контейнере
    }

}
