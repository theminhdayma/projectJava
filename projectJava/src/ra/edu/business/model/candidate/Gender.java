package ra.edu.business.model.candidate;

public enum Gender {
    MALE, FEMALE, OTHER;

    public String getDisplayName() {
        return switch (this) {
            case MALE -> "Nam";
            case FEMALE -> "Nữ";
            case OTHER -> "Khác";
        };
    }
}
