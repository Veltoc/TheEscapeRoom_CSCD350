package maze;

import java.util.Arrays;
import java.util.List;

public class ShortAnswer extends Question
{
    public ShortAnswer(String question,String correct)
    {
        super(question,1);
        this.options[0] = correct;//correct index is 0 by default
    }
    @Override
    public boolean check(String input)
    {
        List<String> userInput = Arrays.asList(input.toLowerCase().split(" "));
        String[] solution = options[0].toLowerCase().split(" ");
        for (String sol : solution) {//look into containsAll?
            if(!userInput.contains(sol)) return false;
        }
    return true;
    }
}
