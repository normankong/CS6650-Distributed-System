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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SkierVertical
 */

public class SkierVertical {
  @SerializedName("resorts")
  private List<SkierVerticalResorts> resorts = null;

  public SkierVertical resorts(List<SkierVerticalResorts> resorts) {
    this.resorts = resorts;
    return this;
  }

  public SkierVertical addResortsItem(SkierVerticalResorts resortsItem) {
    if (this.resorts == null) {
      this.resorts = new ArrayList<SkierVerticalResorts>();
    }
    this.resorts.add(resortsItem);
    return this;
  }

  /**
   * Get resorts
   *
   * @return resorts
   **/

  public List<SkierVerticalResorts> getResorts() {
    return resorts;
  }

  public void setResorts(List<SkierVerticalResorts> resorts) {
    this.resorts = resorts;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkierVertical skierVertical = (SkierVertical) o;
    return Objects.equals(this.resorts, skierVertical.resorts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resorts);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkierVertical {\n");

    sb.append("    resorts: ").append(toIndentedString(resorts)).append("\n");
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