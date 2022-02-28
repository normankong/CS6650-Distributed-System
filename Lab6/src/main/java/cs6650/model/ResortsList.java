package cs6650.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ResortsList
 */

public class ResortsList {
  @SerializedName("resorts")
  private List<ResortsListResorts> resorts = null;

  public ResortsList resorts(List<ResortsListResorts> resorts) {
    this.resorts = resorts;
    return this;
  }

  public ResortsList addResortsItem(ResortsListResorts resortsItem) {
    if (this.resorts == null) {
      this.resorts = new ArrayList<ResortsListResorts>();
    }
    this.resorts.add(resortsItem);
    return this;
  }

  /**
   * Get resorts
   *
   * @return resorts
   **/
  public List<ResortsListResorts> getResorts() {
    return resorts;
  }

  public void setResorts(List<ResortsListResorts> resorts) {
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
    ResortsList resortsList = (ResortsList) o;
    return Objects.equals(this.resorts, resortsList.resorts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resorts);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResortsList {\n");

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
