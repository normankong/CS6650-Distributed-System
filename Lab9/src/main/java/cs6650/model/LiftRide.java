package cs6650.model;

import com.google.gson.annotations.SerializedName;

/**
 * LiftRide
 */
public class LiftRide {

  @SerializedName("time")
  private Integer time = null;

  @SerializedName("liftID")
  private Integer liftID = null;

  @SerializedName("waitTime")
  private Integer waitTime = null;

  @SerializedName("skierId")
  private Integer skierId = null;

  @SerializedName("resortId")
  private Integer resortId = null;

  @SerializedName("seasonId")
  private String seasonId = null;

  @SerializedName("dayId")
  private String dayId = null;



  public LiftRide time(Integer time) {
    this.time = time;
    return this;
  }


  public LiftRide liftID(Integer liftID) {
    this.liftID = liftID;
    return this;
  }

  public LiftRide skierId(Integer skierId) {
    this.skierId = skierId;
    return this;
  }

  public LiftRide waitTime(Integer waitTime) {
    this.waitTime = waitTime;
    return this;
  }


  public LiftRide resortId(Integer resortId) {
    this.resortId = resortId;
    return this;
  }

  public LiftRide seasonId(String seasonId) {
    this.seasonId = seasonId;
    return this;
  }
  public LiftRide dayId(String dayId) {
    this.dayId = dayId;
    return this;
  }

  public Integer getTime() {
    return time;
  }

  public Integer getLiftID() {
    return liftID;
  }

  public Integer getWaitTime() {
    return waitTime;
  }

  public Integer getSkierId() {
    return skierId;
  }

  public Integer getResortId() {
    return resortId;
  }

  public String getSeasonId() {
    return seasonId;
  }

  public String getDayId() {
    return dayId;
  }
}
