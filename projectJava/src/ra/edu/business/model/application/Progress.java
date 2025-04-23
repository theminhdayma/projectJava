package ra.edu.business.model.application;

public enum Progress {
    PENDING,
    INTERVIEWING,
    DESTROYED,
    DONE;

    public String getDisplayName() {
        return switch (this) {
            case PENDING -> "Đang xử lý";
            case INTERVIEWING -> "Quá trình phỏng vấn";
            case DESTROYED -> "Đã hủy đơn";
            case DONE -> "Hoàn thành";
        };
    }
}