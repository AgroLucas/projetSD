package domain;

import java.util.*;

public class Graph {

  private Map<String, Country> cca3ToCountry = new HashMap<String, Country>();
  private Set<Country> countries = new HashSet<Country>();
  private Map<String, List<String>> borders = new HashMap<String, List<String>>(); //cca3 -> borders

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

  //TODO do 2 if
  public boolean addBorder(String c1, String c2) {
    if (!borders.containsKey(c1)) {
      borders.put(c1, new ArrayList<String>());
    }
    if (!borders.get(c1).contains(c2)) {
      borders.get(c1).add(c2);
      return true;
    }
    return false;
  }

  public void calculerItineraireMinimisantNombreDeFrontieres(String from, String to, String fileName) {
    Set<String> visited = new HashSet<String>();      // already visited
    Map<String, String> parents = new HashMap<>();    // way back
    Deque<String> fifo = new ArrayDeque<String>();    // scanning order
    String currentCountry = from; // current position

    while(currentCountry!=null && !currentCountry.equals(to)) {
      visited.add(currentCountry);
      List<String> adjCountryLst = borders.get(currentCountry); // lst of adjacent countries

      for (String c : adjCountryLst) {
        if(!visited.contains(c)){
          fifo.addLast(c);
          parents.put(c, currentCountry);
        }
      }
      currentCountry = fifo.peekFirst(); // null if empty
    }

    if(currentCountry == null) {
      System.out.println("Aucun chemin trouvé");
      return;
    }

    Deque<String> stack = new ArrayDeque<String>();
    while(currentCountry != null){
      stack.push(currentCountry);
      currentCountry = parents.get(currentCountry);
    }

    List<String> path = new ArrayList<>();
    while(stack.size() != 0) {
      path.add(stack.pop());
    }

    for(String c : path) {
      System.out.println(c + " ");
    }
  }

  public void calculerItineraireMinimisantPopulationTotale(String from, String to, String fileName) {
    Map<Country, Integer> finalTab = new HashMap<Country, Integer>();
    Map<Country, Integer> provTab = new HashMap<Country, Integer>();
    //remplir tabs
    for (Country c: countries) {
      finalTab.put(c, null);
      provTab.put(c, null);
    }
    Country currentCountry = cca3ToCountry.get(from);
    finalTab.put(currentCountry, currentCountry.getPopulation());
    provTab.put(currentCountry, currentCountry.getPopulation());
    //rename
    Set<Country> visitedCountries = new HashSet<Country>();

    while (true) {
      System.out.println(currentCountry.getCca3());
      List<String> adjacentCountries = borders.get(currentCountry.getCca3());
      int currentTotalPop = finalTab.get(currentCountry);
      //to check which is the next country
      for (String c: adjacentCountries) {
        Country adjacentCountry = cca3ToCountry.get(c);
        //do not do anything if already added in final tab
        if (finalTab.get(adjacentCountry) != null)
          break;
        Integer adjacentCountryPop = provTab.get(adjacentCountry);
        if (adjacentCountryPop == null)
          adjacentCountryPop = 0;
        //change the adjacent country total pop
        if (adjacentCountryPop == 0 || adjacentCountryPop > currentTotalPop + currentCountry.getPopulation()) {
          provTab.put(adjacentCountry, currentTotalPop + currentCountry.getPopulation());
          visitedCountries.add(adjacentCountry);
        }
      }
      int smallestNewPopulation = 0;
      Country nextCountry = null;
      for (Country country : visitedCountries) {
        int adjacentCountryPop = provTab.get(country);
        if (smallestNewPopulation > adjacentCountryPop) {
          smallestNewPopulation = currentTotalPop + adjacentCountryPop;
          nextCountry = country;
        }
      }
      visitedCountries.remove(nextCountry);
      finalTab.put(nextCountry, smallestNewPopulation);
      //si rien a été modifié ou si on a trouvé le plus petit poids pour le 'to' on arrete
      if (nextCountry == null || nextCountry.equals(to))
        break;
      currentCountry = nextCountry;
    }
  }
}
