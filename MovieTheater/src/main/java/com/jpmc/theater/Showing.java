package com.jpmc.theater;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.math3.util.Precision;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Showing {
  
    private Movie movie;
    private int sequenceOfTheDay;
    private LocalDateTime showStartTime;
    
    /**
     * 
     * @param movie movie
     * @param sequenceOfTheDay sequence for discounts
     * @param showStartTime start time
     */
    public Showing(Movie movie, int sequenceOfTheDay, LocalDateTime showStartTime) {
        this.movie = movie;
        this.sequenceOfTheDay = sequenceOfTheDay;
        this.showStartTime = showStartTime;
    }
   

    public Movie getMovie() {
        return movie;
    }
    
    public void setMovie(Movie movie) {
    	this.movie=movie;
    }

    public LocalDateTime getStartTime() {
        return showStartTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
    	this.showStartTime=startTime;
    }

    public boolean isSequence(int sequence) {
        return this.sequenceOfTheDay == sequence;
    }

    public double getMovieFee() {
        return movie.getTicketPrice();
    }

    public int getSequenceOfTheDay() {
        return sequenceOfTheDay;
    }
    
    public void setSequenceOfTheDay(int sequence) {
    	this.sequenceOfTheDay=sequence;
    }
    
}
