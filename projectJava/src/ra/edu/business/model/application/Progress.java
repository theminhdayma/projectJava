package ra.edu.business.model.application;

public enum Progress {
    PENDING,
    INTERVIEWING,
    WAITING_FOR_INTERVIEW_CONFIRM,
    REQUEST_RESCHEDULE,
    INTERVIEW_SCHEDULED,
    HANDING,
    DESTROYED,
    REJECTED,
    DONE;

    public String getDisplayName() {
        return switch (this) {
            case PENDING -> "Đang xử lý";
            case INTERVIEWING -> "Quá trình phỏng vấn";
            case HANDING -> "Chờ phản hồi";
            case DESTROYED -> "Đã hủy đơn";
            case DONE -> "Hoàn thành";
            case WAITING_FOR_INTERVIEW_CONFIRM -> "Chờ xác nhận lịch phỏng vấn";
            case REJECTED -> "Đã bị từ chối";
            case REQUEST_RESCHEDULE -> "Yêu cầu thay đổi lịch phỏng vấn";
            case INTERVIEW_SCHEDULED -> "Đã xác nhận lịch phỏng vấn";
        };
    }
}