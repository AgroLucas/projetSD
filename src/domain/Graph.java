package domain;

import java.util.*;

public class Graph {

  private Map<String, Country> cca3ToCountry = new HashMap<String, Country>();
  private Set<Country> countries = new HashSet<Country>();
  private Map<String, List<String>> borders = new HashMap<String, List<String>>(); //cca3 -> borders

  public Graph() {
    super();
  }

  /**
   * Adds a Country into the graph.
   *
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

  /**
   * Uses the BFS. algorithm
   * @param from
   * @param to
   * @param fileName
   */
  public void calculerItineraireMinimisantNombreDeFrontieres(String from, String to, String fileName) {
    Set<String> visited = new HashSet<String>();      // already visited
    Map<String, String> parents = new HashMap<>();    // way back
    Deque<String> fifo = new ArrayDeque<String>();    // scanning order
    String currentCountry = from;                     // current position

    visited.add(currentCountry);
    while (currentCountry != null && !currentCountry.equals(to)) {
      List<String> adjCountryLst = borders.get(currentCountry); // lst of adjacent countries
      adjCountryLst.sort(String::compareTo);

      for (String c : adjCountryLst) {
        if (!visited.contains(c)) {
          visited.add(c);
          fifo.addLast(c);
          parents.put(c, currentCountry);
        }
      }
      if (visited.contains(to)) {
        currentCountry = to;
      } else {
        if (fifo.peekFirst() != null) {
          currentCountry = fifo.removeFirst();
        } else {
          currentCountry = null;// null if empty fifo
        }
      }
    }

    if (currentCountry == null) {
      System.out.println("Aucun chemin trouvé");
      return;
    }

    // load path into stack
    Deque<String> stack = new ArrayDeque<String>();
    while (currentCountry != null) {
      stack.push(currentCountry);
      currentCountry = parents.get(currentCountry);
    }

    // pop stack into list -> good order
    List<String> path = new ArrayList<>();
    while (stack.size() != 0) {
      path.add(stack.pop());
    }

    writeOutput(path, fileName);
  }

  public void calculerItineraireMinimisantPopulationTotale(String from, String to, String fileName) {
    Map<Country, Long> finalTab = new HashMap<Country, Long>();
    SortedMap<Country, Long> provTab = new TreeMap<Country, Long>(Comparator.comparing((country -> country.getPopulation())));

    Country currentCountry = cca3ToCountry.get(from);
    provTab.put(currentCountry, (long) currentCountry.getPopulation());

    while (!provTab.isEmpty() && !finalTab.containsKey(to)) {
      finalTab.put(currentCountry, provTab.remove(currentCountry));
      updateProv(currentCountry, provTab, finalTab);
      if (!provTab.isEmpty()) {
        currentCountry = provTab.firstKey();
      }
    }
    System.out.println(to + " : " + finalTab.get(cca3ToCountry.get(to)));
  }

  private void updateProv(Country currentCountry, SortedMap<Country, Long> provTab, Map<Country, Long> finalTab) {
    long baseWeight = finalTab.get(currentCountry);
    for (String cca3 : borders.get(currentCountry.getCca3())) {
      Country c = cca3ToCountry.get(cca3);
      long weight = baseWeight + c.getPopulation();
      if (!finalTab.containsKey(c)) {
        if (!provTab.containsKey(c) || weight < provTab.get(c))
          provTab.put(c, weight);
      }
    }
  }

  private void writeOutput(List<String> path, String fileName) {
    //TODO: write into output file
    for (String c : path) { //display path
      System.out.println(c + " ");
    }
  }
}
