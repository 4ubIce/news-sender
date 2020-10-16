package controller.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

/*
    ����� ������������ ��� �������� ������� � ������� ������ �� ����������, ���� ��������� �������
    ��������� stop
 */

public class PublisherReadThread extends Thread {

    private HashMap<Socket, PrintWriter> socketContainer;
    private Socket socket;
    private BufferedReader br;

    public PublisherReadThread(HashMap<Socket, PrintWriter> socketContainer, Socket socket) {
        this.socketContainer = socketContainer;
        this.socket = socket;
        this.start();
    }

    @Override
    public void run() {

        String str;

        try {

            //������� ����� ��� ������ �������� �� ������
            //������� ��������� ����� ������ - socket.getInputStream()
            //����� ��������������� ��� � ����� �������� - new InputStreamReader
            //����� ������ ��� ��������� ����� - BufferedReader
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //���� ������
            while (true) {
                str = br.readLine();
                if (str.equals("stop")) { //��� ������ ��������� ������� ��������� stop, ��������� ����� � ������� ��� �� ����������
                    closeService();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    private void closeService() {

        final Socket[] tempSocket = new Socket[1]; //��� �� �������� � ����� ���������

        if (!socket.isClosed()) {
            try {
                br.close(); //��������� ����� ������
                socketContainer.forEach((k, v) -> {
                    if (k.equals(socket)) {
                        tempSocket[0] = k;
                        try {
                            v.close(); //��������� ����� ������
                            k.close(); //��������� �����
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                socketContainer.remove(tempSocket[0]); //������� �� ���������� ����� � ����� ������
                this.interrupt(); //������������� ���� �����
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}