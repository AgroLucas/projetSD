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
      System.out.println(currentCountry.getCca3());
      for (Border border: adjacentCountries) {
        //choisir le bon pays du border
        //TODO trouver comment supprimer ce if
        Country adjacentCountry;
        if (border.getCountry1().equals(currentCountry.getCca3()))
          adjacentCountry = cca3ToCountry.get(border.getCountry2());
        else
          adjacentCountry = cca3ToCountry.get(border.getCountry1());
        //System.out.println(adjacentCountry.getCca3());
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
