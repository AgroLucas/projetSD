package domain;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.*;
import org.w3c.dom.*;


public class DOM {

  public static Graph getDom(String fileName) {
    Graph graph = new Graph();

    try {
      File xmlFile = new File(fileName);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(xmlFile);

      setCountries(graph, doc);

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return graph;
  }

  private static void setCountries(Graph graph, Document doc) {
    NodeList countries = doc.getElementsByTagName("country");

    for (int i = 0; i < countries.getLength(); i++) {
      Node nCountry = countries.item(i);
      Element eCountry = (Element) nCountry;

      String cca3 = eCountry.getAttribute("cca3");
      String latlng = eCountry.getAttribute("latlng");
      String name = eCountry.getAttribute("name");
      int population = Integer.parseInt(eCountry.getAttribute("population"));
      String region = eCountry.getAttribute("region");

      String capital = eCountry.getAttribute("capital");

      Country c;
      if (capital == null) {
        c = new Country(cca3, latlng, name, population, region);
      } else {
        String currency = eCountry.getAttribute("currency");
        String languagesList = eCountry.getAttribute("languages");
        List<String> languages = Arrays.asList(languagesList.split(","));
        String subregion = eCountry.getAttribute("subregion");
        c = new Country(cca3, capital, currency, languages, latlng, name, population,
            region, subregion);
      }
      graph.addCountry(c);

      setBorders(graph, eCountry, c);
    }
  }

  private static void setBorders(Graph graph, Element eCountry, Country c) {
    NodeList borders = eCountry.getElementsByTagName("border");
    String countryCca3 = c.getCca3();
    for (int j = 0; j < borders.getLength(); j++) {
      graph.addBorder(countryCca3, borders.item(j).getTextContent());
    }
  }

}
