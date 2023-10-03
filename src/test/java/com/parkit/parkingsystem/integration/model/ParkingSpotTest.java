package com.parkit.parkingsystem.integration.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

/**
 * The ParkingSpotTest class is used to test the ParkingSpot class
 */
public class ParkingSpotTest {

    private static final Logger logger = LogManager.getLogger("TicketTest");

    ParkingSpot parkingSpot;

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
	parkingSpot = new ParkingSpot(0, ParkingType.CAR, false);

	// WHEN
	parkingSpot.setId(1);

	// THEN
	assertEquals(1, parkingSpot.getId());
    }

    /**
     * This method tests the setter and the getter of the ParkingType attribute
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void SetAndGetParkingTypeTest() {
	// GIVEN
	parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	// WHEN
	parkingSpot.setParkingType(ParkingType.BIKE);

	// THEN
	assertEquals(ParkingType.BIKE, parkingSpot.getParkingType());
	assertEquals(false, parkingSpot.isAvailable());
    }

    /**
     * This method tests the setter and the getter of the isAvailable attribute
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void SetAndGetAvailableTest() {
	// GIVEN
	parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	// WHEN
	parkingSpot.setAvailable(true);

	// THEN
	assertEquals(true, parkingSpot.isAvailable());
    }

    /**
     * This method tests the equals method which returns True
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void equalsTrueTest() {
	// GIVEN
	parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	// WHEN
	boolean result = parkingSpot.equals(parkingSpot);

	// THEN
	assertTrue(result);
    }

    /**
     * This method tests the equals method which returns False
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void equalsFalseTest() {
	// GIVEN
	parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	ParkingSpot nullParkingSpot = null;

	// WHEN
	boolean result = parkingSpot.equals(nullParkingSpot);

	// THEN
	assertFalse(result);
	assertEquals(1, parkingSpot.hashCode());
    }
}
