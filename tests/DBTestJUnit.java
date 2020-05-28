package tests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import maze.Main;


public class DBTestJUnit {

	//The Test Question Entrys will add those questions to the DB so be mindful when running
	//this test. It will add many replicated questions into the question pool.
	@Test
	public void test_DBConnection() throws Exception {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Main.connectDatabase();
        outContent.close();
		assertEquals("SQLite DB connected\n", outContent.toString());
		
	}
	
	@Test
	public void test_TFEntry() throws Exception 
	{
		Main.connectDatabase();
		Main.getDBQuestions();
		
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
		Main.insertTFQuestion("Is the sky blue?", "true");
		
		assertTrue(outContent.toString().contains("True/False question inserted!"));
		outContent.close();
	}

	@Test
	public void test_MCEntry() throws Exception 
  {
		Main.connectDatabase();
		Main.getDBQuestions();
		String[] incorrectQs = {"red", "green", "yellow"};
		
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		Main.insertMCQuestion("What color is the sky","blue", incorrectQs);

		assertTrue(outContent.toString().contains("Multiple Choice question inserted!"));
		outContent.close();
	}
	
	@Test
	public void test_SAEntry() throws Exception 
	{
		Main.connectDatabase();
		Main.getDBQuestions();

		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
		
		Main.insertSAQuestion("What color is blue","blue");

		assertTrue(outContent.toString().contains("Short Answer question inserted!"));
		outContent.close();
	}
	
	@Test
	public void test_getDBQuestions() throws Exception 
    {
		Main.connectDatabase();
		
		Exception ex = null;
		
		try {
			Main.getDBQuestions();
		}catch (Exception e) {
			ex = e;
		}
		assertEquals(null, ex);
	}
}
