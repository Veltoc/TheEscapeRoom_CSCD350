package maze;

public class TrueFalse extends Question {
    public TrueFalse(String question,String correct)
    {
        super(question,2);
        if(correct.equalsIgnoreCase("false")) this.correctIndex = 1;//correct index is 0 by default
        this.options[0] = "True";
        this.options[1] = "False";
    }
}
