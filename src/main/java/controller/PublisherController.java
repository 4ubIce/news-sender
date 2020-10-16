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
    private static volatile HashMap<Socket, PrintWriter> container; //��������� ��� ����� ������� � ������� �����������
    private static JTextArea archiveNewsArea;

    public PublisherController() {

        PublisherUI publisherUI = new PublisherUI();
        publisherUI.start();
        archiveNewsArea = publisherUI.getArchiveNewsArea();
        container = new HashMap<>();


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
                    container.put(localSocket, pw);
                    startReceive(container, localSocket); //��������� ������ ��������� �� ������ �� ������� �������� ����������
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

        ControllerHelper.updateTextArea(archiveNewsArea, str, 2); //��������� view � �������� ������
        new PublisherWriteThread(container, str).start(); //�������� ������ � ������ ������

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
        }); //��������� ��� ������ � ������ � ����������
    }

}
