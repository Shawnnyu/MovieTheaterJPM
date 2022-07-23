package com.jpmc.theater;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class MovieTests {
	private Theater theater = new Theater(LocalDateProvider.singleton(), "src/test/resources/movies.properties", "src/main/resources/discount.properties");
	private LocalDate localDate=LocalDate.of(9999,12,31);
	private LocalDateTime timeOutofRange= LocalDateTime.of(localDate, LocalTime.of(23, 0));
	private LocalDateTime timeInRange= LocalDateTime.of(localDate, LocalTime.of(14, 0));
	Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1);
	Movie spiderManClone = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1);
	Showing showing = new Showing(spiderMan, 5, timeOutofRange);
	
	
    @Test
    void test_specialMovieWith50PercentDiscount() {
        assertEquals(10, theater.calculateTicketPrice(showing));
    }
    
    @Test
    void test_movieWithRangeDiscount() {
    	showing = new Showing(spiderMan, 5, timeInRange);
    	assertEquals(9.37, theater.calculateTicketPrice(showing));
    }
    
    @Test
    void test_movieWithSequenceDiscount() {
    	spiderMan.setSpecialCode(0);
    	showing = new Showing(spiderMan, 2, timeOutofRange);
    	assertEquals(10.5, theater.calculateTicketPrice(showing));
    }
    
    @Test
    void test_movieEquals() {
    	assertEquals(spiderMan, spiderManClone);
    	assertTrue(spiderMan.equals(spiderManClone));
    }
    
    @Test
    void test_movieHashCodeEquals() {
    	assertEquals(spiderMan.hashCode(), spiderManClone.hashCode());
    }
    
    @Test
    void test_movieNull(){
    	assertFalse(spiderMan.equals(null));
    }
    
    @Test
    void test_movieNotEqualsName() {
    	spiderManClone.setTitle("Spider-Man: Far From Home");
    	assertFalse(spiderMan.equals(spiderManClone));
    	assertNotEquals(spiderMan, spiderManClone);
    }
    
    @Test
    void test_movieNotEqualsPrice() {
    	spiderManClone.setTicketPrice(1);
    	assertFalse(spiderMan.equals(spiderManClone));
    	assertNotEquals(spiderMan, spiderManClone);
    }
    
    @Test
    void test_movieNotEqualsDuration() {
    	spiderManClone.setRunningTime(Duration.ofMinutes(80));
    	assertFalse(spiderMan.equals(spiderManClone));
    	assertNotEquals(spiderMan, spiderManClone);
    }
    
    @Test
    void test_movieNotEqualsCode() {
    	spiderManClone.setSpecialCode(2);
    	assertFalse(spiderMan.equals(spiderManClone));
    	assertNotEquals(spiderMan, spiderManClone);
    }
    
    @Test
    void test_movieNotEqualsDescription() {
    	spiderManClone.setDescription("Description");
    	assertFalse(spiderMan.equals(spiderManClone));
    	assertNotEquals(spiderMan, spiderManClone);
    }
}
