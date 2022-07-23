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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class TheaterTests {
	
	private Theater theater = new Theater(LocalDateProvider.singleton(), "src/test/resources/movies.properties", "src/test/resources/discount.properties");
	private LocalDate localDate=LocalDate.of(9999,12,31);
	private LocalDateTime timeOutofRange= LocalDateTime.of(localDate, LocalTime.of(23, 0));
	private LocalDateTime timeInRange= LocalDateTime.of(localDate, LocalTime.of(14, 0));
	Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1);
	Movie spiderManClone = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1);
	Showing showing = theater.getSchedule().get(0);
	
    @Test
    void test_printMovieSchedulePlainText() {
        StringBuilder sbTest= new StringBuilder();
        sbTest.append(LocalDate.now());
        sbTest.append("\n===================================================\n");
        sbTest.append(showing.getSequenceOfTheDay() + ": " + showing.getStartTime() + " " + showing.getMovie().getTitle() + " " + showing.getMovie().getRunningTime() + " $" + showing.getMovieFee() + "\n");
        sbTest.append("===================================================\n");
        assertEquals(sbTest.toString(), theater.printScheduleText());
    }
    
    @Test
    void test_printMovieScheduleJson() {
    	StringBuilder sb= new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		try {
			sb.append(objectMapper.writeValueAsString(showing));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
    	assertEquals(sb.toString(),theater.printScheduleJson());
    }
    
    @Test
    void test_specialMovieWith50PercentDiscount() {
    	showing.setSequenceOfTheDay(5);
        assertEquals(10, theater.calculateTicketPrice(showing));
    }
    
    @Test
    void test_movieWithRangeDiscount() {
    	showing.setStartTime(timeInRange);
    	assertEquals(9.37, theater.calculateTicketPrice(showing));
    }
    
    @Test
    void test_movieWithSequenceDiscount() {
    	showing.getMovie().setSpecialCode(0);
    	showing.setSequenceOfTheDay(2);
    	assertEquals(10.5, theater.calculateTicketPrice(showing));
    }
    

    @Test
    void totalFeeForCustomer() {
    	Reservation reservation = theater.reserve(new Customer("john", "id-12345"), 1, 4);
        assertEquals(reservation.totalFee(), 38);
    }

    
  
}
