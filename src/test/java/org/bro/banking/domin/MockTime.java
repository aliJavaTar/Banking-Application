package org.bro.banking.domin;

import java.time.Clock;
import java.time.LocalDate;

public class MockTime {
    private final Clock clock;

    public MockTime(Clock clock) {
        this.clock = clock;
    }

    public LocalDate getCurrentDate() {
        return LocalDate.now(clock);
    }
}
