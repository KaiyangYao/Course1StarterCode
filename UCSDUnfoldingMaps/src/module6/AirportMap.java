package module6;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PGraphics;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;

	String airportsData = "/Users/AdamYao/IdeaProjects/UCSDJava1/Course1StarterCode/UCSDUnfoldingMaps/data/airports.dat";
	String routesData = "/Users/AdamYao/IdeaProjects/UCSDJava1/Course1StarterCode/UCSDUnfoldingMaps/data/routes.dat";

	private CommonMarker lastSelected;


	public void setup() {
		// setting up PAppler
		size(3200,2400, OPENGL);

		// setting up map and default events
		map = new UnfoldingMap(this, 50, 50, 3150, 2350);
		MapUtils.createDefaultEventDispatcher(this, map);

		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, airportsData);

		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();

		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
			m.setRadius(8);
			airportList.add(m);

			// put airport in hashmap with OpenFlights unique id for key
			if(feature.getProperties().containsValue("\"Turkey\"")) {
				airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
			}
		}


		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, routesData);
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {

			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));

			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}

			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());

			// System.out.println(sl.getProperties());

			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
			 routeList.add(sl);

		}



		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES

		// map.addMarkers(routeList);

		// map.addMarkers(airportList);

		for(Marker marker : routeList) {
			marker.setColor(Color.black.getRGB());
			marker.setStrokeWeight(1);
			map.addMarker(marker);
		}


		for(Marker marker : airportList) {
			if((((AirportMarker) marker).getCountry()).equals("\"Turkey\"")){
				map.addMarker(marker);
			}
		}




	}

	public void draw() {
		background(0);
		map.draw();
	}

	/** Event handler that gets called automatically when the
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;

		}
		selectMarkerIfHover(airportList);
		//loop();
	}

	// If there is a marker selected
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}

		for (Marker m : markers)
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}


}
