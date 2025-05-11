package com.company;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

// Klasa QuizConsumer odpowiada za obsługę quizu na serwerze
public class QuizConsumer implements Runnable {
    private final BlockingQueue<String> queue; // Kolejka z odpowiedziami
    private final Consumer<String> logger; // Funkcja logująca (np. do GUI)

    private final Map<String, String> questions = new LinkedHashMap<>(); // Lista pytań i odpowiedzi
    private Iterator<Map.Entry<String, String>> iterator; // Iterator do przechodzenia przez pytania

    // Konstruktor klasy - ustawia kolejkę i logger, ładuje pytania
    public QuizConsumer(BlockingQueue<String> queue, Consumer<String> logger) {
        this.queue = queue;
        this.logger = logger;

        loadQuestions(); // Wczytujemy pytania do mapy
        iterator = questions.entrySet().iterator(); // Tworzymy iterator do pytań
    }

    // Funkcja do ładowania pytań (tu "na sztywno", ale może być z pliku)
    private void loadQuestions() {
        questions.put("Stolica Polski?", "Warszawa");
        questions.put("2 + 2 = ?", "4");
        questions.put("Język urzędowy w Niemczech?", "Niemiecki");
    }

    // Główna pętla quizu
    @Override
    public void run() {
        while (iterator.hasNext()) {
            Map.Entry<String, String> current = iterator.next();     // Pobieramy aktualne pytanie
            String question = current.getKey();
            String correctAnswer = current.getValue();
            logger.accept("❓ PYTANIE: " + question);                 // Wyświetlamy pytanie

            boolean answered = false;                                // Czy już ktoś odpowiedział poprawnie?
            Set<String> alreadyAnswered = new HashSet<>();           // Kto już próbował odpowiedzi?

            while (!answered) {
                try {
                    String message = queue.take();                   // Czekamy na odpowiedź od klienta
                    String[] parts = message.split(":");             // Oczekujemy format: imię:odpowiedź

                    if (parts.length != 2) continue;

                    String player = parts[0].trim().toLowerCase();   // Imię gracza (czyścimy i unifikujemy)
                    String answer = parts[1].trim();

                    if (alreadyAnswered.contains(player)) {
                        logger.accept(" :) " + player + " już odpowiadał.");
                        continue;
                    }

                    alreadyAnswered.add(player);                    // Dodajemy do listy próbujących

                    if (answer.equalsIgnoreCase(correctAnswer)) {
                        logger.accept(":D " + player + " odpowiedział poprawnie!");
                        answered = true;
                    } else {
                        logger.accept(":( " + player + " odpowiedź błędna: " + answer);
                    }

                } catch (InterruptedException e) {
                    logger.accept(":( Błąd: " + e.getMessage());
                }
            }
        }

        logger.accept("\n QUIZ ZAKOŃCZONY!");
    }
}
