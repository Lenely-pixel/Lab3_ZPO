package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class StartClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createClientWindow());
    }

    private static void createClientWindow() {
        JFrame frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        // Panel with input fields and button
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 1, 10, 10));

        // Label and field for answer
        centerPanel.add(new JLabel("Your Answer:"));
        JTextField answerField = new JTextField();
        centerPanel.add(answerField);

        // Button to send answer
        JButton sendButton = new JButton("Send Answer >>");
        centerPanel.add(sendButton);

        // Nickname field
        JPanel nickPanel = new JPanel(new BorderLayout());
        nickPanel.add(new JLabel("Your Nick:"), BorderLayout.WEST);
        JTextField nickField = new JTextField();
        nickPanel.add(nickField, BorderLayout.CENTER);
        centerPanel.add(nickPanel);

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        // Добавим действие кнопки
        sendButton.addActionListener(e -> {
            String nickname = nickField.getText().trim();
            String answer = answerField.getText().trim();

            // Проверим, что оба поля заполнены
            if (!nickname.isEmpty() && !answer.isEmpty()) {
                sendAnswerToServer(nickname, answer);
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter both nickname and answer.");
            }
        });
        centerPanel.add(sendButton, BorderLayout.SOUTH);
    }
    // Метод для отправки ответа на сервер
    private static void sendAnswerToServer(String nickname, String answer) {
        // Создаем подключение к серверу
        try (Socket socket = new Socket("localhost", 12345);  // Подключаемся к серверу
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Формируем сообщение, которое будем отправлять
            String message = nickname + "|" + answer;
            out.println(message);  // Отправляем сообщение на сервер

            // Можно добавить дополнительные действия (например, вывод сообщений в GUI о статусе)
            System.out.println("Answer sent: " + message);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Fail to connect to the server");
        }
        // TODO: Добавим действие кнопки позже
    }
}
