package com.yahoo.reportr.data.entity;

/**
 * Created by bhavanis on 1/12/16.
 */
public class Submission {

    private Integer submissionId;
    private String tumblrUrl;
    private Integer reporterId;
    private Integer topicId;
    private Long evaluateTime;
    private Long createdTime;
    private Boolean accepted;
    private String reporterEmail;
    private Long postId;
    private String blogName;

    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getReporterId() {
        return reporterId;
    }

    public void setReporterId(Integer reporterId) {
        this.reporterId = reporterId;
    }

    public String getTumblrUrl() {
        return tumblrUrl;
    }

    public void setTumblrUrl(String tumblrUrl) {
        this.tumblrUrl = tumblrUrl;
    }

    public Long getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(Long evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    @Override
    public String toString() {
        return "Submission [submissionId=" + submissionId + ", tumblrUrl=" + tumblrUrl + ", reporterId=" + reporterId
                + ", topicId=" + topicId + ", evaluateTime=" + evaluateTime + ", createdTime=" + createdTime
                + ", accepted=" + accepted + ", reporterEmail=" + reporterEmail + ", postId=" + postId + ", blogName="
                + blogName + "]";
    }
}
