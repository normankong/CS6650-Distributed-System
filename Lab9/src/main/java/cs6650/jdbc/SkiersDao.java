package cs6650.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import cs6650.model.SkierVertical;
import cs6650.model.SkierVerticalResorts;
import org.apache.commons.dbcp2.BasicDataSource;

public class SkiersDao {
  private static final boolean isFastMode = Boolean.valueOf(System.getProperty("FAST_MODE"));
  private static BasicDataSource slowDataSource;

  public SkiersDao() {
    slowDataSource = DBCPDataSource.getDataSource();
  }

  public static void main(String[] args) {
    SkiersDao dao = new SkiersDao();
  }

  public int getHeightByResortIdAndSeasonIdAndDayIdAndSkierId(int resortId, String seasonId, String dayId, int skierId) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String insertQueryStatement = "select count(1) as COUNT from LiftRides where resortId = ? and seasonId = ? and dayId = ? and skierId = ?";
    try {

      conn = isFastMode ? FastDataSource.getConnection() : slowDataSource.getConnection();
      preparedStatement = conn.prepareStatement(insertQueryStatement);
      preparedStatement.setInt(1, resortId);
      preparedStatement.setString(2, seasonId);
      preparedStatement.setString(3, dayId);
      preparedStatement.setInt(4, skierId);

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


  public SkierVertical getHeightBySkierId(int skierId) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String queryStatement = "select resortId, seasonId, count(1) as COUNT from LiftRides where skierId = ? group by resortId, seasonId";

    SkierVertical skierVertical = new SkierVertical();
    try {

      conn = isFastMode ? FastDataSource.getConnection() : slowDataSource.getConnection();
      preparedStatement = conn.prepareStatement(queryStatement);
      preparedStatement.setInt(1, skierId);

      // Execute insert SQL statement
      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        String seasonId = rs.getString("seasonId");
        int height = rs.getInt("COUNT");

        SkierVerticalResorts item = new SkierVerticalResorts();
        item.totalVert(height).seasonID(seasonId);
        skierVertical.addResortsItem(item);
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
    return skierVertical;
  }
}