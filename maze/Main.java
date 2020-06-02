package maze;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import maze.Room.Direction;

import java.sql.*;

public class Main {
    private static final int HEIGHT = 4;
    private static final int WIDTH = 4;
    private static Scanner keyboard;
    private static Random rnd = new Random();
    private static Maze maze;
    private static Room currentRoom;
    private static ArrayList<Question> questions;
    private static Connection conn = null;


    public static void main(String[] args)
    {
        keyboard = new Scanner(System.in);
        boolean quit;
        String in;
        try
        {
            while (true)
            {
                maze = new Maze(WIDTH, HEIGHT);
                currentRoom = maze.getStart();

                connectDatabase();
                getDBQuestions();

                quit = playGame();
                if (quit) break;

                System.out.println("\n" + maze.getDisplay(currentRoom));
                System.out.println("Do you want to play again? (y/n)");
                in = keyboard.nextLine().toLowerCase();
                if (!in.startsWith("y"))
                    break;
            }
            keyboard.close();
            conn.close();
        }
        catch (java.sql.SQLException e)
        {
            System.out.println("Unable to connect to SQL, closing");
        }
    }

    public static void connectDatabase()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:SqliteDBQuestions.db");
            System.out.println("SQLite DB connected");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public static void getDBQuestions()
    {
        questions = new ArrayList<Question>();
        PreparedStatement ps = null;
        String sql = "";
        ResultSet rs = null;
        try
        {
            sql = "SELECT * FROM tbl_TrueFalse";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next())
            {
                String question = rs.getString("Question");
                String answer = rs.getString("Answer");
                answer.toLowerCase();
                questions.add(new TrueFalse(question, Boolean.parseBoolean(answer)));
            }

            ps.close();
            rs.close();

            sql = "SELECT * FROM tbl_MultipleChoice";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next())
            {
                String question = rs.getString("Question");
                String answer = rs.getString("Answer");
                String[] incorrectQs = {rs.getString("Incorrect1"), rs.getString("Incorrect2"), rs.getString("Incorrect3")};
                questions.add(new MultipleChoice(question, answer, incorrectQs));
            }

            ps.close();
            rs.close();

            sql = "SELECT * FROM tbl_ShortAnswer";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next())
            {
                String question = rs.getString("Question");
                String answer = rs.getString("Answer");
                questions.add(new ShortAnswer(question, answer));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.toString());
        }
        finally
        {
            try
            {
                rs.close();
                ps.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        Collections.shuffle(questions, rnd);
    }

    public static void insertTFQuestion(String question, String answer)
    {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO tbl_TrueFalse(Question, Answer) VALUES(?,?)");
            ps.setString(1, question);
            ps.setString(2, answer);
            ps.execute();
            ps.close();
            questions.add(new TrueFalse(question, Boolean.parseBoolean(answer)));
            Collections.shuffle(questions, rnd);
            System.out.println("True/False question inserted!");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public static void insertMCQuestion(String question, String answer, String[] incorrectOptions)
    {
        try
        {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO tbl_MultipleChoice(Question, Answer, Incorrect1, Incorrect2, Incorrect3) VALUES(?,?,?,?,?)");
            ps.setString(1, question);
            ps.setString(2, answer);
            ps.setString(3, incorrectOptions[0]);
            ps.setString(4, incorrectOptions[1]);
            ps.setString(5, incorrectOptions[2]);
            ps.execute();
            ps.close();
            questions.add(new MultipleChoice(question, answer, incorrectOptions));
            Collections.shuffle(questions, rnd);
            System.out.println("Multiple Choice question inserted!");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public static void insertSAQuestion(String question, String answer)
    {
        try
        {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO tbl_ShortAnswer(Question, Answer) VALUES(?,?)");
            ps.setString(1, question);
            ps.setString(2, answer);
            ps.execute();
            ps.close();
            questions.add(new ShortAnswer(question, answer));
            Collections.shuffle(questions, rnd);
            System.out.println("Short Answer question inserted!");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private static boolean playGame()
    {
        // Returns whether the game should immediately quit or not
        while (true)
        {
            // Prompting player
            System.out.println("\n" + maze.getDisplay(currentRoom));
            System.out.println("Enter your command. Entering 'help' will show a list of commands.");

            // Getting command
            while (true)
            {
                System.out.print("> ");
                String in = keyboard.nextLine().toLowerCase();
                System.out.print("\n\n\n");
                if (in.equals("help")) {
                    printCommands();
                    continue;
                }
                else if (in.startsWith("q")) {//quit
                    return true;
                }
                else if (in.equals("add question")) {
                    addQuestion();
                    continue;
                }

                else if (in.startsWith("save")) {
                    String filename = getFilename("save", in);
                    if (filename == null) {
                        System.out.println("Please enter a file name.");
                        continue;
                    }
                    try {
                        FileOutputStream file = new FileOutputStream(filename);
                        ObjectOutputStream out = new ObjectOutputStream(file);
                        out.writeObject(maze);
                        out.writeObject(currentRoom);
                        file.close();
                        out.close();
                    }
                    catch (Exception e) {
                        System.out.println("Unable to save...");
                        continue;
                    }
                }
                else if (in.startsWith("load")) {
                    String filename = getFilename("save", in);
                    if (filename == null) {
                        System.out.println("Please enter a file name.");
                        continue;
                    }
                    try {
                        FileInputStream file = new FileInputStream(filename);
                        ObjectInputStream inFile = new ObjectInputStream(file);
                        maze = (Maze) inFile.readObject();
                        currentRoom = (Room) inFile.readObject();
                        file.close();
                        inFile.close();
                    }
                    catch (Exception e) {
                        System.out.println("Unable to load...");
                        continue;
                    }

                // Directions
                }
                else if (in.startsWith("n") || in.equals("up")) {
                    moveToRoom(Direction.UP);
                }
                else if (in.startsWith("s") || in.equals("down")) {
                    moveToRoom(Direction.DOWN);
                }
                else if (in.startsWith("w") || in.equals("left")) {
                    moveToRoom(Direction.LEFT);
                }
                else if (in.startsWith("e") || in.equals("right")) {
                    moveToRoom(Direction.RIGHT);
                }
                else if (in.startsWith("/")) {
                    if (in.equals("/")) {
                        printCheatCommands();
                        continue;
                    }
                    else if (in.startsWith("/unlock")) {
                        Direction dir;
                        if (in.endsWith("up")) {
                            dir = Direction.UP;
                        }
                        else if (in.endsWith("down")) {
                            dir = Direction.DOWN;
                        }
                        else if (in.endsWith("left")) {
                            dir = Direction.LEFT;
                        }
                        else if (in.endsWith("right")) {
                            dir = Direction.RIGHT;
                        }
                        else {
                            System.out.println("Unknown direction. Please enter up, down, left, or right.");
                            continue;
                        }
                        currentRoom.openDoor(dir);
                    } else if (in.startsWith("/escape")) {
                        currentRoom = maze.getFinish();
                    }
                }

                else {
                    System.out.println("Unknown command. Please enter 'help' for a list of commands.");
                }
                break;
            }

            // Checking game state
            if (maze.getFinish() == currentRoom) {
                playSound("win");
                System.out.println("You escaped! Congratulations!");
                break;
            }
            else if (!maze.isPathToFinish(currentRoom)) {
                playSound("lose");
                System.out.println("There is no escape...");
                break;
            }
        }
        return false;
    }

    private static void addQuestion()
    {
        System.out.println(
                "Enter the corresponding number to the Question you wish to add\n"
                        + "1. True/False Question\n"
                        + "2. Multiple Choice Question\n"
                        + "3. Short Answer Question\n"
        );

        String in = keyboard.nextLine().toLowerCase();
        String question;
        String answer;

        switch (in) {
            case "1":
                System.out.println("Enter the question:");
                question = keyboard.nextLine();
                System.out.println("Is it true or false?");
                answer = keyboard.nextLine();
                insertTFQuestion(question, answer);
                break;

            case "2":
                System.out.println("Enter the question:");
                question = keyboard.nextLine();
                System.out.println("Enter the correct answer:");
                answer = keyboard.nextLine();
                System.out.println("Enter incorrect answer one:");
                String incorrect1 = keyboard.nextLine();
                System.out.println("Enter incorrect answer two:");
                String incorrect2 = keyboard.nextLine();
                System.out.println("Enter incorrect answer three:");
                String incorrect3 = keyboard.nextLine();
                insertMCQuestion(question, answer, new String[]{incorrect1, incorrect2, incorrect3});
                break;

            case "3":
                System.out.println("Enter the question:");
                question = keyboard.nextLine();
                System.out.println("Enter the correct answer:");
                answer = keyboard.nextLine();
                insertSAQuestion(question, answer);
                break;
        }
    }

    private static void moveToRoom(Direction dir)
    {
        if (currentRoom.canTravelTo(dir)) {
            if (askQuestion()) {
                currentRoom = currentRoom.getRoomIn(dir);
            } else {
                currentRoom.closeDoor(dir);
            }
        } else {
            System.out.println("The door to that room is locked...");
        }
    }

    private static boolean askQuestion()
    {
        // Getting question
        Question currentQuestion = questions.remove(questions.size() - 1);
        if (questions.size() == 0) {
            //initQuestions();
            getDBQuestions();
        }

        // Asking question
        System.out.println(currentQuestion);
        String answer = "";
        while (answer.length() == 0) { // Waiting for answer
            System.out.print("? ");
            answer = keyboard.nextLine().toLowerCase();
        }

        if (!answer.equals("/fail")
                && (answer.equals("/answer") || currentQuestion.check(answer))) {
            playSound("correct question");
            System.out.println("\n\n\nCorrect! You may proceed.");
            return true;
        } else {
            playSound("incorrect question");
            System.out.println("\n\n\nIncorrect! The door is now locked.");
            return false;
        }
    }

    private static String getFilename(String prefix, String input)
    {
        if (input.length() > prefix.length() + 1) { // prefix and a space
            return input.substring(prefix.length() + 1) + ".sav";
        }
        return null;
    }

    private static void printCommands()
    {
        System.out.println(
                "help: displays this message.\n"
                        + "quit: quits the game.\n"
                        + "add question: adds a question to the DB\n"
                        + "north, up: moves to the north room.\n"
                        + "south, down: move to the south room.\n"
                        + "west, left: move to the west room.\n"
                        + "east, right: move to the east room.\n"
                        + "save <name>: saves the current game.\n"
                        + "load <name>: loads the specified game.\n"
                        + "\nFor most commands, you can enter the first character instead."
        );
    }

    private static void printCheatCommands()
    {
        System.out.println(
                "/: prefix for all cheats; also displays this message.\n"
                        + "answer: will always be a correct answer.\n" // Implemented in askQuestion
                        + "fail: will always be an incorrect answer.\n" // Implemented in askQuestion
                        + "unlock <dir>: unlock the door in the specified direction.\n"
                        + "escape: teleport to the finish.\n"
        );
    }

    private static void playSound(String sound)
    {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("sounds/" + sound + ".wav"));
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            audioClip.start();
        } catch (Exception e) {
            System.err.println("Error trying to play sound " + sound + ".");
        }
    }

    public static Room getCurrentRoom()
    {
        return currentRoom;
    }

    public static Maze getMaze()
    {
        return maze;
    }
}
