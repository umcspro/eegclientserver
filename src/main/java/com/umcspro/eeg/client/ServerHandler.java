package com.umcspro.eeg.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerHandler {
    private Socket socket;
    private PrintWriter output;

    public ServerHandler(String hostName, int port) {
        try {
            this.socket = new Socket(hostName,port);
            this.output = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String message){
        output.println(message);
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

