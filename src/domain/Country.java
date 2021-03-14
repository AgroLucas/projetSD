package domain;

import java.util.List;
import java.util.Objects;

public class Country {
  private String cca3;
  private String capital;
  private String currency;
  private List<String> languages;
  private String latlng;
  private String name;
  private int population;
  private String region;
  private String subregion;


  public Country(String cca3, String latlng, String name, int population, String region) {
    this.cca3 = cca3;
    this.latlng = latlng;
    this.name = name;
    this.population = population;
    this.region = region;
  }

  public Country(String cca3, String capital, String currency,
      List<String> languages, String latlng, String name, int population, String region,
      String subregion) {
    this.cca3 = cca3;
    this.capital = capital;
    this.currency = currency;
    this.languages = languages;
    this.latlng = latlng;
    this.name = name;
    this.population = population;
    this.region = region;
    this.subregion = subregion;
  }

  public String getCca3() {
    return cca3;
  }

  public String getCapital() {
    return capital;
  }

  public String getCurrency() {
    return currency;
  }

  public List<String> getLanguages() {
    return languages;
  }

  public String getLatlng() {
    return latlng;
  }

  public String getName() {
    return name;
  }

  public int getPopulation() {
    return population;
  }

  public String getRegion() {
    return region;
  }

  public String getSubregion() {
    return subregion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Country country = (Country) o;
    return Objects.equals(cca3, country.cca3);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cca3);
  }
}
