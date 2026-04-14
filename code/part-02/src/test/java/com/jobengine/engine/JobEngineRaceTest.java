package com.jobengine.engine;

import com.jobengine.model.Job;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class JobEngineRaceTest {

    private JobEngine engine;

    @AfterEach
    void tearDown() throws InterruptedException {
        if (engine != null) {
            engine.shutdown();
        }
    }

    /**
     * Submit the same job twice. Two worker threads race to execute it.
     * We expect the task to run exactly once.
     *
     * @RepeatedTest because race conditions are timing-dependent.
     */
    @RepeatedTest(50)
    void jobShouldExecuteExactlyOnce() throws InterruptedException {
        engine = new JobEngine(2);
        AtomicInteger runCount = new AtomicInteger(0);

        Job job = new Job("1", "payment", () -> runCount.incrementAndGet());

        // Both workers race to execute the same job
        engine.submit(job);
        engine.submit(job);

        engine.shutdown();
        engine.awaitTermination(5, TimeUnit.SECONDS);

        assertThat(runCount.get()).isEqualTo(1); // FAILS — sometimes 2
    }
}