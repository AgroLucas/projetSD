package main;

import domain.DOM;
import domain.Graph;
import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import domain.SAXHandler;

public class Main {

  public static void main(String[] args) {
    try {
      //SAX
			File inputFile = new File("resources/countries.xml");
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			SAXHandler userhandler = new SAXHandler();
			saxParser.parse(inputFile, userhandler);
			Graph g = userhandler.getGraph();

      //DOM
      //Graph g = DOM.getDom("resources/countries.xml");

      g.calculerItineraireMinimisantNombreDeFrontieres("BEL", "IND", "output.xml");
      g.calculerItineraireMinimisantPopulationTotale("BEL", "IND", "output2.xml");
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
  }
}
