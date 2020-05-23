package maze;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import maze.Room.Direction;

public class Main
{
    private static final int HEIGHT = 4;
    private static final int WIDTH = 4;

    private static Scanner keyboard;
    private static Random rnd = new Random();

    private static Maze maze;
    private static Room currentRoom;
    private static ArrayList<Question> questions;


    public static void main(String[] args)
    {
        keyboard = new Scanner(System.in);
        boolean quit;
        String in;

        while(true) {
            initGame(WIDTH, HEIGHT);

            quit = playGame();
            if (quit) {
                break;
            }

            System.out.println("\n" + maze.getDisplay(currentRoom));
            System.out.println("Do you want to play again? (y/n)");
            in = keyboard.nextLine().toLowerCase();
            if (!in.startsWith("y")) {
                break;
            }
        }

        keyboard.close();
    }

    private static void initGame(int mazeWidth, int mazeHeight)
    {
        maze = new Maze(mazeWidth, mazeHeight);
        currentRoom = maze.getStart();

        initQuestions();
    }

    private static void initQuestions()
    {
        questions = new ArrayList<Question>();

        questions.add(new TrueFalse("It's true", true));
        questions.add(new MultipleChoice("take a guess", "Yup", "Wrong", "Wrong", "Wrong"));
        questions.add(new ShortAnswer("George Bush", "George Bush"));

        Collections.shuffle(questions, rnd);
    }

    private static boolean playGame()
    {
        // Returns whether the game should immediately quit or not

        String in;

        while (true) {
            // Prompting player
            System.out.println("\n" + maze.getDisplay(currentRoom));
            System.out.println("Enter your command. Entering 'help' will show a list of commands.");

            // Getting command
            while (true) {
                System.out.print("> ");
                in = keyboard.nextLine().toLowerCase();
                System.out.print("\n\n\n");

                if (in.equals("help")) {
                    printCommands();
                    continue;
                } else if (in.startsWith("q")) { //quit
                    return true;

                } else if (in.startsWith("save")) {
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
                    } catch (Exception e) {
                        System.out.println("Unable to save...");
                        continue;
                    }
                } else if (in.startsWith("load")) {
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
                    } catch (Exception e) {
                        System.out.println("Unable to load...");
                        continue;
                }

                // Directions
                } else if (in.startsWith("n") || in.equals("up")) {
                    moveToRoom(Direction.UP);
                } else if (in.startsWith("s") || in.equals("down")) {
                    moveToRoom(Direction.DOWN);
                } else if (in.startsWith("w") || in.equals("left")) {
                    moveToRoom(Direction.LEFT);
                } else if (in.startsWith("e") || in.equals("right")) {
                    moveToRoom(Direction.RIGHT);

                } else if (in.startsWith("/")) {
                    if (in.equals("/")) {
                        printCheatCommands();
                        continue;

                    } else if (in.startsWith("/unlock")) {
                        Direction dir;
                        if (in.endsWith("up")) {
                            dir = Direction.UP;
                        } else if (in.endsWith("down")) {
                            dir = Direction.DOWN;
                        } else if (in.endsWith("left")) {
                            dir = Direction.LEFT;
                        } else if (in.endsWith("right")) {
                            dir = Direction.RIGHT;
                        } else {
                            System.out.println("Unknown direction. Please enter up, down, left, or right.");
                            continue;
                        }

                        currentRoom.openDoor(dir);
                    } else if (in.startsWith("/escape")) {
                        currentRoom = maze.getFinish();
                    }

                } else {
                    System.out.println("Unknown command. Please enter 'help' for a list of commands.");
                }

                break;
            }

            // Checking game state
            if (maze.getFinish() == currentRoom) {
                System.out.println("You escaped! Congratulations!");
                break;
            } else if (!maze.isPathToFinish(currentRoom)) {
                System.out.println("There is no escape...");
                break;
            }
        }

        return false;
    }

    private static void printCommands()
    {
        System.out.println(
            "help: displays this message.\n"
            + "quit: quits the game.\n"
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
            initQuestions();
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
            System.out.println("\n\n\nCorrect! You may proceed.");
            return true;
        } else {
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

    //below methods are to allow for access for testing
    public static Room getCurrentRoom()
    {
        return currentRoom;
    }
    public static Maze getMaze()
    {
        return maze;
    }
}
