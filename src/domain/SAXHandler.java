package domain;

import java.util.Arrays;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {

  private Graph graph;
  private Country currentCountry;
  private boolean bBorder;


  public Graph getGraph() {
    return this.graph;
  }

  @Override
  public void startDocument() {
    this.graph = new Graph();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equalsIgnoreCase("country")) {
      String cca3 = attributes.getValue("cca3");
      String capital = attributes.getValue("capital");
      String currency = attributes.getValue("currency");
      String languages = attributes.getValue("languages");
      String latlng = attributes.getValue("latlng");
      String name = attributes.getValue("name");
      int population = Integer.parseInt(attributes.getValue("population"));
      String region = attributes.getValue("region");
      String subregion = attributes.getValue("subregion");
      Country country;
      if (languages == null || currency == null || capital == null || subregion == null) // Antarctica ATA doesn't have all attributes
        country = new Country(cca3, latlng, name, population, region);
      else
        country = new Country(cca3, capital, currency, Arrays.asList(languages.split(",").clone()), latlng, name, population, region, subregion);
      if (!this.graph.addCountry(country))
        throw new SAXException();
      this.currentCountry = country;
    } else if (qName.equalsIgnoreCase("border"))
      this.bBorder = true;
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    if (this.bBorder) {
      if (!this.graph.addBorder(new Border(this.currentCountry.getCca3(), new String(ch, start, length))))
        throw new SAXException();
      this.bBorder = false;
    }
  }
}
