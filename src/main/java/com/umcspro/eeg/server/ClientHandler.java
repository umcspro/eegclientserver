package com.umcspro.eeg.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final EEGServer server;
    private final Scanner input;
    private String clientName;
    private int nelectrode;


    public ClientHandler(Socket socket, EEGServer server){
        try {
            this.socket = socket;
            this.server = server;
            input = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            boolean connected = isClinetConnected();

            if (!connected) {
                return;
            }
            join();
            comunicate();
            disconnect();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean isClinetConnected() throws IOException {
        if (input.hasNextLine()) {
            clientName = input.nextLine();
            return true;
        }
        return false;
    }

    private void join() {
        server.addClient(clientName, this);
        server.printUsers();
    }


    private void comunicate() throws IOException {
        String clientMessage;
        boolean connected = true;

        while (connected) {
            if (input.hasNextLine()) {
                clientMessage = input.nextLine();
                connected = parseClientMessage(clientMessage);
            } else {
                connected = false;
            }
        }
    }

    public boolean parseClientMessage(String clientMessage) {
        if(!clientMessage.equalsIgnoreCase("bye")) {
            server.process(clientMessage, clientName);
            return true;
        }
        return false;
    }

    private void disconnect() throws IOException {
        server.removeClient(clientName);
        socket.close();
        server.printUsers();
    }

    public int getAndIncrease(){
        int  i=this.nelectrode;
        nelectrode++;
        return i;
    }


}
