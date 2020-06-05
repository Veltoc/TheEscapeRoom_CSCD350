package maze;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


public class QuestionManager
{
    private static Connection dbConnection;


    public static void connect()
    {
        try {
            dbConnection = DriverManager.getConnection("jdbc:sqlite:SqliteDBQuestions.db");
        } catch (SQLException e) {
            dbConnection = null;
            System.out.println("Unable to connect to database.");
            e.printStackTrace();
        }
    }

    public static void close()
    {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addQuestionToDatabase(String questionType, String prompt, String answer, String[] options)
    {
        PreparedStatement statement = null; // Setting to null to please compiler

        try {
            switch (questionType) {
                case "True/False":
                    statement = dbConnection.prepareStatement("INSERT INTO tbl_TrueFalse(Question, Answer) VALUES(?,?)");
                    break;
                case "Multiple Choice":
                    statement = dbConnection.prepareStatement("INSERT INTO tbl_MultipleChoice(Question, Answer, Incorrect1, Incorrect2, Incorrect3) VALUES(?,?,?,?,?)");
                    for (int i = 0; i < options.length; i++) {
                        statement.setString(i + 3, options[i]);
                    }
                    
                    break;
                case "Short Answer":
                    statement = dbConnection.prepareStatement("INSERT INTO tbl_ShortAnswer(Question, Answer) VALUES(?,?)");
                    break;
            }

            statement.setString(1, prompt);
            statement.setString(2, answer);

            statement.execute();
            statement.close();

        } catch (SQLException e) {
            System.out.println("Unable to insert question.");
            return;
        }

        System.out.println("Question inserted!");
    }

    public static ArrayList<Question> getQuestions()
    {
        String[] tables = {"TrueFalse", "MultipleChoice", "ShortAnswer"};
        ArrayList<Question> questions = new ArrayList<Question>();

        ResultSet results;
        String prompt, answer;
        for (String table: tables) {
            try {
                // Getting results
                results = dbConnection.prepareStatement("SELECT * FROM tbl_" + table).executeQuery();

                // Reading questions
                while(results.next()) {
                    prompt = results.getString("Question");
                    answer = results.getString("Answer").toLowerCase();

                    switch (table) {
                        case "TrueFalse":
                            questions.add(new TrueFalse(prompt, Boolean.parseBoolean(answer)));
                            break;
                        case "MultipleChoice":
                            questions.add(new MultipleChoice(prompt, answer,
                                results.getString("Incorrect1"), results.getString("Incorrect2"), results.getString("Incorrect3")));
                            break;
                        case "ShortAnswer":
                            questions.add(new ShortAnswer(prompt, answer));
                            break;
                    }
                }

                results.close();

            } catch (SQLException e) {
                System.out.printf("Unable to read table %s%n", table);
                e.printStackTrace();
                continue;
            }
        }

        return questions;
    }

    public static void start(Scanner input)
    {
        System.out.println("Entering question management mode...");

        while (true) {
            System.out.print("> ");
            String in = input.nextLine().toLowerCase();
            System.out.print("\n\n\n");

            if (in.startsWith("h")) { // help
                printCommands();
            } else if (in.startsWith("q")) { // quit
                return;

            } else if (in.startsWith("a")) { // add question
                addQuestion(input);

            } else {
                System.out.println("Unknown command. Please enter 'help' for a list of commands.");
            }
        }
    }


    private static void printCommands()
    {
        System.out.println(
                "help: displays this message.\n"
                        + "quit: quits the tool.\n"
                        + "add: adds a new question to the database\n"
                        + "\nFor all commands, you can enter the first character instead."
        );
    }

    private static void addQuestion(Scanner input)
    {
        String[] questionTypes = {"True/False", "Multiple Choice", "Short Answer"};

        // Getting question type
        System.out.println("What is the question type?");
        for (int i = 0; i < questionTypes.length; i++) {
            System.out.printf("%d. %s%n", i + 1, questionTypes[i]);
        }
        String questionType = getQuestionType(input, questionTypes);

        // Getting prompt
        System.out.print("Question prompt: ");
        String questionPrompt = input.nextLine();

        // Getting answer
        String questionAnswer;
        if (questionType.equals("True/False")) {
            System.out.print("Is the answer true or false? ");
            if (input.nextLine().toLowerCase().startsWith("t")) {
                questionAnswer = "true";
            } else {
                questionAnswer = "false";
            }
        } else {
            System.out.print("Question answer: ");
            questionAnswer = input.nextLine();
        }

        // Getting extra options
        String[] questionOptions = null; // Setting to null to please compiler
        if (questionType.equals("Multiple Choice")) {
            questionOptions = new String[3];

            for (int i = 0; i < 3; i++) {
                System.out.printf("Extra option %d: ", i + 1);
                questionOptions[i] = input.nextLine();
            }
        }

        // Adding to database
        addQuestionToDatabase(questionType, questionPrompt, questionAnswer, questionOptions);
    }

    private static String getQuestionType(Scanner input, String[] types)
    {
        String type;
        while (true) {
            type = input.nextLine().toUpperCase();

            // Trying to interpret as number
            try {
                int typeNum = Integer.parseInt(type) - 1;
                if (typeNum >= 0 && typeNum < types.length) {
                    return types[typeNum];
                }

            // Not a number
            } catch (NumberFormatException e) {
                for (String questionType: types) {
                    if (type.startsWith(questionType.substring(0, 0))) {
                        return questionType;
                    }
                }
            }

            // No valid option, so retry
            System.out.println("Please enter a valid option.");
        }
    }

    public static Connection getConnection()
    {
        return dbConnection;
    }
}
