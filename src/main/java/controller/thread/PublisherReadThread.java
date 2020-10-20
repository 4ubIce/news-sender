package controller.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

/*
    Класс используется для удаления сокетов и потоков записи из контейнера, если подписчик прислал
    сообщение stop
 */

public class PublisherReadThread extends Thread {

    private boolean stop = false;
    private HashMap<Socket, PrintWriter> socketContainer;
    private Socket socket;
    private final BufferedReader br;

    public PublisherReadThread(HashMap<Socket, PrintWriter> socketContainer, Socket socket) throws IOException {
        this.socketContainer = socketContainer;
        this.socket = socket;
        //создаем поток для чтения символов из сокета
        //сначала открываем поток сокета - socket.getInputStream()
        //потом преобразовываем его в поток символов - new InputStreamReader
        //потом делаем его читателем строк - BufferedReader
        br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.start();
    }

    @Override
    public void run() {

        String str;

        try {
            //цикл чтения
            while (!stop) {
                synchronized (br) {
                    str = br.readLine();
                    if (str.equals("stop")) { //как только подписчик прислал сообщение stop, закрываем сокет и удаляем его из контейнера
                        closeService();
                        stop = true; //останавливаем цикл чтения
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    private void closeService() {

        final Socket[] tempSocket = new Socket[1]; //что бы работало в лямда выражении

        if (!socket.isClosed()) {
            try {
                br.close(); //закрываем поток чтения
                socketContainer.entrySet()
                                .stream()
                                .filter(e -> e.getKey().equals(socket))
                                .forEach(e -> {
                                    try {
                                        e.getValue().close(); //закрываем поток записи
                                        e.getKey().close(); //закрываем сокет
                                        tempSocket[0] = e.getKey(); //сохраняем сокет, что бы потом его удалить из контейнера
                                    } catch (IOException ioException) {
                                        ioException.printStackTrace();
                                    }
                                });
                socketContainer.remove(tempSocket[0]); //удаляем из контейнера сокет и поток записи
                this.interrupt(); //останавливаем этот поток
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
