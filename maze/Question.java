package maze;

import java.util.ArrayList;

public abstract class Question
{
    private String question;
    private String answer;
    private ArrayList<String> options;


    public Question(String question, String answer, String... incorrectOptions)
    {
        this.question = question;
        this.answer = answer;

        this.options = new ArrayList<String>(incorrectOptions.length + 1);
        this.options.add(answer);
        for (String option: incorrectOptions) {
            this.options.add(option);
        }
    }


    @Override
    public abstract String toString(); // Forces children to implement method
    public abstract boolean check(String guess);

    public String getQuestion()
    {
        return this.question;
    }

    public String[] getOptions()
    {
        // Creating the String array tells the ArrayList to create a String array of that size
        // Not including the argument creates an Object array
        return this.options.toArray(new String[this.options.size()]);
    }

    public String getAnswer()
    {
        return this.answer;
    }

    // Public for unit testing purposes; would otherwise be protected
    public ArrayList<String> getOptionArray()
    {
        return this.options;
    }
}
