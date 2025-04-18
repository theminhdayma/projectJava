package ra.edu.validate;

public class StringRule {
    final int minLength;
    final int maxLength;

    public StringRule(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public boolean isValidString(String value) {
        if (value == null) {
            return false;
        }
        return value.length() >= this.minLength && value.length() <= this.maxLength;
    }
}
