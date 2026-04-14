package com.jobengine.engine;

import com.jobengine.model.Job;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JobEngineThroughputTest {

    @Test
    void shouldProcessFourJobsUnder1500ms() {
        JobEngine engine = new JobEngine();
        int jobCount = 4;

        long start = System.currentTimeMillis();

        for (int i = 0; i < jobCount; i++) {
            Job job = new Job("job-" + i, "slow-" + i, () -> {
                try { Thread.sleep(500); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
            engine.submit(job);
        }

        long elapsed = System.currentTimeMillis() - start;

        // FAILS — sequential execution: 4 × 500ms ≈ 2000ms
        assertThat(elapsed).isLessThan(1500);
    }
}