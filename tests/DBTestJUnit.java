package tests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import maze.MultipleChoice;
import maze.ShortAnswer;
import maze.TrueFalse;
import maze.Main;


public class DBTestJUnit {

	//The Test Question Entrys will add those questions to the DB so be mindful when running
	//this test. It will add many replicated questions into the question pool.
	@Test
	
	public void test_DBConnection() throws Exception {
		maze.Main main = new Main();
		
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        main.connectDatabase();
        outContent.close();
		assertEquals("SQLite DB connected\n", outContent.toString());
		
	}
	
	@Test
	public void test_TFEntry() throws Exception 
	{
		maze.Main main = new Main();
		main.connectDatabase();
		main.getDBQuestions();
		
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
		main.insertTFQuestion("Is the sky blue?", "true");
		
		outContent.close();
		assertEquals("TF has been inserted!\n", outContent.toString());
	}

	@Test
	public void test_MCEntry() throws Exception 
  {
		maze.Main main = new Main();
		main.connectDatabase();
		main.getDBQuestions();
		String[] incorrectQs = {"red", "green", "yellow"};
		
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
		main.insertMCQuestion("What color is the sky","blue", incorrectQs);
		outContent.close();
		assertEquals("MC has been inserted!\n", outContent.toString());
	}
	
	
	
	
	
	@Test
	public void test_SAEntry() throws Exception 
  {
		maze.Main main = new Main();
		main.connectDatabase();
		main.getDBQuestions();

		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
		
		Main.insertSAQuestion("What color is blue","blue");
		outContent.close();
		assertEquals("SA has been inserted!\n", outContent.toString());
	}
	
	
	@Test
	public void test_getDBQuestions() throws Exception 
    {
		maze.Main main = new Main();
		main.connectDatabase();
		
		Exception ex = null;
		
		try {
			main.getDBQuestions();
		}catch (Exception e) {
			ex = e;
		}
		assertEquals(null, ex);
	}
	
}
