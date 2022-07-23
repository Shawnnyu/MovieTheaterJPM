package com.jpmc.theater;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.math3.util.Precision;

public class Reservation {

    private Customer customer;
    private Showing showing;
    private int audienceCount;
    private Theater theater;
    
	/**
	 * 
	 * @param customer customer
	 * @param showing showing
	 * @param audienceCount number of orders
	 * @param theater
	 */
    public Reservation(Customer customer, Showing showing, int audienceCount, Theater theater) {
        this.customer = customer;
        this.showing = showing;
        this.audienceCount = audienceCount;
        this.theater = theater;
    }

    public double totalFee() {
        return theater.calculateFee(audienceCount, showing);
    }
    

	
}