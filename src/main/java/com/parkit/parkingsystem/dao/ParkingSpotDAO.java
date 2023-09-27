package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

/**
 * The ParkingSpotDAO class is used to interact between the ParkingSpot objects
 * and the database
 */
public class ParkingSpotDAO {
    private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * This method finds the next available spot in the parking for the vehicle
     * 
     * @param ParkingType object
     * 
     * @return the ID of the available spot
     */
    public int getNextAvailableSlot(ParkingType parkingType) {
	Connection con = null;
	int result = -1;
	try {
	    con = dataBaseConfig.getConnection();
	    PreparedStatement ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
	    ps.setString(1, parkingType.toString());
	    ResultSet rs = ps.executeQuery();
	    if (rs.next()) {
		result = rs.getInt(1);
		;
	    }
	    dataBaseConfig.closeResultSet(rs);
	    dataBaseConfig.closePreparedStatement(ps);
	} catch (Exception ex) {
	    logger.error("Error fetching next available slot", ex);
	} finally {
	    dataBaseConfig.closeConnection(con);
	}
	return result;
    }

    /**
     * This method updates the spot in the parking of the vehicle
     * 
     * @param ParkingSpot object
     * 
     * @return a boolean value on the successful execution of the database update
     */
    public boolean updateParking(ParkingSpot parkingSpot) {
	// update the availability for that parking slot
	Connection con = null;
	try {
	    con = dataBaseConfig.getConnection();
	    PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
	    ps.setBoolean(1, parkingSpot.isAvailable());
	    ps.setInt(2, parkingSpot.getId());
	    int updateRowCount = ps.executeUpdate();
	    dataBaseConfig.closePreparedStatement(ps);
	    return (updateRowCount == 1);
	} catch (Exception ex) {
	    logger.error("Error updating parking info", ex);
	    return false;
	} finally {
	    dataBaseConfig.closeConnection(con);
	}
    }

}
