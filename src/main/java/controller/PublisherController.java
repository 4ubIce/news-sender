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
    private static ArrayList<Socket> sockets; //��������� ��� ����� ������� �����������
    private static ArrayList<PrintWriter> writers; //��������� ��� ����� ������� �����������
    private static JTextArea archiveNewsArea;

    public PublisherController() {

        PublisherUI publisherUI = new PublisherUI();
        publisherUI.start();
        archiveNewsArea = publisherUI.getArchiveNewsArea();

        sockets = new ArrayList<>();
        writers = new ArrayList<>();


        try {
            //��������� ��������� ����� (ServerSocket)
            serverSocket = new ServerSocket(port);
            //������ � ����������� ���� - ������� ����������
            while (true) {

                System.out.println("Waiting for a connection on " + port);

                try  {
                    //�������� ���������� � �������� �������� � �������
                    //�������� � �������� �����-������
                    localSocket = serverSocket.accept();
                    pw = new PrintWriter(localSocket.getOutputStream(), true); //������� ����� ��� ������ �������� � �����
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
                writer.println(str); //���������� ������ � �����
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }

    }

}
