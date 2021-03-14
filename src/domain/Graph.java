package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {

  private Map<String, Country> cca3ToCountry = new HashMap<String, Country>();
  private Set<Country> countries = new HashSet<Country>();
  private Map<String, List<Border>> borders = new HashMap<String, List<Border>>(); //cca3 -> borders

  public Graph() {
    super ();
  }

  /**
   * Adds a Country into the graph.
   * @param c : Country object
   * @return true if the Country has been added without issue (false otherwise)
   */
  public boolean addCountry(Country c) {
    cca3ToCountry.put(c.getCca3(), c);
    return countries.add(c);
  }

  /**
   * Adds a Border into the graph. (adds any unknown Country given as parameter too)
   * @param b : Border object
   * @return true if the Border has been added without issue (false otherwise)
   */
  public boolean addBorder(Border b) {
    String c1 = b.getCountry1();
    String c2 = b.getCountry2();
    if (!borders.containsKey(c1)) {
      borders.put(c1, new ArrayList<Border>());
    }
    if (!borders.containsKey(c2)) {
      borders.put(c2, new ArrayList<Border>());
    }
    if (!borders.get(c1).contains(b) && !borders.get(c2).contains(b)) {
      borders.get(c1).add(b);
      borders.get(c2).add(b);
      return true;
    }
    return false;
  }

  public void calculerItineraireMinimisantNombreDeFrontieres(String from, String to, String fileName) {
  }

  public void calculerItineraireMinimisantPopulationTotale(String from, String to, String fileName) {
    Map<Country, Integer> countryToInt = new HashMap<Country,Integer>();
    Map<Integer, Country> intToCountry = new HashMap<Integer,Country>();
    int count = 0;
    for (Country country: countries) {
      countryToInt.put(country, count);
      intToCountry.put(count++, country);
    }
    int[] finalTab = new int[countries.size()];
    List<Country> basePath = new ArrayList<Country>();
    List<Country> currentPath = new ArrayList<Country>();
    Country currentCountry = cca3ToCountry.get(from);

    while (true) {
      List<Border> adjacentCountries = borders.get(currentCountry.getCca3());
      int posCurrentCountry = countryToInt.get(currentCountry);
      int smallestNewPopulation = 0;
      Country nextCountry = null;
      currentPath.add(currentCountry);

      for (Border border: adjacentCountries) {
        //choisir le bon pays du border
        //TODO trouver comment supprimer ce if
        Country adjacentCountry;
        if (border.getCountry1().equals(currentCountry.getCca3()))
          adjacentCountry = cca3ToCountry.get(border.getCountry2());
        else
          adjacentCountry = cca3ToCountry.get(border.getCountry1());
        int posAdjacentCountry = countryToInt.get(adjacentCountry);
        int newPopulation = finalTab[posCurrentCountry] + adjacentCountry.getPopulation();
        //verifier si le nbr de population de currentCountry + le nbr de population du bon pays < que le nbr de population déjà présent pour le bon pays
        if (newPopulation > finalTab[posAdjacentCountry]) {
          finalTab[posAdjacentCountry] = newPopulation;
          if (smallestNewPopulation == 0 || smallestNewPopulation > newPopulation) {
            smallestNewPopulation = newPopulation;
            nextCountry = adjacentCountry;
            if (adjacentCountry.getCca3().equals(to))
              basePath = currentPath;
          }
        }
      }
      //si rien a été modifié ou si on a trouvé le plus petit poids pour le 'to' on arrete
      if (nextCountry == null || nextCountry.equals(to))
        break;
      currentCountry = nextCountry;
    }
    System.out.println(finalTab.toString());
  }
}
