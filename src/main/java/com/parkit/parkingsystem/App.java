package com.parkit.parkingsystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.service.InteractiveShell;

/**
 * The Main class contains the main method that orders the program
 */

public class App {

    private static final Logger logger = LogManager.getLogger("App");

    public static void main(String args[]) {
	logger.info("Initializing Parking System");
	InteractiveShell.loadInterface();
    }
}
