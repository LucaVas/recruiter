package dev.lucavassos.recruiter.monitoring;

import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;

import java.time.Instant;

import static dev.lucavassos.recruiter.utils.DateTimeUtils.durationInSecondsBetweenNowAnd;

public class MonitoringProcessor {

    private final Counter jobsCounter;
    private final Counter usersCounter;
    private final Counter candidatesCounter;
    private final Counter candidaciesCounter;
    private final Histogram getSingleJob;
    private final Histogram getMultipleJobs;
    private final Histogram getLoginTime;
    private final Histogram getSignupTime;

    public MonitoringProcessor() {
        jobsCounter = Counter.build()
                .name("jobs_added")
                .help("Number of new jobs")
                .register();
        usersCounter = Counter.build()
                .name("new_users")
                .help("Number of new users")
                .register();
        candidatesCounter = Counter.build()
                .name("new_candidates")
                .help("Number of new candidates")
                .register();
        candidaciesCounter = Counter.build()
                .name("new_candidacies")
                .help("Number of new candidacies")
                .register();
        getSingleJob = Histogram.build()
                .name("get_single_job")
                .help("Time taken to get a single job")
                .register();
        getMultipleJobs = Histogram.build()
                .name("get_multiple_jobs")
                .help("Time taken to get multiple jobs")
                .register();
        getLoginTime = Histogram.build()
                .name("get_login_time")
                .help("Time taken to login")
                .register();
        getSignupTime = Histogram.build()
                .name("get_signup_time")
                .help("Time taken to signup")
                .register();
    }

    public void incrementJobsCounter() {
        jobsCounter.inc();
    }
    public void incrementUsersCounter() {
        usersCounter.inc();
    }
    public void incrementCandidatesCounter() {
        candidatesCounter.inc();
    }
    public void incrementCandidaciesCounter() {
        candidaciesCounter.inc();
    }
    public void observeGetSingleJob(Instant start) {
        getSingleJob.observe(durationInSecondsBetweenNowAnd(start));
    }
    public void observeGetMultipleJobs(Instant start) {
        getMultipleJobs.observe(durationInSecondsBetweenNowAnd(start));
    }
    public void observeGetLoginTime(Instant start) {
        getLoginTime.observe(durationInSecondsBetweenNowAnd(start));
    }
    public void observeGetSignupTime(Instant start) {
        getSignupTime.observe(durationInSecondsBetweenNowAnd(start));
    }

}
