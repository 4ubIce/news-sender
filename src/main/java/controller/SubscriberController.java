package controller;

import utils.ControllerHelper;
import view.SubscriberUI;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SubscriberController {

    private static boolean stop = false;
    private final JTextArea newsArea;
    private static Socket socket;
    private static BufferedReader br;
    private static PrintWriter pw;

    public SubscriberController(String address, int port) {
        SubscriberUI subscriberUI = new SubscriberUI();
        subscriberUI.start();
        newsArea = subscriberUI.getNewsArea();
        try {
            socket = new Socket(address, port); //открываем сокет для обращения к компьютеру в сети
            //создаем поток для чтения символов из сокета
            //сначала открываем поток сокета - socket.getInputStream()
            //потом преобразовываем его в поток символов - new InputStreamReader
            //потом делаем его читателем строк - BufferedReader
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            startReceive(); //запускаем прием сообщений из сокета
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /*
                TODO нужно реализовать правильное закрытие сокета и его потоков чтения/записи
            */
            try {
                pw.close();
                br.close();
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }



    }

    private void startReceive() {

        String str;

        try {
            //цикл чтения
            while (!stop) {
                str = br.readLine();
                if (str != null) {
                    ControllerHelper.updateTextArea(newsArea, str, 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void doBeforeExit() {
        stop = true; //останавливаем цикл чтения символов из потока
        try {
            pw = new PrintWriter(socket.getOutputStream(), true); //создаем поток для записи символов в сокет
            pw.println("stop"); //отправляем сообщение о закрытии подписчика
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
