package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

/**
 * The FareCalculatorServiceTest class is used to test the FareCalculatorService
 * class
 */
public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
	fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
	ticket = new Ticket();
    }

    /**
     * This method tests the calculation of the parking price for a car
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void calculateFareCar() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    /**
     * This method tests the calculation of the parking price for a bike
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void calculateFareBike() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    /**
     * This method tests the calculation of the parking price for a car with a
     * discount
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void calculateFareCarWithDiscount() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000)); // 1 hour (60 minutes and the user is known then
								       // there is a discount of 5%)
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket, true);
	assertEquals((0.95 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    /**
     * This method tests the calculation of the parking price for a bike with a
     * discount
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void calculateFareBikeWithDiscount() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket, true);
	assertEquals((0.95 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    /**
     * This method tests the calculation of the price with an unknown type of
     * vehicle discount
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void calculateFareUnkownType() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    /**
     * This method tests the calculation of the price with time of entry after the
     * exit time discount
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void calculateFareBikeWithFutureInTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    /**
     * This method tests the calculation of the price for a bike a duration in
     * parking under one hour
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
								      // parking fare
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    /**
     * This method tests the calculation of the price for a car a duration in
     * parking under one hour
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
								      // parking fare
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    /**
     * This method tests the calculation of the price for a car with a duration more
     * than a day
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void calculateFareCarWithMoreThanADayParkingTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
									   // parking fare per hour
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    /**
     * This method tests the calculation of the price for a car with a duration
     * under 30 minutes
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void calculateFareCarWithLessThan30minutesParkingTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis());

	Date outTime = new Date(inTime.getTime() + (29 * 60 * 1000)); // add 29 minutes in order to be sure, the outTime
								      // will be under 29 minutes.
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((0.00 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    /**
     * This method tests the calculation of the price for a bike with a duration
     * under 30 minutes
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void calculateFareBikeWithLessThan30minutesParkingTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis());

	Date outTime = new Date(inTime.getTime() + (29 * 60 * 1000)); // add 29 minutes in order to be sure, the outTime
								      // will be under 29 minutes.
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((0.00 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

}
