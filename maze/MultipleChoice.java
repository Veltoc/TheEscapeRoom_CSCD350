package maze;

import java.util.Random;

public class MultipleChoice extends Question {
    public MultipleChoice(String question,String correct,String incorrect1,String incorrect2,String incorrect3)
    {
        super(question,4);
        this.options[0] = correct;//correct index is 0 by default
        this.options[1] = incorrect1;
        this.options[2] = incorrect2;
        this.options[3] = incorrect3;
        shuffle();
    }
    public void shuffle()
    {
        Random rnd = new Random();
        for (int i = options.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i+1);
            if(i==correctIndex) correctIndex = index;
            else if(index==correctIndex) correctIndex = i;
            String a = options[index];
            options[index] = options[i];
            options[i] = a;
        }
    }
}
