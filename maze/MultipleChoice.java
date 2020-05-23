package maze;

import java.util.Collections;

public class MultipleChoice extends Question
{
    public MultipleChoice(String question, String answer, String... incorrectOptions)
    {
        super(question, answer, incorrectOptions);
    }


    @Override
    public String toString()
    {
        StringBuilder strBuilder = new StringBuilder();
        Collections.shuffle(getOptionArray());

        strBuilder.append(getQuestion() + '\n');
        for (int i = 0; i < getOptionArray().size(); i++) {
            strBuilder.append(Integer.toString(i + 1) + ". " + getOptionArray().get(i) + '\n');
        }

        return strBuilder.toString();
    }

    @Override
    public boolean check(String guess)
    {
        if (guess.equals(getAnswer().toLowerCase())) {
            return true;
        } else {
            // Trying to interpret as option number
            try {
                int optionNum = Integer.parseInt(guess) - 1;
                if (getOptions()[optionNum] == getAnswer()) {
                    return true;
                } else {
                    return false;
                }

            // Not a number, so incorrect option
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}
