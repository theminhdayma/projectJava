package ra.edu.business.model.application;

public enum ResultInterview {
    PASSED, FAILED;
    public String getDisplayName() {
        return switch (this) {
            case PASSED -> "Đậu";
            case FAILED -> "Rớt";
        };
    }
}
