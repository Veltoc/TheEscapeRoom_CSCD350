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
    
    //Method structure was if if-else therefore if answer was zero it would auto run the else in truefalse questions.
    //Changed to if-elseif-else. Also removed the try catch because the Integer.parseInt(input) throws that exception everytime 
    //regardless of whether the input is correct or not.
    public boolean check(String input)
    {
        int val = 0;
        if(input.equalsIgnoreCase("T")) val = 0;
        else if(input.equalsIgnoreCase("F")) val = 1;
        else {
        	val = Integer.parseInt(input);
            //Based on how we layout the questions either 1-4 we will have to use val-- or 0-3 (we don't)
        	//val--;
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
