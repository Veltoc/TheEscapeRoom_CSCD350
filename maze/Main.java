package maze;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main
{
    private static Maze maze;
    private static final int HEIGHT = 4;
    private static final int WIDTH = 4;
    private static Room currentRoom;
    private  static ArrayList<Question> questions = new ArrayList<Question>();
    private static Random rnd = new Random();
    private static Scanner keyboard = new Scanner(System.in);
    private static boolean win = false;
    private static boolean running = true;
    private static boolean doRemove = false;
    private static final String PASSWORD = "p@ssword";
    public static void main(String[] args)
    {
        while(running)
        {
            win = false;
            maze = new Maze(WIDTH, HEIGHT);
            String in = "";
            currentRoom = maze.getStart();
            questions.add(new TrueFalse("It's true", "true"));
            questions.add(new MultipleChoice("take a guess", "Yup", "Wrong", "Wrong", "Wrong"));
            questions.add(new ShortAnswer("George Bush", "George Bush"));
            do
            {
                System.out.println("Enter a command: up, down, left, right, or q to quit");
                in = keyboard.nextLine().toLowerCase();
                //System.out.println("in: "+in); //for testing
                if (in.equals("q"))
                {
                    running = false;
                    break;
                }
                if (!move(in))
                {
                    System.out.println("There is no escape, do you want to try again? (y/n)");
                    in = keyboard.nextLine().toLowerCase();
                    if (in.equals("n")) running=false;
                    break;
                }
                else if (win)
                {
                    System.out.println("You completed the escape room! Do you want to try again? (y/n)");
                    in = keyboard.nextLine().toLowerCase();
                    if (in.equals("n")) running=false;
                    break;
                }
            }
            while (true);
        }
    }
    private static boolean move(String direction)
    {
        switch (direction)
        {
            case "up": if(currentRoom.canTravelTo(Room.Direction.UP))
            {
                if(throwQuestion()) currentRoom = currentRoom.getRoomIn(Room.Direction.UP);
                else currentRoom.closeDoor(Room.Direction.UP);
            }
            else System.out.println("Room locked");
                break;
            case "down": if(currentRoom.canTravelTo(Room.Direction.DOWN))
            {
                if(throwQuestion()) currentRoom = currentRoom.getRoomIn(Room.Direction.DOWN);
                else currentRoom.closeDoor(Room.Direction.DOWN);
            }
            else System.out.println("Room locked");
                break;
            case "left": if(currentRoom.canTravelTo(Room.Direction.LEFT))
            {
                if(throwQuestion()) currentRoom = currentRoom.getRoomIn(Room.Direction.LEFT);
                else currentRoom.closeDoor(Room.Direction.LEFT);
            }
            else System.out.println("Room locked");
                break;
            case "right": if(currentRoom.canTravelTo(Room.Direction.RIGHT))
            {
                if(throwQuestion()) currentRoom = currentRoom.getRoomIn(Room.Direction.RIGHT);
                else currentRoom.closeDoor(Room.Direction.RIGHT);
            }
            else System.out.println("Room locked");
                break;
            case "/finishcheat": System.out.println("Password?");
                if(keyboard.nextLine().equals(PASSWORD)) {
                    System.out.println("Password accepted, moving to finish.");
                    currentRoom = maze.getFinish();
                }
                break;
            default: System.out.println("Please enter a valid direction");
                break;
        }
        if(maze.getFinish()==currentRoom)
        {
            win = true;
            return true;//saves runtime
        }

        return maze.isPathToFinish(currentRoom);
    }
    private static boolean throwQuestion()
    {
        int rand = rnd.nextInt(questions.size());
        Question currentQuestion = null;
        if(doRemove)
            currentQuestion = questions.remove(rand);
        else
            currentQuestion = questions.get(rand);
        System.out.println(currentQuestion.getQuestion());
        String[] options = currentQuestion.getOptions();
        if(options.length==1)
        {
            System.out.print("Short answer: ");
        }
        else
        {
            int number = 1;
            for (String option : options)
            {
                System.out.println(number + ". " + option);
                number++;
            }
        }
        String query = keyboard.nextLine();
        if(query.equals("/unlock")) {
            System.out.println("Password?");
            if(keyboard.nextLine().equals(PASSWORD)){
                System.out.println("Password accepted, unlocking door");
                return true;
            }
            return false;
        }
        else if(query.equals("/AdminFail")) return false;//her for tests to fail
        else if(currentQuestion.check(query))//I do error prevention in the Question of if its multiple choice or T/F and not a number but the question will return false. Consider earlier checking
        {
            System.out.println("Correct!");
            return true;
        }
        System.out.println("Incorrect");
        return false;
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