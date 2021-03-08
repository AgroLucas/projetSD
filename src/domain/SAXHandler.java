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
      List<String> languages = Arrays.asList(attributes.getValue("languages").split(",").clone());
      Country country = new Country(attributes.getValue("cca3"), attributes.getValue("capital"), attributes.getValue("currency"),
                                    languages, attributes.getValue("latlng"), attributes.getValue("name"), Integer.parseInt(attributes.getValue("population")),
                                    attributes.getValue("region"), attributes.getValue("subregion"));
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
