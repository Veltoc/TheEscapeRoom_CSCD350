package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import maze.MultipleChoice;
import maze.ShortAnswer;
import maze.TrueFalse;

public class QuestionTests {

	@Test
	public void test_TrueFalse() 
	{
		TrueFalse question = new TrueFalse("Is true true", true);

		assertTrue(question.check("t"));
		assertTrue(question.check("true"));
		assertFalse(question.check("f"));
		assertFalse(question.check("false"));
		assertFalse(question.check("jibberish"));

		int ansIndex = question.getOptionArray().indexOf(question.getAnswer()) + 1;
		assertTrue(question.check(Integer.toString(ansIndex)));
	}
	
	@Test
	public void test_MultipleChoice() 
	{
		MultipleChoice question = new MultipleChoice("Which is Correct", "Correct", "Incorrect", "INCORRECT");

		assertTrue(question.check("correct"));
		assertFalse(question.check("jibberish"));

		int ansIndex = question.getOptionArray().indexOf(question.getAnswer()) + 1;
		assertTrue(question.check(Integer.toString(ansIndex)));
	}
	
	@Test
	public void test_ShortAnswer() 
	{
		ShortAnswer question = new ShortAnswer("What is the color of the sky", "light blue");

		assertTrue(question.check("light blue"));
		assertTrue(question.check("blue light"));
		assertFalse(question.check("light jibberish"));
		assertFalse(question.check("blue jibberish"));
		assertFalse(question.check("jibberish light"));
		assertFalse(question.check("jibberish blue"));
		assertFalse(question.check("jibberish jibberish"));
	}
}
