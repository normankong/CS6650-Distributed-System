package cs6650.jdbc;

import java.sql.*;
import cs6650.model.LiftRide;
import org.apache.commons.dbcp2.*;

public class LiftRideDao {
  private static BasicDataSource dataSource;

  public LiftRideDao() {
    dataSource = DBCPDataSource.getDataSource();
  }

  public static void main(String[] args) {
    LiftRideDao liftRideDao = new LiftRideDao();
    LiftRide liftRide = (new LiftRide()).time(100).liftID(200).waitTime(300).skierId(400).resortId(500).seasonId("SUMMER").dayId("10");
    liftRideDao.createLiftRide(liftRide);
  }

  public void createLiftRide(LiftRide newLiftRide) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String insertQueryStatement = "INSERT INTO LiftRides (skierId, resortId, seasonId, dayId, time, liftId, waitTime) " +
        "VALUES (?,?,?,?,?,?,?)";
    try {
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(insertQueryStatement);
      preparedStatement.setInt(1, newLiftRide.getSkierId());
      preparedStatement.setInt(2, newLiftRide.getResortId());
      preparedStatement.setString(3, newLiftRide.getSeasonId());
      preparedStatement.setString(4, newLiftRide.getDayId());
      preparedStatement.setInt(5, newLiftRide.getTime());
      preparedStatement.setInt(6, newLiftRide.getLiftID());
      preparedStatement.setInt(7, newLiftRide.getWaitTime());

      // execute insert SQL statement
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }
}