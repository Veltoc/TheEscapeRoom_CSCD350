package maze;

public class TrueFalse extends MultipleChoice
{
    public TrueFalse(String question, boolean isTrue)
    {
        super(question, boolToStr(isTrue), boolToStr(!isTrue));
    }

    @Override
    public boolean check(String answer)
    {
        // Converts t and f into true and false respectively
        // Otherwise checks answer

        if (answer.startsWith("t")) {
            return super.check("true");
        } else if (answer.startsWith("f")) {
            return super.check("false");
        } else {
            return super.check(answer);
        }
    }

    private static String boolToStr(boolean bool)
    {
        if (bool) {
            return "True";
        } else {
            return "False";
        }
    }
}
