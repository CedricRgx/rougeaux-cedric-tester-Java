package com.parkit.parkingsystem.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    private static final Logger logger = LogManager.getLogger("FareCalculatorService");

    public void calculateFare(Ticket ticket) {

	TicketDAO ticketDAO = new TicketDAO();
	logger.info("Appel à getNbTicket() depuis calculateFare() avec le véhicule : " + ticket.getVehicleRegNumber());
	if (ticketDAO.getNbTicket(ticket.getVehicleRegNumber()) > 1) { // the vehicle has been already parked, then it
								       // has a 5% discount
	    calculateFare(ticket, true);
	} else {
	    calculateFare(ticket, false);
	}
    }

    public void calculateFare(Ticket ticket, boolean isDiscounted) {
	if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
	    throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
	}

	// Step 4: expand the 5% discount feature
	double factorDiscount = isDiscounted ? 0.95 : 1; // if there is a 5% discount, the user has to pay 95% of the
	// price

	double inHour = ticket.getInTime().getTime();
	double outHour = ticket.getOutTime().getTime();

	// Step 2: retrieval of input and output times in milliseconds
	double duration = (outHour - inHour) / (1000 * 60 * 60); // To convert the duration from milliseconds to hours
	// Step 3: implement the 30-minutes free feature
	if (duration < 0.5) {
	    ticket.setPrice(0);
	} else {
	    switch (ticket.getParkingSpot().getParkingType()) {
	    case CAR: {
		ticket.setPrice(factorDiscount * duration * Fare.CAR_RATE_PER_HOUR);
		break;
	    }
	    case BIKE: {
		ticket.setPrice(factorDiscount * duration * Fare.BIKE_RATE_PER_HOUR);
		break;
	    }
	    default:
		throw new IllegalArgumentException("Unkown Parking Type");
	    }
	}
    }

}