package com.jobengine.engine;

import com.jobengine.model.Job;
import com.jobengine.model.JobStatus;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

class JobEngineSmokeTest {

    @Test
    void shouldRunASingleJob() {
        JobEngine engine = new JobEngine();
        AtomicBoolean executed = new AtomicBoolean(false);

        Job job = new Job("1", "hello-world", () -> executed.set(true));

        engine.submit(job);

        assertThat(executed.get()).isTrue();
        assertThat(job.getStatus()).isEqualTo(JobStatus.COMPLETED);
        assertThat(job.getStartedAt()).isNotNull();
        assertThat(job.getCompletedAt()).isNotNull();
    }

    @Test
    void shouldHandleAFailingJob() {
        JobEngine engine = new JobEngine();

        Job job = new Job("2", "kaboom",
                () -> { throw new RuntimeException("Something broke"); });

        engine.submit(job);

        assertThat(job.getStatus()).isEqualTo(JobStatus.FAILED);
        assertThat(job.getFailureReason()).isEqualTo("Something broke");
    }

    @Test
    void shouldNotExecuteAJobThatAlreadyRan() {
        JobEngine engine = new JobEngine();
        AtomicBoolean executed = new AtomicBoolean(false);

        Job job = new Job("3", "already-done", () -> executed.set(true));

        // Manually transition past PENDING — simulate a job that was already handled
        job.transitionTo(JobStatus.PENDING, JobStatus.COMPLETED);

        engine.submit(job);

        assertThat(executed.get()).isFalse();
        assertThat(job.getStatus()).isEqualTo(JobStatus.COMPLETED);
    }
}