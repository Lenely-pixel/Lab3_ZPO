package com.company;

// Importujemy komponenty GUI i klasy do wątków/kolejek
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

public class StartServer {
    private static JTextArea serverTextArea; // Pole tekstowe w oknie serwera
    private static final int PORT = 12345;   // Port serwera
    private static final BlockingQueue<String> queue = new LinkedBlockingQueue<>(); // Kolejka blokująca do odpowiedzi

    public static void main(String[] args) {
        // Uruchamiamy GUI w oddzielnym wątku
        SwingUtilities.invokeLater(StartServer::createWindow);

        // Uruchomienie producenta (odbiera odpowiedzi) i konsumenta (obsługuje logikę quizu)
        new Thread(new Producer(PORT, queue)).start();
        new Thread(new QuizConsumer(queue, StartServer::log)).start();
    }

    // Metoda logowania wiadomości do pola tekstowego
    public static void log(String message) {
        SwingUtilities.invokeLater(() -> {
            serverTextArea.append(message + "\n"); // Dodaj linię tekstu do okna
        });
    }

    // Tworzenie GUI okna serwera
    private static void createWindow() {
        JFrame frame = new JFrame("Quiz server"); // Tworzymy okno z tytułem
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Zamknięcie programu po kliknięciu [X]
        frame.setSize(500, 400); // Rozmiar okna
        frame.setLayout(new BorderLayout()); // Ustawiamy układ okna

        // Tworzymy pole tekstowe do wyświetlania wiadomości
        serverTextArea = new JTextArea();
        serverTextArea.setEditable(false); // Pole tylko do odczytu
        JScrollPane scrollPane = new JScrollPane(serverTextArea); // Dodajemy przewijanie

        frame.add(scrollPane, BorderLayout.CENTER); // Umieszczamy pole tekstowe na środku
        frame.setVisible(true); // Pokazujemy okno

        log("Server is on..."); // Pierwsza wiadomość w logu
    }
}
