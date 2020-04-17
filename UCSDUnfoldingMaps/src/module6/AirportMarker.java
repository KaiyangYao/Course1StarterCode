package module6;

import java.awt.*;
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMarker extends CommonMarker {
	public static List<SimpleLinesMarker> routes;
	
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		pg.fill(Color.RED.getRGB());
		pg.stroke = false;
		pg.ellipse(x, y, 8, 8);
		
		
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		 // show rectangle with title
		
		// show routes

		String title = getTitle();
		pg.pushStyle();

		pg.rectMode(PConstants.CORNER);

		pg.stroke(110);
		pg.fill(255,255,255);
		pg.rect(x, y + 15, pg.textWidth(title) +6, 18, 5);


		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.fill(0);
		pg.text(title, x + 3 , y +18);


		pg.popStyle();
	}


	public String getTitle(){
		return getName() + " " + getCode();
	}

	public String getName(){
		String pureName = (String) getProperty("name");
		return pureName.substring(1,pureName.length()-1);
	}

	public String getCity(){
		return (String) getProperty("city");
	}

	public String getCountry(){
		return (String) getProperty("country");
	}

	public String getCode(){
		return getProperty("code").toString();
	}






	
}
