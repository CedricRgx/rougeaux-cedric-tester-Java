package com.parkit.parkingsystem.integration.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

/**
 * The TicketTest class is used to test the Ticket class
 */
public class TicketTest {

    private static final Logger logger = LogManager.getLogger("TicketTest");

    Ticket ticket;

    /**
     * This method tests the setter and the getter of the Id attribute
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void SetAndGetIdTest() {
	// GIVEN
	ticket = new Ticket();

	// WHEN
	ticket.setId(1);

	// THEN
	assertEquals(1, ticket.getId());
    }

    /**
     * This method tests the setter and the getter of the ParkingSpot attribute
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void setAndGetParkingSpotTest() {
	// GIVEN
	ticket = new Ticket();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	// WHEN
	ticket.setParkingSpot(parkingSpot);

	// THEN
	assertNotNull(ticket.getParkingSpot());
    }

    /**
     * This method tests the setter and the getter of the VehicleRegNumber attribute
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void setAndGetVehicleRegNumberTest() {
	// GIVEN
	ticket = new Ticket();

	// WHEN
	ticket.setVehicleRegNumber("RVGRVG");

	// THEN
	assertEquals("RVGRVG", ticket.getVehicleRegNumber());
    }

    /**
     * This method tests the setter and the getter of the Price attribute
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void setAndGetPriceTest() {
	// GIVEN
	ticket = new Ticket();

	// WHEN
	ticket.setPrice(15.9);

	// THEN
	assertEquals(15.9, ticket.getPrice());
    }

    /**
     * This method tests the setter and the getter of the InTime attribute
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void setAndGetInTimeTest() {
	// GIVEN
	ticket = new Ticket();
	Date inTimeDate = new Date(System.currentTimeMillis() - (1 * 60 * 60 * 1000));

	// WHEN
	ticket.setInTime(inTimeDate);

	// THEN
	assertEquals(inTimeDate, ticket.getInTime());
    }

    /**
     * This method tests the setter and the getter of the InTime attribute
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void setAndGetOutTimeTest() {
	// GIVEN
	ticket = new Ticket();
	Date outTimeDate = new Date(System.currentTimeMillis());

	// WHEN
	ticket.setOutTime(outTimeDate);

	// THEN
	assertEquals(outTimeDate, ticket.getOutTime());
    }
}
