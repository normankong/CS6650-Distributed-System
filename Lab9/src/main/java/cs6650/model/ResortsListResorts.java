package cs6650.model;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

/**
 * ResortsListResorts
 */

public class ResortsListResorts {
  @SerializedName("resortName")
  private String resortName = null;

  @SerializedName("resortID")
  private Integer resortID = null;

  public ResortsListResorts resortName(String resortName) {
    this.resortName = resortName;
    return this;
  }

  /**
   * Get resortName
   *
   * @return resortName
   **/
  public String getResortName() {
    return resortName;
  }

  public void setResortName(String resortName) {
    this.resortName = resortName;
  }

  public ResortsListResorts resortID(Integer resortID) {
    this.resortID = resortID;
    return this;
  }

  /**
   * Get resortID
   *
   * @return resortID
   **/
  public Integer getResortID() {
    return resortID;
  }

  public void setResortID(Integer resortID) {
    this.resortID = resortID;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResortsListResorts resortsListResorts = (ResortsListResorts) o;
    return Objects.equals(this.resortName, resortsListResorts.resortName) &&
        Objects.equals(this.resortID, resortsListResorts.resortID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resortName, resortID);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResortsListResorts {\n");

    sb.append("    resortName: ").append(toIndentedString(resortName)).append("\n");
    sb.append("    resortID: ").append(toIndentedString(resortID)).append("\n");
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
