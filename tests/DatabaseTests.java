package tests;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import maze.Question;
import maze.QuestionManager;

public class DatabaseTests {
    @Before
    public void connect() {
        QuestionManager.connect();
    }

    @After
    public void close() {
        QuestionManager.close();
    }

    @Test
    public void test_connection() {
        assertNotEquals(QuestionManager.getConnection(), null);
    }

    @Test
    public void test_addTrueFalse() {
        String prompt = "True/False Question";
        String answer = "True/False Answer";

        QuestionManager.addQuestionToDatabase("True/False", prompt, answer, null);
        assertTrue(validateEntry("TrueFalse", prompt, answer, null));
    }

    @Test
    public void test_addMultipleChoice() {
        String prompt = "Multiple Choice Question";
        String answer = "Multiple Choice Answer";
        String[] options = { "Multiple Choice Option 1", "Multiple Choice Option 2", "Multiple Choice Option 3" };

        QuestionManager.addQuestionToDatabase("True/False", prompt, answer, options);
        assertTrue(validateEntry("TrueFalse", prompt, answer, options));
    }

    @Test
    public void test_addShortAnswer() {
        String prompt = "Short Answer Question";
        String answer = "Short Answer Answer";

        QuestionManager.addQuestionToDatabase("True/False", prompt, answer, null);
        assertTrue(validateEntry("TrueFalse", prompt, answer, null));
    }

    @Test
    public void test_getQuestions() {
        ArrayList<Question> questions = QuestionManager.getQuestions();

        assertTrue(questions.size() > 0);
    }


    private boolean validateEntry(String table, String prompt, String answer, String[] options)
    {
        Connection dbConnection;

        try {
            // Connecting to database
            dbConnection = DriverManager.getConnection("jdbc:sqlite:SqliteDBQuestions.db");

            // Getting question table
            ResultSet result = dbConnection.prepareStatement("SELECT * FROM tbl_" + table).executeQuery();

            // Checking if question exists
            while (result.next()) { // Gets next entry
                if (result.getString("Question").equals(prompt) && result.getString("Answer").equals(answer)) {
                    if (table.equals("MultipleChoice")) {
                        for (int i = 0; i < options.length; i++) {
                            if (!result.getString("Incorrect" + Integer.toString(i + 1)).equals(options[i])) {
                                break;
                            }
                        }
                    } else {
                        return true;
                    }
                }
            }

            // Closing
            dbConnection.close();
            result.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
