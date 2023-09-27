package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

/**
 * The ParkingServiceTest class is used to test the ParkingService class
 */
@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @Mock
    private static ParkingSpotDAO parkingSpotDAO;

    @Mock
    private static TicketDAO ticketDAO;

    Ticket ticket = new Ticket();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    @BeforeEach
    private void setUpPerTest() {
	try {
	    ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
	    ticket.setParkingSpot(parkingSpot);
	    ticket.setVehicleRegNumber("ABCDEF");
	    parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new RuntimeException("Failed to set up test mock objects");
	}
    }

    /**
     * This method tests the process of exit of a vehicle
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void processExitingVehicleTest() throws Exception {
	// GIVEN
	when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
	when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
	when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
	
	// WHEN
	parkingService.processExitingVehicle();

	// THEN
	verify(ticketDAO, times(1)).updateTicket(ticket);
	verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
    }

    /**
     * This method tests the process of entry of a vehicle
     * 
     * @param none
     * 
     * @return void
     */
    @Test
     public void testProcessIncomingVehicle() throws Exception {
	 // GIVEN
         when(inputReaderUtil.readSelection()).thenReturn(1);
         when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
         when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
         
         when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(false);
         when(ticketDAO.getNbTicket(anyString())).thenReturn(1);
         when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);

         // WHEN
         parkingService.processIncomingVehicle();

         // THEN
         verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
         verify(ticketDAO, times(1)).getNbTicket(anyString());
         verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
     }

    /**
     * This method tests the process of exit of a vehicle with an unable update of the database
     * 
     * @param none
     * 
     * @return void
     */
    @Test public void processExitingVehicleTestUnableUpdate() throws Exception {
	// GIVEN
	when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
	when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

	// WHEN
	parkingService.processExitingVehicle();

	// THEN
	verify(ticketDAO, times(1)).updateTicket(ticket); 
     }

    /**
     * This method tests the identification of an available parking
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void testGetNextParkingNumberIfAvailable() {
	// GIVEN
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(parkingSpot.getId());

	// WHEN
	ParkingSpot parkingSpoted = parkingService.getNextParkingNumberIfAvailable();

	// THEN
	verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any(ParkingType.class));
	assertEquals(parkingSpot.getId(), parkingSpoted.getId());
	assertTrue(parkingSpoted.isAvailable());
    }

    /**
     * This method tests the lack of an available parking
     * 
     * @param none
     * 
     * @return void
     */
    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() {
	// GIVEN
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);

	// WHEN
	ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();

	// THEN
	assertNull(parkingSpot);
    }

    /**
     * This method tests the identification of an available parking with a wrong argument
     * @param none
     * 
     * @return void
     */
    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument() {
	// GIVEN
	when(inputReaderUtil.readSelection()).thenReturn(3);

	// WHEN
	ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();

	// THEN
	assertNull(parkingSpot);
    }
}
