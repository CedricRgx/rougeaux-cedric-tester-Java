package com.parkit.parkingsystem.integration.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

/**
 * The TicketDAOTest class is used to test the TicketDAO class
 */
@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

    private static final Logger logger = LogManager.getLogger("TicketDAOTest");

    private static TicketDAO ticketDAO;

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    private static void setUp() throws Exception {
	ticketDAO = new TicketDAO();
	ticketDAO.setDataBaseConfig(dataBaseTestConfig);
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
     * This method tests the process of saving a ticket in the database
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void saveTicketTest() {
	// GIVEN
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	Ticket ticket = new Ticket();
	ticket.setId(1);
	ticket.setParkingSpot(parkingSpot);
	ticket.setVehicleRegNumber("ABCDEF");
	ticket.setPrice(0);
	ticket.setInTime(new Date(System.currentTimeMillis() - (20 * 60 * 60 * 1000)));
	ticket.setOutTime(null);

	// WHEN
	boolean result = ticketDAO.saveTicket(ticket);
	Ticket savedTicket = ticketDAO.getTicket("ABCDEF");

	// THEN
	assertFalse(result);
	assertEquals(ticket.getParkingSpot(), savedTicket.getParkingSpot());
	assertEquals(ticket.getVehicleRegNumber(), savedTicket.getVehicleRegNumber());
	assertEquals(ticket.getPrice(), savedTicket.getPrice());
	assertNull(savedTicket.getOutTime());
	assertNotNull(savedTicket.getInTime());
    }

    /**
     * This method tests the process of getting a ticket from the database
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void getTicketTest() {
	// GIVEN
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	String vehicleRegNumberTest = "FFFFFF";
	Ticket ticket = new Ticket();
	ticket.setId(1);
	ticket.setParkingSpot(parkingSpot);
	ticket.setVehicleRegNumber(vehicleRegNumberTest);
	ticket.setPrice(0);
	ticket.setInTime(new Date(System.currentTimeMillis() - (20 * 60 * 60 * 1000)));
	ticket.setOutTime(null);
	ticketDAO.saveTicket(ticket);

	// WHEN
	Ticket ticketRecoveredTicket = ticketDAO.getTicket(vehicleRegNumberTest);

	// THEN
	assertNotNull(ticketRecoveredTicket);
    }

    /**
     * This method tests the process of updating a ticket in the database
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void updateTicketTest() {
	// GIVEN
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	Ticket ticket = new Ticket();
	ticket.setId(1);
	ticket.setParkingSpot(parkingSpot);
	ticket.setVehicleRegNumber("TOTOTO");
	ticket.setPrice(0);
	ticket.setInTime(new Date(System.currentTimeMillis() - (20 * 60 * 60 * 1000)));
	ticket.setOutTime(new Date(System.currentTimeMillis() - (10 * 60 * 60 * 1000)));
	ticketDAO.saveTicket(ticket);

	// WHEN
	ticket.setPrice(50.2);
	boolean result = ticketDAO.updateTicket(ticket);

	// THEN
	assertTrue(result);
    }

    /**
     * This method tests the process of getting the number of occurrence of a ticket
     * in the database
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void getNbTicketTest() {
	// GIVEN
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	Ticket ticketOne = new Ticket();
	ticketOne.setId(1);
	ticketOne.setParkingSpot(parkingSpot);
	ticketOne.setVehicleRegNumber("AAAAAA");
	ticketOne.setPrice(0);
	ticketOne.setInTime(new Date(System.currentTimeMillis() - (20 * 60 * 60 * 1000)));
	ticketOne.setOutTime(new Date(System.currentTimeMillis() - (10 * 60 * 60 * 1000)));
	ticketDAO.saveTicket(ticketOne);

	Ticket ticketTwo = new Ticket();
	ticketTwo.setId(1);
	ticketTwo.setParkingSpot(parkingSpot);
	ticketTwo.setVehicleRegNumber("AAAAAA");
	ticketTwo.setPrice(0);
	ticketTwo.setInTime(new Date(System.currentTimeMillis() - (9 * 60 * 60 * 1000)));
	ticketTwo.setOutTime(new Date(System.currentTimeMillis() - (7 * 60 * 60 * 1000)));
	ticketDAO.saveTicket(ticketTwo);

	Ticket ticketThree = new Ticket();
	ticketThree.setId(1);
	ticketThree.setParkingSpot(parkingSpot);
	ticketThree.setVehicleRegNumber("AAAAAA");
	ticketThree.setPrice(0);
	ticketThree.setInTime(new Date(System.currentTimeMillis() - (5 * 60 * 60 * 1000)));
	ticketThree.setOutTime(null);
	ticketDAO.saveTicket(ticketThree);

	// WHEN
	int numberOfTicket = ticketDAO.getNbTicket("AAAAAA");

	// THEN
	assertEquals(3, numberOfTicket);
    }
}
