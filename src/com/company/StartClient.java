package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;

public class StartClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartClient::createWindow);
    }

    private static void createWindow() {
        JFrame frame = new JFrame("Quiz klient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new GridLayout(4, 1));

        // Pole na imię gracza
        JTextField nameField = new JTextField();
        frame.add(new JLabel("Twoje imię:"));
        frame.add(nameField);

        // Pole na odpowiedź
        JTextField answerField = new JTextField();
        frame.add(new JLabel("Twoja odpowiedź:"));
        frame.add(answerField);

        // Przycisk do wysłania odpowiedzi
        JButton sendButton = new JButton("Wyślij odpowiedź");
        frame.add(sendButton);

        // Akcja po kliknięciu
        sendButton.addActionListener((ActionEvent e) -> {
            String name = nameField.getText().trim();
            String answer = answerField.getText().trim();

            if (name.isEmpty() || answer.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Wypełnij oba pola!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Format wiadomości: Imię:Odpowiedź
            String message = name + ":" + answer;
            sendMessageToServer(message);
            answerField.setText(""); // czyścimy pole odpowiedzi
        });

        frame.setVisible(true);
    }

    // Wysyłka wiadomości do serwera
    private static void sendMessageToServer(String message) {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(message);
            JOptionPane.showMessageDialog(null, "📤 Wysłano: " + message);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "❌ Błąd połączenia: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
