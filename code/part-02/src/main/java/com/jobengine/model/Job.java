package com.jobengine.model;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

public class Job {

    private final String id;
    private final String name;
    private final Runnable task;

    // ✅ FIX: AtomicReference instead of plain field
    private final AtomicReference<JobStatus> status = new AtomicReference<>(JobStatus.PENDING);
    private volatile Instant startedAt;
    private volatile Instant completedAt;
    private volatile String failureReason;

    public Job(String id, String name, Runnable task) {
        this.id = id;
        this.name = name;
        this.task = task;
    }

    // ✅ FIX: CAS fuses check + write into one atomic CPU instruction
    public boolean transitionTo(JobStatus expected, JobStatus next) {
        return status.compareAndSet(expected, next);
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public Runnable getTask() { return task; }
    public JobStatus getStatus() { return status.get(); }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public String getFailureReason() { return failureReason; }

    public void setStartedAt(Instant t) { this.startedAt = t; }
    public void setCompletedAt(Instant t) { this.completedAt = t; }
    public void setFailureReason(String r) { this.failureReason = r; }
}