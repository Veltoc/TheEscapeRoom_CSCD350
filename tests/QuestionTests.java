package tests;

import maze.MultipleChoice;
import maze.Question;
import maze.ShortAnswer;
import maze.TrueFalse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class QuestionTests {

	@Test
	void test_TrueFalse() 
	{
		TrueFalse tf = new TrueFalse("Is true true", "true");
		assertTrue(tf.check("t"));
	}
	
	
	@Test
	void test_MultipleChoice() 
	{
		MultipleChoice mc = new MultipleChoice("Which is Correct", "correct", "incorrect", "incorrect", "incorrect");
		
		//Because we shuffle the order of possible choices we have to getCorrectIndex() in order to consistently 
		//get the correct answer location. We can't use a concrete number because the correct answer is shuffled
		assertTrue(mc.check(Integer.toString(mc.getCorrectIndex())));
		
	}
	
	@Test
	void test_ShortAnswer() 
	{
		ShortAnswer sa = new ShortAnswer("What is the color of the sky", "blue");
		assertTrue(sa.check("blue"));
	}

}
