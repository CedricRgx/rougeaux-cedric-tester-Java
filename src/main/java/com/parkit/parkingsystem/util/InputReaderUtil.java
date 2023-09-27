package com.parkit.parkingsystem.util;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The InputReaderUtil class allows to get the information from the user
 */
public class InputReaderUtil {

    private static Scanner scan = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");

    /**
     * This method allows to get to information from the user
     * 
     * @param none
     * 
     * @return the number of the type of vehicle
     */
    public int readSelection() {
	try {
	    int input = Integer.parseInt(scan.nextLine());
	    return input;
	} catch (Exception e) {
	    logger.error("Error while reading user input from Shell", e);
	    System.out.println("Error reading input. Please enter valid number for proceeding further");
	    return -1;
	}
    }

    /**
     * This method allows to get to vehicle registration number of the vehicle from
     * the user
     * 
     * @param none
     * 
     * @return the vehicle registration number of the vehicle
     */
    public String readVehicleRegistrationNumber() throws Exception {
	try {
	    String vehicleRegNumber = scan.nextLine();
	    if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
		throw new IllegalArgumentException("Invalid input provided");
	    }
	    return vehicleRegNumber;
	} catch (Exception e) {
	    logger.error("Error while reading user input from Shell", e);
	    System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
	    throw e;
	}
    }
}