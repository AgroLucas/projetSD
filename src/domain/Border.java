package domain;

public class Border {

  private Country country1;
  private Country country2;

  public Border(Country country1, Country country2) {
    this.country1 = country1;
    this.country2 = country2;
  }

  public Country getCountry1() {
    return country1;
  }

  public Country getCountry2() {
    return country2;
  }

  @Override
  public String toString() {
    return "Border{country1 = " + country1 + ", country2 = " + country2 + "}";
  }
}
