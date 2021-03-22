package domain;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.SortedMap;
import java.util.Set;
import java.util.List;
import java.util.Deque;
import java.util.Comparator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class Graph {

    public static final String PATH_RESOURCE = "resources/";

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
     * Uses the BFS algorithm.
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
        long totPop = 0;
        while (currentCountry != null) {
            totPop += cca3ToCountry.get(currentCountry).getPopulation();
            stack.push(currentCountry);
            currentCountry = parents.get(currentCountry);
        }

        // pop stack into list -> good order
        List<String> path = new ArrayList<>();
        while (stack.size() != 0) {
            path.add(stack.pop());
        }

        writeOutput(path, fileName, totPop);
    }

    /**
     * Use Dijkstra.
     */
    public void calculerItineraireMinimisantPopulationTotale(String from, String to, String fileName) {
        Map<Country, Long> finalTab = new HashMap<Country, Long>();
        Map<String, String> previousCountries = new HashMap<String, String>();
        SortedMap<Country, Long> provTab = new TreeMap<Country, Long>(Comparator.comparing((country -> country.getPopulation())));

        Country currentCountry = cca3ToCountry.get(from);

        provTab.put(currentCountry, (long) currentCountry.getPopulation());
        previousCountries.put(currentCountry.getCca3(), null);

        while (!provTab.isEmpty() && !finalTab.containsKey(cca3ToCountry.get(to))) {
            finalTab.put(currentCountry, provTab.remove(currentCountry));
            updateProv(currentCountry, provTab, previousCountries, finalTab);
            if (!provTab.isEmpty()) {
                currentCountry = provTab.firstKey();
            }
        }

        if (finalTab.get(cca3ToCountry.get(to)) == null) {
          System.out.println("Aucun chemin trouvé");
          return;
        }
        String cca3 = to;

        // load path into stack
        Deque<String> stack = new ArrayDeque<String>();
        while (cca3 != null) {
            stack.push(cca3);
            cca3 = previousCountries.get(cca3);
        }

        // pop stack into list -> good order
        List<String> path = new ArrayList<>();
        while (stack.size() != 0) {
            path.add(stack.pop());
        }

        writeOutput(path, fileName, finalTab.get(cca3ToCountry.get(to)));
    }

    /**
     * Updates provTab & previousCountries with the borders of the current country.
     */
    private void updateProv(Country currentCountry, SortedMap<Country, Long> provTab, Map<String, String> previousCountries, Map<Country, Long> finalTab) {
        long baseWeight = finalTab.get(currentCountry);
        for (String cca3 : borders.get(currentCountry.getCca3())) {
            Country c = cca3ToCountry.get(cca3);
            long weight = baseWeight + c.getPopulation();
            if (!finalTab.containsKey(c)) {
                if (!provTab.containsKey(c) || weight < provTab.get(c)) {
                    previousCountries.put(c.getCca3(), currentCountry.getCca3());
                    provTab.put(c, weight);
                }
            }
        }
    }


    private void writeOutput(List<String> path, String fileName, long totPop) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("itineraire");
            rootElement.setAttribute("arrive", path.get(path.size() - 1));
            rootElement.setAttribute("depart", path.get(0));
            rootElement.setAttribute("nbPays", "" + path.size());
            rootElement.setAttribute("sommePopulation", "" + totPop);
            document.appendChild(rootElement);

            for (String countryCca3 : path) {
                writeCountry(document, rootElement, countryCca3);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");

            DOMImplementation domImpl = document.getImplementation();
            DocumentType docType = domImpl.createDocumentType("itineraire",
                null, "itineraire.dtd");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docType.getSystemId());

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(PATH_RESOURCE + fileName));
            transformer.transform(source, result);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void writeCountry(Document document, Element root, String countryCca3) throws XMLStreamException {
        Element c = document.createElement("pays");

        Country countryObject = cca3ToCountry.get(countryCca3);
        c.setAttribute("cca3", countryCca3);
        c.setAttribute("nom", countryObject.getName());
        c.setAttribute("population", "" + countryObject.getPopulation());

        root.appendChild(c);
    }


}
