package dev.jb9.screenmatchapi.utils;

import java.util.Scanner;

public class Reader {
    private final String SUFIX = "\n> ";
    private final Scanner SCANNER = new Scanner(System.in);

    public String ask(String question) {
        return ask(question, "");
    }
    public String ask(String question, String tip) {
        askQuestion(question, tip);
        return SCANNER.nextLine();
    }

    public boolean askForBoolean(String question) {
        askQuestion(question, "", true);
        String answer = SCANNER.nextLine();

        return answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes");
    }

    public int askForInteger(String question) {
        askQuestion(question, "type an integer number");
        int answer = SCANNER.nextInt();
        SCANNER.nextLine();

        return answer;
    }

    private void askQuestion(String question, String tip) {
        askQuestion(question, tip, false);
    }
    private void askQuestion(String question, String tip, boolean yesOrNoType) {
        if (!question.endsWith("?")) {
            question += "?";
        }

        if (tip.isEmpty() && !yesOrNoType) {
            System.out.print(question + SUFIX);
            return;
        }

        if (yesOrNoType) {
            tip = "Y / N";
        }

        System.out.print(question + " [" + tip + "]" + SUFIX);
    }
}
