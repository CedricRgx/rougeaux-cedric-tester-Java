package com.parkit.parkingsystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The DataBaseConfig class is used to interact with the production database
 */

public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");

    /**
     * This method creates the production database connection
     * 
     * @param none
     * 
     * @return Connection object
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
	logger.info("Create DB connection");
	Class.forName("com.mysql.cj.jdbc.Driver");
	return DriverManager.getConnection(
		"jdbc:mysql://localhost:3306/prod?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
		"root", "rootroot");
    }

    /**
     * This method closes the database connection
     * 
     * @param Connection object
     * 
     * @return void
     */
    public void closeConnection(Connection con) {
	if (con != null) {
	    try {
		con.close();
		logger.info("Closing DB connection");
	    } catch (SQLException e) {
		logger.error("Error while closing connection", e);
	    }
	}
    }

    /**
     * This method closes the object for to interact with database
     * 
     * @param PreparedStatement object
     * 
     * @return void
     */
    public void closePreparedStatement(PreparedStatement ps) {
	if (ps != null) {
	    try {
		ps.close();
		logger.info("Closing Prepared Statement");
	    } catch (SQLException e) {
		logger.error("Error while closing prepared statement", e);
	    }
	}
    }

    /**
     * This method closes the result of the interaction with database
     * 
     * @param ResultSet object
     * 
     * @return void
     */
    public void closeResultSet(ResultSet rs) {
	if (rs != null) {
	    try {
		rs.close();
		logger.info("Closing Result Set");
	    } catch (SQLException e) {
		logger.error("Error while closing result set", e);
	    }
	}
    }
}
