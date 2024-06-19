package com.umcspro.eeg.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Client {
    ServerHandler serverHandler = new ServerHandler("localhost", 2345);

    public static void main(String[] args) {
        Client client = new Client();
        Scanner fromKeyboard = new Scanner(System.in);
        String name = fromKeyboard.nextLine();
        String filepath = fromKeyboard.nextLine();
        client.sendData(name,filepath);
    }

    public void sendData(String name, String filepath) {
        try {
            serverHandler.send(name);
            Scanner filescanner = new Scanner(new File(filepath));
            while (filescanner.hasNextLine()) {
                String s = filescanner.nextLine();
                serverHandler.send(s);
                Thread.sleep(2000);
            }
            serverHandler.send("bye");
            serverHandler.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}