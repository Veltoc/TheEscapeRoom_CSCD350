package maze;

import java.util.Arrays;
import java.util.List;

public class ShortAnswer extends Question
{
    public ShortAnswer(String question, String answer)
    {
        super(question, answer);
    }


    @Override
    public String toString()
    {
        return getQuestion();
    }

    @Override
    public boolean check(String guess)
    {
        List<String> guessTokens = Arrays.asList(guess.toLowerCase().split(" "));
        List<String> answerTokens = Arrays.asList(getAnswer().toLowerCase().split(" "));

        // Checking if all tokens exist and no more
        if (guessTokens.containsAll(answerTokens) && guessTokens.size() == answerTokens.size()) {
            return true;
        } else {
            return false;
        }
    }
}
