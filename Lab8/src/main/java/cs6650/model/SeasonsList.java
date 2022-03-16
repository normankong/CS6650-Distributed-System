package cs6650.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * SeasonsList
 */

public class SeasonsList {
  @SerializedName("seasons")
  private List<String> seasons = null;

  public SeasonsList seasons(List<String> seasons) {
    this.seasons = seasons;
    return this;
  }

  public SeasonsList addSeasonsItem(String seasonsItem) {
    if (this.seasons == null) {
      this.seasons = new ArrayList<String>();
    }
    this.seasons.add(seasonsItem);
    return this;
  }

  public List<String> getSeasons() {
    return seasons;
  }

  public void setSeasons(List<String> seasons) {
    this.seasons = seasons;
  }

}
