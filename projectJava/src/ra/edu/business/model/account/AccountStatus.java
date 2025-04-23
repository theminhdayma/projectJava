package ra.edu.business.model.account;

public enum AccountStatus {
    ACTIVE, INACTIVE;

    public String getDisplayName() {
        return switch (this) {
            case ACTIVE -> "Hoạt động";
            case INACTIVE -> "Đã khóa";
        };
    }
}
