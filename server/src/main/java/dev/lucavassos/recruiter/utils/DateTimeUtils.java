package dev.lucavassos.recruiter.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeUtils {
    public static final ZoneOffset UTC_OFFSET = ZoneOffset.UTC;

    public static LocalDateTime getCurrentDTime() {
        return LocalDateTime.now(UTC_OFFSET);
    }

    public static double durationInSecondsBetweenNowAnd(Instant then) {
        return (double) Duration.between(then, Instant.now()).toMillis() / 1000;
    }
}
