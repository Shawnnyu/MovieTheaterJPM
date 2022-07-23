package com.jpmc.theater;

public class TheaterRunner {
    public static void main(String[] args) {
        Theater theater = new Theater(LocalDateProvider.singleton(),"src/main/resources/movies.properties","src/main/resources/discount.properties");
        System.out.print(theater.printScheduleText());
        System.out.print(theater.printScheduleJson());
    }
}
