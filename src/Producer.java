package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    private final BlockingQueue<Answer> queue;
    private final int port;

    public Producer(BlockingQueue<Answer> queue, int port) {
        this.queue = queue;
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            StartServer.log("Сервер запущен на порту " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message = in.readLine();
                if (message != null) {
                    String[] parts = message.split("\\|");
                    if (parts.length == 2) {
                        Answer answer = new Answer(parts[0].trim(), parts[1].trim());
                        queue.put(answer);
                        StartServer.log("Получен ответ от " + answer.getNickname());
                    }
                }
                clientSocket.close();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

