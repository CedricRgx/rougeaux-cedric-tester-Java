package com.parkit.parkingsystem.integration.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;

/**
 * The TicketDAOTest class is used to test the TicketDAO class
 */
@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

    private static final Logger logger = LogManager.getLogger("ParkingSpotDAOTest");

    private static ParkingSpotDAO parkingSpotDAO;

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    private static void setUp() throws Exception {
	parkingSpotDAO = new ParkingSpotDAO();
	parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
	dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
	dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {
    }

    /**
     * This method tests the check if a next spot is available in the parking
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void getNextAvailableSlotTest() {
	// GIVEN
	int numberOfAvailableSpotCar;
	int numberOfAvailableSpotBike;

	// WHEN
	numberOfAvailableSpotCar = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
	numberOfAvailableSpotBike = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);

	// THEN
	assertTrue(numberOfAvailableSpotCar > 0);
	assertTrue(numberOfAvailableSpotBike > 0);
    }

    /**
     * This method tests the process of updating the availability for that parking
     * slot
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void updateParkingTest() {
	// GIVEN
	ParkingSpot parkingSpotOne = new ParkingSpot(1, ParkingType.CAR, false);
	ParkingSpot parkingSpotTwo = new ParkingSpot(2, ParkingType.BIKE, false);

	// WHEN
	boolean resultOne = parkingSpotDAO.updateParking(parkingSpotOne);
	boolean resultTwo = parkingSpotDAO.updateParking(parkingSpotTwo);

	// THEN
	assertTrue(resultOne);
	assertTrue(resultTwo);
	assertEquals(1, parkingSpotOne.getId());
	assertEquals(2, parkingSpotTwo.getId());
	assertEquals(ParkingType.CAR, parkingSpotOne.getParkingType());
	assertEquals(ParkingType.BIKE, parkingSpotTwo.getParkingType());
	assertEquals(false, parkingSpotOne.isAvailable());
	assertEquals(false, parkingSpotTwo.isAvailable());
    }
}
