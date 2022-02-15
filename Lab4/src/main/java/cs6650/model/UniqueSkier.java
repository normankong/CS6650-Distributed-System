package cs6650.model;

import com.google.gson.annotations.SerializedName;

/**
 * UniqueSkier
 */

public class UniqueSkier {
  @SerializedName("time")
  private String time = null;

  @SerializedName("numSkiers")
  private Integer numSkiers = null;

  public UniqueSkier(String time, Integer numSkiers) {
    this.time = time;
    this.numSkiers = numSkiers;
  }

}
