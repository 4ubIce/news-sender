package controller;

import utils.ControllerHelper;
import view.SubscriberUI;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SubscriberController {

    private static final int port = 1222;
    private static final String address = "127.0.0.1";
    private static boolean stop = false;
    private final JTextArea newsArea;
    private static Socket socket;
    private static BufferedReader br;

    public SubscriberController() {
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
        } catch (IOException e) {
            try {
                br.close();
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }

        startReceive();

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
        } finally {
            /*
                TODO нужно реализовать правильное закрытие сокета и его потока
            */
            try {
                br.close();
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void doBeforeExit() {
        stop = true; //останавливаем цикл чтения символов из потока
    }

}
