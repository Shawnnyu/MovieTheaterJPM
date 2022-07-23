package com.jpmc.theater;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CustomerTests {
	
	private Customer john = new Customer("John Doe", "id-12345");
	private Customer johnClone = new Customer("John Doe", "id-12345");
	
	  @Test
	    void test_CustomerEquals() {
	    	assertEquals(johnClone, john);
	    	assertTrue(john.equals(john));
	    }
	    
	    @Test
	    void test_CustomerNotEqualsName() {
	    	johnClone.setName("jane");
	    	assertNotEquals(john, johnClone);
	    }
	    
	    @Test
	    void test_CustomerNotEqualsId() {
	    	johnClone.setId("id-23456");
	    	assertNotEquals(john, johnClone);
	    }
	    
	    @Test
	    void test_CustomerNull() {
	    	assertFalse(john.equals(null));
	    }
	    
	    
	    @Test
	    void test_hashCodeEquals() {
	    	assertEquals(john.hashCode(), johnClone.hashCode());
	    }
	    
	    @Test
	    void test_toStringEquals() {
	    	assertEquals(john.toString(), johnClone.toString());
	    }

}
