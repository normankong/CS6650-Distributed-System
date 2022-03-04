/*
 * Ski Data API for NEU Seattle distributed systems course
 * An API for an emulation of skier managment system for RFID tagged lift tickets. Basis for CS6650 Assignments for 2019
 *
 * OpenAPI spec version: 1.1
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package cs6650.model;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

/**
 * APIStatsEndpointStats
 */

public class APIStatsEndpointStats {
  @SerializedName("URL")
  private String URL = null;

  @SerializedName("operation")
  private String operation = null;

  @SerializedName("mean")
  private Integer mean = null;

  @SerializedName("max")
  private Integer max = null;

  public APIStatsEndpointStats URL(String URL) {
    this.URL = URL;
    return this;
  }

  /**
   * Get URL
   *
   * @return URL
   **/

  public String getURL() {
    return URL;
  }

  public void setURL(String URL) {
    this.URL = URL;
  }

  public APIStatsEndpointStats operation(String operation) {
    this.operation = operation;
    return this;
  }

  /**
   * Get operation
   *
   * @return operation
   **/
  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public APIStatsEndpointStats mean(Integer mean) {
    this.mean = mean;
    return this;
  }

  /**
   * Get mean
   *
   * @return mean
   **/
  public Integer getMean() {
    return mean;
  }

  public void setMean(Integer mean) {
    this.mean = mean;
  }

  public APIStatsEndpointStats max(Integer max) {
    this.max = max;
    return this;
  }

  /**
   * Get max
   *
   * @return max
   **/
  public Integer getMax() {
    return max;
  }

  public void setMax(Integer max) {
    this.max = max;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    APIStatsEndpointStats apIStatsEndpointStats = (APIStatsEndpointStats) o;
    return Objects.equals(this.URL, apIStatsEndpointStats.URL) &&
        Objects.equals(this.operation, apIStatsEndpointStats.operation) &&
        Objects.equals(this.mean, apIStatsEndpointStats.mean) &&
        Objects.equals(this.max, apIStatsEndpointStats.max);
  }

  @Override
  public int hashCode() {
    return Objects.hash(URL, operation, mean, max);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class APIStatsEndpointStats {\n");

    sb.append("    URL: ").append(toIndentedString(URL)).append("\n");
    sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
    sb.append("    mean: ").append(toIndentedString(mean)).append("\n");
    sb.append("    max: ").append(toIndentedString(max)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
