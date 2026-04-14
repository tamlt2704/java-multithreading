package com.jobengine.engine;

import com.jobengine.model.Job;
import com.jobengine.model.JobStatus;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobEngine {

    private final ExecutorService workers;

    public JobEngine(int workerCount) {
        this.workers = Executors.newFixedThreadPool(workerCount);
    }

    /** Use default: availableProcessors × 2 (good for I/O-bound jobs). */
    public JobEngine() {
        this(Runtime.getRuntime().availableProcessors() * 2);
    }

    /** Original single-threaded logic — now private, called by workers. */
    private void process(Job job) {
        if (!job.transitionTo(JobStatus.PENDING, JobStatus.RUNNING)) {
            return;
        }

        job.setStartedAt(Instant.now());

        try {
            job.getTask().run();
            job.transitionTo(JobStatus.RUNNING, JobStatus.COMPLETED);
        } catch (Exception e) {
            job.transitionTo(JobStatus.RUNNING, JobStatus.FAILED);
            job.setFailureReason(e.getMessage());
        }

        job.setCompletedAt(Instant.now());
    }

    /** Submit a job to the thread pool for async processing. */
    public void submit(Job job) {
        workers.submit(() -> process(job));
    }

    /** Stop accepting new jobs. Already-submitted jobs will finish. */
    public void shutdown() {
        workers.shutdown();
    }

    /**
     * Block until all submitted jobs complete, or the timeout expires.
     * Call this after shutdown() to wait for in-flight work to drain.
     * Returns true if all jobs finished, false if the timeout was reached.
     */
    public boolean awaitTermination(long timeout, java.util.concurrent.TimeUnit unit)
            throws InterruptedException {
        return workers.awaitTermination(timeout, unit);
    }
}