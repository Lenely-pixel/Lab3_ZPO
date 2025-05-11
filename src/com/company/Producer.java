package com.company;

import java.io.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;

// Klasa Producer odbiera odpowiedzi od klientów i dodaje je do kolejki
public class Producer implements Runnable {
    private final int port;                       // Port, na którym nasłuchuje serwer
    private final BlockingQueue<String> queue;    // Kolejka do przechowywania odpowiedzi

    // Konstruktor ustawiający port i kolejkę
    public Producer(int port, BlockingQueue<String> queue) {
        this.port = port;
        this.queue = queue;
    }

    @Override
    public void run() {
        // Tworzymy gniazdo serwera
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            StartServer.log("🔌 Nasłuchiwanie na porcie " + port); // Log: serwer gotowy

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Czekamy na połączenie klienta

                // Odbieramy dane z gniazda
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                );
                String response = in.readLine(); // Oczekujemy jednej linii: np. "Anna:Warszawa"

                if (response != null) {
                    StartServer.log("📩 Otrzymano odpowiedź: " + response);
                    queue.put(response); // Dodajemy odpowiedź do kolejki
                }

                clientSocket.close(); // Zamykamy połączenie z klientem
            }

        } catch (IOException | InterruptedException e) {
            // W razie błędu wyświetlamy go w logu serwera
            StartServer.log("❌ Błąd w Producer: " + e.getMessage());
        }
    }
}

