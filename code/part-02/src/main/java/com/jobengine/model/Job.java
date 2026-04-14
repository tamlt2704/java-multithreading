package com.jobengine.model;

import java.time.Instant;

public class Job {

    private final String id;
    private final String name;
    private final Runnable task;

    // ⚠️ BUG: plain field — no thread safety
    private JobStatus status = JobStatus.PENDING;
    private Instant startedAt;
    private Instant completedAt;
    private String failureReason;

    public Job(String id, String name, Runnable task) {
        this.id = id;
        this.name = name;
        this.task = task;
    }

    // ⚠️ BUG: check-then-act is NOT atomic
    public boolean transitionTo(JobStatus expected, JobStatus next) {
        if (this.status == expected) {
            this.status = next;
            return true;
        }
        return false;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public Runnable getTask() { return task; }
    public JobStatus getStatus() { return status; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public String getFailureReason() { return failureReason; }

    public void setStartedAt(Instant t) { this.startedAt = t; }
    public void setCompletedAt(Instant t) { this.completedAt = t; }
    public void setFailureReason(String r) { this.failureReason = r; }
}