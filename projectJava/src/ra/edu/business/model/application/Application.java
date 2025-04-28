package ra.edu.business.model.application;

import ra.edu.validate.application.ApplicationValidate;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static ra.edu.MainApplication.scanner;

public class Application {
    private int id;
    private int candidateId;
    private int recruitmentPositionId;
    private String cvUrl;
    private Progress progress;
    private ResultInterview resultInterview;
    private LocalDate createAt;
    private LocalDateTime interviewDate;
    private LocalDateTime confirmInterviewDate;
    private String confirmInterviewDateReason;
    private String destroyReason;
    private LocalDate rejectedAt;
    private String rejectedReason;
    private LocalDate destroyDate;
    private String nodeDone;

    public Application() {
    };

    public Application(int id, int candidateId, int recruitmentPositionId, String cvUrl, Progress progress, ResultInterview resultInterview, LocalDate createAt, LocalDateTime interviewDate, LocalDateTime confirmInterviewDate,String confirmInterviewDateReason, String destroyReason, LocalDate rejectedAt, String rejectedReason, LocalDate destroyDate, String nodeDone) {
        this.id = id;
        this.candidateId = candidateId;
        this.recruitmentPositionId = recruitmentPositionId;
        this.cvUrl = cvUrl;
        this.progress = progress;
        this.resultInterview = resultInterview;
        this.createAt = createAt;
        this.interviewDate = interviewDate;
        this.confirmInterviewDate = confirmInterviewDate;
        this.confirmInterviewDateReason = confirmInterviewDateReason;
        this.destroyReason = destroyReason;
        this.rejectedAt = rejectedAt;
        this.rejectedReason = rejectedReason;
        this.destroyDate = destroyDate;
        this.nodeDone = nodeDone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public int getRecruitmentPositionId() {
        return recruitmentPositionId;
    }

    public void setRecruitmentPositionId(int recruitmentPositionId) {
        this.recruitmentPositionId = recruitmentPositionId;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }
    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public String getDestroyReason() {
        return destroyReason;
    }

    public void setDestroyReason(String destroyReason) {
        this.destroyReason = destroyReason;
    }

    public LocalDate getDestroyDate() {
        return destroyDate;
    }

    public void setDestroyDate(LocalDate destroyDate) {
        this.destroyDate = destroyDate;
    }

    public void inputData() {
        this.cvUrl = ApplicationValidate.validateCvUrl(scanner);
    }

    public LocalDateTime getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDateTime interviewDate) {
        this.interviewDate = interviewDate;
    }

    public ResultInterview getResultInterview() {
        return resultInterview;
    }

    public void setResultInterview(ResultInterview resultInterview) {
        this.resultInterview = resultInterview;
    }

    public LocalDateTime getConfirmInterviewDate() {
        return confirmInterviewDate;
    }

    public void setConfirmInterviewDate(LocalDateTime confirmInterviewDate) {
        this.confirmInterviewDate = confirmInterviewDate;
    }

    public String getConfirmInterviewDateReason() {
        return confirmInterviewDateReason;
    }

    public void setConfirmInterviewDateReason(String confirmInterviewDateReason) {
        this.confirmInterviewDateReason = confirmInterviewDateReason;
    }

    public LocalDate getRejectedAt() {
        return rejectedAt;
    }

    public void setRejectedAt(LocalDate rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }
}

