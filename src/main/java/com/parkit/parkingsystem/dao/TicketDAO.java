package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

/**
 * The TicketDAO class is used to interact between the Ticket objects and the
 * database
 */
public class TicketDAO {

    private static final Logger logger = LogManager.getLogger("TicketDAO");

    private DataBaseConfig dataBaseConfig = new DataBaseConfig();

    public DataBaseConfig getDataBaseConfig() {
	return dataBaseConfig;
    }

    public void setDataBaseConfig(DataBaseConfig dataBaseConfig) {
	this.dataBaseConfig = dataBaseConfig;
    }

    /**
     * This method save a ticket in the database
     * 
     * @param Ticket object
     * 
     * @return a boolean value on the successful execution of the save action in the
     *         database
     */
    public boolean saveTicket(Ticket ticket) {
	Connection con = null;
	try {
	    con = dataBaseConfig.getConnection();
	    PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
	    // ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
	    // ps.setInt(1,ticket.getId());
	    ps.setInt(1, ticket.getParkingSpot().getId());
	    ps.setString(2, ticket.getVehicleRegNumber());
	    ps.setDouble(3, ticket.getPrice());
	    ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
	    ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
	    return ps.execute();
	} catch (Exception ex) {
	    logger.error("Error fetching next available slot", ex);
	} finally {
	    dataBaseConfig.closeConnection(con);
	    return false;
	}
    }

    /**
     * This method retrieves a ticket in the database
     * 
     * @param the vehicle reg number to retrieve
     * 
     * @return Ticket of the retrieved vehicle
     */
    public Ticket getTicket(String vehicleRegNumber) {
	Connection con = null;
	Ticket ticket = null;
	try {
	    con = dataBaseConfig.getConnection();
	    PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
	    // ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
	    ps.setString(1, vehicleRegNumber);
	    ResultSet rs = ps.executeQuery();
	    if (rs.next()) {
		ticket = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
		ticket.setParkingSpot(parkingSpot);
		ticket.setId(rs.getInt(2));
		ticket.setVehicleRegNumber(vehicleRegNumber);
		ticket.setPrice(rs.getDouble(3));
		ticket.setInTime(rs.getTimestamp(4));
		ticket.setOutTime(rs.getTimestamp(5));
	    }
	    dataBaseConfig.closeResultSet(rs);
	    dataBaseConfig.closePreparedStatement(ps);
	} catch (Exception ex) {
	    logger.error("Error fetching next available slot", ex);
	} finally {
	    dataBaseConfig.closeConnection(con);
	    return ticket;
	}
    }

    /**
     * This method updates a ticket in the database
     * 
     * @param Ticket of the vehicle
     * 
     * @return a boolean value on the successful execution of the update action in
     *         the database
     */
    public boolean updateTicket(Ticket ticket) {
	Connection con = null;
	try {
	    con = dataBaseConfig.getConnection();
	    PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
	    ps.setDouble(1, ticket.getPrice());
	    ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
	    ps.setInt(3, ticket.getId());
	    ps.execute();
	    return true;
	} catch (Exception ex) {
	    logger.error("Error saving ticket info", ex);
	} finally {
	    dataBaseConfig.closeConnection(con);
	}
	return false;
    }

    /**
     * This method get the number of ticket for the same vehicle
     * 
     * @param the vehicle reg number to retrieve
     * 
     * @return the number of the occurrence of the vehicle reg number in the
     *         database
     */
    public int getNbTicket(String regVehicle) {
	Connection con = null;
	int nbTicket = 0;
	try {
	    con = dataBaseConfig.getConnection();
	    PreparedStatement ps = con.prepareStatement(DBConstants.COUNT_TICKET);
	    ps.setString(1, regVehicle);
	    ResultSet rs = ps.executeQuery();
	    if (rs.next()) {
		nbTicket = rs.getInt(1);
	    }
	    ps.execute();
	} catch (Exception ex) {
	    logger.error("Error retrieving ticket info", ex);
	} finally {
	    dataBaseConfig.closeConnection(con);
	}
	return nbTicket;
    }
}
