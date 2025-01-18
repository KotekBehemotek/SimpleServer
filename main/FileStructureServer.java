package main;

import main.server.Server;

public class FileStructureServer {

    public static void main(String[] args) {
        Server server = new Server(8080);
        server.start();
    }

}
