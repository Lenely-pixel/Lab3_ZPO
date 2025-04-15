package com.company;

import javax.swing.*;
import java.awt.*;

public class StartServer {

    private static JTextArea serverTextArea;

    private static void createWindow() {
        JFrame frame = new JFrame("Quiz server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Создаём текстовое поле и прокрутку
        serverTextArea = new JTextArea();
        serverTextArea.setEditable(false); // Только для чтения
        JScrollPane scrollPane = new JScrollPane(serverTextArea);

        // Добавляем текстовое поле в окно
        frame.add(scrollPane, BorderLayout.CENTER);

        // Показываем окно
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Создание GUI на потоке интерфейса
        SwingUtilities.invokeLater(() -> createWindow());
    }

    // Метод для добавления текста в JTextArea
    public static void log(String message) {
        serverTextArea.append(message + "\n");
    }
}
