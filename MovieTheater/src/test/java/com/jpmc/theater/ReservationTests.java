package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReservationTests {
	private Theater theater = new Theater(LocalDateProvider.singleton(), "src/test/resources/movies.properties", "src/main/resources/discount.properties");
	private LocalDate localDate=LocalDate.of(9999,12,31);
	
    @Test
    void totalFee() {
        var customer = new Customer("John Doe", "unused-id");
        var showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                1,
                LocalDateTime.of(localDate, LocalTime.of(9, 0))
        );
        assertEquals(new Reservation(customer, showing, 3, theater).totalFee(), 28.5);
    }
}
