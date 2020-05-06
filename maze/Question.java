package maze;

public abstract class Question {
    String question;
    String[] options;
    int correctIndex;
    public Question()
    {
        this(" ",0);
    }
    public Question(String question, int size)
    {
        this.question = question;
        this.options = new String[size];
        this.correctIndex = 0;
    }
    public boolean check(String input)
    {
        int val = 0;
        if(input.equalsIgnoreCase("T")) val = 0;
        if(input.equalsIgnoreCase("F")) val = 1;
        else {
            try
            {
                val = Integer.parseInt(input);
                val--;
            }
            catch (NumberFormatException e)
            {
                System.out.println("Input not recognizes, please input a corresponding number");
            }
        }

        return val==correctIndex;
    }

    public int getCorrectIndex()
    {
        return correctIndex;
    }

    public String getQuestion()
    {
        return question;
    }

    public String[] getOptions()
    {
        return options;
    }
}
