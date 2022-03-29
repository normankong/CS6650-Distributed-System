package cs6650.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

public class ResortsDao {
  private static final boolean isFastMode = Boolean.valueOf(System.getProperty("FAST_MODE"));
  private static BasicDataSource slowDataSource;

  public ResortsDao() {
    slowDataSource = DBCPDataSource.getDataSource();
  }

  public static void main(String[] args) {
    ResortsDao dao = new ResortsDao();
  }

  public int getCountByResortIdAndSeasonIdAndDayId(int resortId, String seasonId, String dayId) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String insertQueryStatement = "select count(1) as COUNT from LiftRides where resortId = ? and seasonId = ? and dayId = ?";
    try {

      conn = isFastMode ? FastDataSource.getConnection() : slowDataSource.getConnection();
      preparedStatement = conn.prepareStatement(insertQueryStatement);
      preparedStatement.setInt(1, resortId);
      preparedStatement.setString(2, seasonId);
      preparedStatement.setString(3, dayId);

      // Execute insert SQL statement
      ResultSet rs = preparedStatement.executeQuery();

      if (rs.next()) {
         int count = rs.getInt("COUNT");
        return count;
      }

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
    return 0;
  }
}