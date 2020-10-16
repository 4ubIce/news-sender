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
            socket = new Socket(address, port); //��������� ����� ��� ��������� � ���������� � ����
            //������� ����� ��� ������ �������� �� ������
            //������� ��������� ����� ������ - socket.getInputStream()
            //����� ��������������� ��� � ����� �������� - new InputStreamReader
            //����� ������ ��� ��������� ����� - BufferedReader
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            startReceive(); //��������� ����� ��������� �� ������
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /*
                TODO ����� ����������� ���������� �������� ������ � ��� ������� ������/������
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
            //���� ������
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
        stop = true; //������������� ���� ������ �������� �� ������
        try {
            pw = new PrintWriter(socket.getOutputStream(), true); //������� ����� ��� ������ �������� � �����
            pw.println("stop"); //���������� ��������� � �������� ����������
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
