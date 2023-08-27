package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
	if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
	    throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
	}

	double inHour = ticket.getInTime().getTime();
	double outHour = ticket.getOutTime().getTime();

	// Step 2: retrieval of input and output times in milliseconds
	double duration = (outHour - inHour) / (1000 * 60 * 60); // To convert the duration from milliseconds to hours

	// Step 3: implement the 30-minute free feature
	if (duration < 0.5) {
	    ticket.setPrice(0);
	} else {
	    switch (ticket.getParkingSpot().getParkingType()) {
	    case CAR: {
		ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
		break;
	    }
	    case BIKE: {
		ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
		break;
	    }
	    default:
		throw new IllegalArgumentException("Unkown Parking Type");
	    }
	}
    }
}