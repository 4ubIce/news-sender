package controller.thread;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

/*
    ����� ������������ ��� ������ �������� ������ � �����
 */
public class PublisherWriteThread extends Thread {

    private HashMap<Socket, PrintWriter> socketContainer;
    private String str;

    public PublisherWriteThread(HashMap<Socket, PrintWriter> socketContainer, String str) {
        this.socketContainer = socketContainer;
        this.str = str;
    }

    @Override
    public void run() {
        try {

            socketContainer.forEach((k, v) -> v.println(str)); //�������� ����� �� ���������� � ���������� ����� ���� ������� � �����

        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }
}
