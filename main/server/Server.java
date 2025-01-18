package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private boolean toStop = false;
    private final int port;
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
    }

    public void setToStop(boolean toStop) {
        this.toStop = toStop;
    }

    public int getPort() {
        return port;
    }

    public void start() {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        startThreads();
    }

    private void startThreads() {
        while (!toStop) {
            try {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket);
                serverThread.start();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

}
