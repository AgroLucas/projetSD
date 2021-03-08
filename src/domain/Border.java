package domain;

import java.util.Objects;

public class Border {

  private String country1; //cca3
  private String country2; //cca3

  public Border(String country1, String country2) {
    this.country1 = country1;
    this.country2 = country2;
  }

  public String getCountry1() {
    return country1;
  }

  public String getCountry2() {
    return country2;
  }

  @Override
  public String toString() {
    return "Border{country1 = " + country1 + ", country2 = " + country2 + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Border border = (Border) o;
    return country1.equals(border.country1) && country2.equals(border.country2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(country1, country2);
  }
}
