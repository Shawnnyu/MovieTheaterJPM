package com.jpmc.theater;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.util.Precision;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/*
 * Theater class
 * Handles its own showing, and reservations in case of multiple theaters
 * Loads its showings, discounts from properties file
 * Better implementation with spring boot and databases
 */
public class Theater {

    private static int MOVIE_CODE_SPECIAL = 1;  
    LocalDateProvider provider;
    private List<Showing> schedule;
    private List<Movie> movieList;
    private String[] movies;
    private String[] runtimes;
    private String[] prices;
    private String[] codes;
    private String[] schedules;
    //discount for special movie
    private float specialMovieDiscountName;
    //discount for movie within time range
    private float specialMovieDiscountRange;
    private String[] discountSeq;
    private String[] discountPrice;
    private String[] discountRange;
    private Map<Integer,Integer> discountMap;
    
	/**
	 * Creates theater object and loads the showtimes and discounts
	 * 
	 * @param provider date time provider
	 * @param showingFile file with movie properties, show time schedule, discount code
	 * @param discountFile discount properties file with customizable sequence
	 */
    public Theater(LocalDateProvider provider,String showingFile, String discountFile) {
        this.provider = provider;

        setup(showingFile,discountFile);
        LocalTime time = LocalTime.of(9, 0);
        
        for(int i=0;i<schedules.length;i++) {
        	int x=Integer.parseInt(schedules[i]);
        	if(i==0) {
        		schedule.add(new Showing(movieList.get(x), 1,LocalDateTime.of(provider.currentDate(), time)));
        	} else {
        		time=time.plus(movieList.get(x).getRunningTime());
        		schedule.add(new Showing(movieList.get(x), i+1,LocalDateTime.of(provider.currentDate(), time)));
        	}
        }
    }
    /*
     * Should use database to store the movies and discounts
     * Need to find a better way of creating a list of whole objects with multiple attributes from properties file otherwise
     */
    public void setup(String showingFile, String discountFile) {
        movieList= new ArrayList<>();
        schedule= new ArrayList<>();
        //load all the movies
        try(InputStream  input = new FileInputStream(showingFile)){
        	Properties properties = new Properties();
        	properties.load(input);
            movies = properties.getProperty("movies").split(",");
            runtimes= properties.getProperty("duration").split(",");
            prices = properties.getProperty("price").split(",");
            codes= properties.getProperty("code").split(",");
            schedules=properties.getProperty("schedule").split(",");
        } catch (IOException e) {
        	e.printStackTrace();
        }
        
        for(int i=0;i<movies.length;i++) {
        	Movie movie=new Movie(movies[i], Duration.ofMinutes(Integer.parseInt(runtimes[i])), Double.parseDouble(prices[i]), Integer.parseInt(codes[i]));
        	movieList.add(movie);
        }
        //Load all the discount types
    	discountMap=new HashMap<>();
        try(InputStream  input = new FileInputStream(discountFile)){
        	Properties properties = new Properties();
        	properties.load(input);
        	discountSeq=properties.getProperty("discount.sequence").split(",");
        	discountPrice=properties.getProperty("discount.price").split(",");
        	discountRange=properties.getProperty("discount.range").split(",");
        	specialMovieDiscountName=Float.parseFloat(properties.getProperty("discount.special.name"));
        	specialMovieDiscountRange=Float.parseFloat(properties.getProperty("discount.special.range"));
        }catch (IOException e) {
        	e.printStackTrace();
        }
        
        for(int i=0;i<discountSeq.length;i++) {
        	discountMap.put(Integer.parseInt(discountSeq[i]), Integer.parseInt(discountPrice[i]));
        }
    }
	/**
	 * 
	 * @param showSequence showing sequence
	 * @param localTime time for new requirement
	 * @param showing showing
	 * @return max discount
	 */
	private double getDiscount(int showSequence, LocalTime localTime , Showing showing) {
		LocalTime low = null;
		LocalTime high = null;
		if(discountRange.length==2) {
			low=LocalTime.of(Integer.parseInt(discountRange[0]),0);
			high=LocalTime.of(Integer.parseInt(discountRange[1]),0);
		}

		double rangeDiscount=0;
		if(localTime.isAfter(low) && localTime.isBefore(high)) {
			rangeDiscount=showing.getMovie().getTicketPrice() * specialMovieDiscountRange;
		}
		
	    double specialDiscount = 0;
	    if (MOVIE_CODE_SPECIAL == showing.getMovie().getSpecialCode()) {
           specialDiscount = showing.getMovie().getTicketPrice() * specialMovieDiscountName;  // 20% discount for special movie
       }
	       
       double sequenceDiscount = 0;
	       
       if(discountMap.containsKey(showSequence)) {
    	   sequenceDiscount=discountMap.get(showSequence);
       }
       double maxDiscount=Math.max(Math.max(specialDiscount, sequenceDiscount), rangeDiscount);
       maxDiscount=Precision.round(maxDiscount, 2);
       return maxDiscount;

   }
	
    public double calculateFee(int audienceCount, Showing showing) {
        return calculateTicketPrice(showing) * audienceCount;
    }
    
    
    public double calculateTicketPrice(Showing showing) {
        return Precision.round((showing.getMovie().getTicketPrice() - getDiscount(showing.getSequenceOfTheDay(), showing.getStartTime().toLocalTime(), showing)),2);
    }

    public Reservation reserve(Customer customer, int sequence, int howManyTickets) {
        Showing showing;
        try {
            showing = schedule.get(sequence-1);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw new IllegalStateException("not able to find any showing for given sequence " + sequence);
        }
        return new Reservation(customer, showing, howManyTickets, this);
    }
    
    public List<Showing> getSchedule(){
    	return schedule;
    }

    public String printScheduleText() {
    	StringBuilder sb= new StringBuilder();
    	sb.append(provider.currentDate());
    	sb.append("\n===================================================\n");
    	schedule.forEach(s ->
    	sb.append(s.getSequenceOfTheDay() + ": " + s.getStartTime() + " " + s.getMovie().getTitle() + " " + s.getMovie().getRunningTime() + " $" + s.getMovieFee() + "\n")
    	);
    	sb.append("===================================================\n");
    	
    	return sb.toString();
    }
    
    public String printScheduleJson() {
    	StringBuilder sb= new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    	for(Showing s: schedule) {
    		try {
				String json = objectMapper.writeValueAsString(s);
				sb.append(json);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
    	}
    	return sb.toString();
    	
    }
}
