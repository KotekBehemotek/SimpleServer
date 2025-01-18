package main.server;

import main.processing.RequestInterpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

    private final Socket socket;

    protected ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (socket.isBound() && !socket.isClosed()) {

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                outputStream.write(RequestInterpreter.interpretRequest(inputStream));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}