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
  private Map<Country, List<Border>> borders = new HashMap<Country, List<Border>>();

  public Graph() {
    super ();
  }

  /**
   * Adds a Country into the graph.
   * @param c : Country object
   * @return true if the Country has been added without issue (false otherwise)
   */
  public boolean addCountry(Country c) {
    return countries.add(c);
  }

  /**
   * Adds a Border into the graph. (adds any unknown Country given as parameter too)
   * @param b : Border object
   * @return true if the Border has been added without issue (false otherwise)
   */
  public boolean addBorder(Border b) {
    Country c1 = b.getCountry1();
    Country c2 = b.getCountry2();
    if(!countries.contains(c1)) {
      addCountry(c1);
    }
    if(!countries.contains(c2)) {
      addCountry(c2);
    }
    if(!borders.containsKey(c1)) {
      borders.put(c1, new ArrayList<Border>());
    }
    if(!borders.containsKey(c2)) {
      borders.put(c2, new ArrayList<Border>());
    }
    if(!borders.get(c1).contains(b) || !borders.get(c2).contains(b)){
      borders.get(c1).add(b);
      borders.get(c2).add(b);
      return true;
    }
    return false;
  }

  public void calculerItineraireMinimisantNombreDeFrontieres(String from, String to, String fileName) {
  }

  public void calculerItineraireMinimisantPopulationTotale(String from, String to, String fileName) {
  }
}
