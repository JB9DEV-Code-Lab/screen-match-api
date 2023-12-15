package dev.jb9.screenmatchapi.utils;

import java.util.Arrays;
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
        return askForInteger(question, new int[0]);
    }

    public int askForInteger(String question, int[] validOptions) {
        int validOptionsCount = validOptions.length;
        String tipComplement = switch(validOptionsCount) {
            case 0 -> "";
            case 1 -> "from 0 to %d".formatted(validOptions[0]);
            case 2 -> "from %d to %d".formatted(validOptions[0], validOptions[1]);
            default -> Arrays.toString(validOptions);
        };

        askQuestion(question, "type an integer number %s".formatted(tipComplement));
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
