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

    private boolean stop = false;
    private HashMap<Socket, PrintWriter> socketContainer;
    private Socket socket;
    private final BufferedReader br;

    public PublisherReadThread(HashMap<Socket, PrintWriter> socketContainer, Socket socket) throws IOException {
        this.socketContainer = socketContainer;
        this.socket = socket;
        //������� ����� ��� ������ �������� �� ������
        //������� ��������� ����� ������ - socket.getInputStream()
        //����� ��������������� ��� � ����� �������� - new InputStreamReader
        //����� ������ ��� ��������� ����� - BufferedReader
        br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.start();
    }

    @Override
    public void run() {

        String str;

        try {
            //���� ������
            while (!stop) {
                synchronized (br) {
                    str = br.readLine();
                    if (str.equals("stop")) { //��� ������ ��������� ������� ��������� stop, ��������� ����� � ������� ��� �� ����������
                        closeService();
                        stop = true; //������������� ���� ������
                    }
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
                socketContainer.entrySet()
                                .stream()
                                .filter(e -> e.getKey().equals(socket))
                                .forEach(e -> {
                                    try {
                                        e.getValue().close(); //��������� ����� ������
                                        e.getKey().close(); //��������� �����
                                        tempSocket[0] = e.getKey(); //��������� �����, ��� �� ����� ��� ������� �� ����������
                                    } catch (IOException ioException) {
                                        ioException.printStackTrace();
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
