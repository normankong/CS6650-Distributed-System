package cs6650.model;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class ResortIDSeasonsBody {
  @SerializedName("year")
  private String year = null;

  public ResortIDSeasonsBody year(String year) {
    this.year = year;
    return this;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResortIDSeasonsBody resortIDSeasonsBody = (ResortIDSeasonsBody) o;
    return Objects.equals(this.year, resortIDSeasonsBody.year);
  }

  @Override
  public int hashCode() {
    return Objects.hash(year);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResortIDSeasonsBody {\n");

    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
