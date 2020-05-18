package tests;

import static org.junit.Assert.*;

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
		main.connectDatabase();
		
	}
	@Test
	public void test_TFEntry() throws Exception 
  {
		maze.Main main = new Main();
		main.connectDatabase();
		main.insertTFQuestion("Is the sky blue?", "true");
	}
	
	@Test
	public void test_MCEntry() throws Exception 
  {
		maze.Main main = new Main();
		main.connectDatabase();
		String[] incorrectQs = {"red", "green", "yellow"};
		main.insertMCQuestion("What color is the sky","blue", incorrectQs);
	}
	
	@Test
	public void test_SAEntry() throws Exception 
  {
		maze.Main main = new Main();
		main.connectDatabase();
		main.insertSAQuestion("What color is blue","blue");
	}
	
	
	@Test
	public void test_getDBQuestions() throws Exception 
  {
		maze.Main main = new Main();
		main.connectDatabase();
		main.getDBQuestions();
	}
	
}
