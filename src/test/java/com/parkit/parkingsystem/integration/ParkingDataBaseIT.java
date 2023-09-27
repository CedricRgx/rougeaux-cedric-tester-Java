package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

/**
 * The ParkingDataBaseIT class is used to do integration test of the program
 */
@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static final Logger logger = LogManager.getLogger("ParkingDatabaseIT");

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    private static ParkingSpotDAO parkingSpotDAO;

    private static TicketDAO ticketDAO;

    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception {
	parkingSpotDAO = new ParkingSpotDAO();
	parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
	ticketDAO = new TicketDAO();
	ticketDAO.dataBaseConfig = dataBaseTestConfig;
	dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
	lenient().when(inputReaderUtil.readSelection()).thenReturn(1);
	when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {

    }

    /**
     * This method tests the process of park a vehicle
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void testParkingACar() throws Exception {
	// GIVEN
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

	// WHEN
	parkingService.processIncomingVehicle();

	// THEN
	assertTrue(ticketDAO.getNbTicket("ABCDEF") == 1); // check that a ticket is actualy saved in DB
	assertNotEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)); // check that Parking table is updated
	// with availability
    }

    /**
     * This method tests the process of exit a vehicle
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void testParkingLotExit() throws Exception {
	// GIVEN
	testParkingACar();
	when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("GHIJKL");

	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	Ticket initialTicket = new Ticket();
	initialTicket.setId(1);
	initialTicket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
	initialTicket.setVehicleRegNumber("GHIJKL");
	initialTicket.setPrice(0);
	initialTicket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
	initialTicket.setOutTime(null);
	ticketDAO.saveTicket(initialTicket);
	Ticket ticketSaved = ticketDAO.getTicket("GHIJKL");
	ticketSaved.setOutTime(new Date(System.currentTimeMillis()));
	ticketDAO.updateTicket(ticketSaved);

	// WHEN
	parkingService.processExitingVehicle();

	// THEN
	assertNotNull(ticketDAO.getTicket("GHIJKL").getOutTime()); // check that the out time is populated in the
								   // database
	assertNotEquals(0, ticketSaved.getPrice()); // check that the fare is generated
    }

    /**
     * This method tests the process of exit a vehicle with an discount
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void testParkingLotExitRecurringUser() throws Exception {
	// GIVEN
	// testParkingACar();
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	// ticket of the first entry of the vehicle
	Ticket firstTicket = new Ticket();
	firstTicket.setId(1);
	firstTicket.setParkingSpot(parkingSpot);
	firstTicket.setVehicleRegNumber("ABCDEF");
	firstTicket.setPrice(0);
	firstTicket.setInTime(new Date(System.currentTimeMillis() - (20 * 60 * 60 * 1000)));
	firstTicket.setOutTime(new Date(System.currentTimeMillis() - (10 * 60 * 60 * 1000)));
	ticketDAO.saveTicket(firstTicket);
	parkingSpotDAO.updateParking(firstTicket.getParkingSpot());
	parkingSpot.setAvailable(true);
	parkingSpotDAO.updateParking(parkingSpot);

	// ticket of the second entry of the vehicle
	Ticket secondTicket = new Ticket();
	secondTicket.setId(2);
	secondTicket.setParkingSpot(parkingSpot);
	secondTicket.setVehicleRegNumber("ABCDEF");
	secondTicket.setPrice(0);
	secondTicket.setInTime(new Date(System.currentTimeMillis() - (1 * 60 * 60 * 1000)));
	secondTicket.setOutTime(null);
	ticketDAO.saveTicket(secondTicket);
	parkingSpotDAO.updateParking(secondTicket.getParkingSpot());

	// WHEN
	parkingService.processExitingVehicle();

	// THEN
	// assertNotNull(ticketDAO.getTicket("ABCDEF").getOutTime()); // check that the
	// out time is populated in the
	// database
	// assertNotEquals(0, initialTicket.getPrice()); // check that the fare is
	// generated

	// THEN

    }
}
