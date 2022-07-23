package com.jpmc.theater;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.math3.util.Precision;

public class Movie {

    private String title;
    private String description;
    private Duration runningTime;
    private double ticketPrice;
    private int specialCode;
/**
 * 
 * @param title movie title
 * @param runningTime movie running time
 * @param ticketPrice movie ticket price
 * @param specialCode special code for discount
 */
    public Movie(String title, Duration runningTime, double ticketPrice, int specialCode) {
        this.title = title;
        this.runningTime = runningTime;
        this.ticketPrice = ticketPrice;
        this.specialCode = specialCode;
    }
	
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
    	this.title=title;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public void setDescription(String description) {
    	this.description=description;
    }
    
    public int getSpecialCode() {
    	return specialCode;
    }
    
    public void setSpecialCode(int specialCode) {
    	this.specialCode=specialCode;
    }

    public Duration getRunningTime() {
        return runningTime;
    }
    
    public void setRunningTime(Duration runningTime) {
    	this.runningTime=runningTime;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }
    
    public void setTicketPrice(double ticketPrice) {
    	this.ticketPrice=ticketPrice;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Double.compare(movie.ticketPrice, ticketPrice) == 0
                && Objects.equals(title, movie.title)
                && Objects.equals(description, movie.description)
                && Objects.equals(runningTime, movie.runningTime)
                && Objects.equals(specialCode, movie.specialCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, runningTime, ticketPrice, specialCode);
    }
}