package uk.co.brotherlogic.busit.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import uk.co.brotherlogic.busit.BusStop;

import com.google.code.gwt.geolocation.client.Geolocation;
import com.google.code.gwt.geolocation.client.Position;
import com.google.code.gwt.geolocation.client.PositionCallback;
import com.google.code.gwt.geolocation.client.PositionError;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BusIt implements EntryPoint
{
   Map<Position, BusStop> stops = new TreeMap<Position, BusStop>();

   /**
    * This is the entry point method.
    */
   @Override
   public void onModuleLoad()
   {
	   
      // Do everything on the location callbac
      if (Geolocation.isSupported())
      {
         Label mainLabel = new Label("Hello");
         RootPanel.get("maintext").add(mainLabel);
         
         Geolocation geo = Geolocation.getGeolocation();
         geo.getCurrentPosition(new PositionCallback() {
			
			@Override
			public void onSuccess(Position position) {
				Label newLabel = new Label(position.getCoords().getLatitude() + "," + position.getCoords().getLongitude());
				RootPanel.get("maintext").add(newLabel);
			}
			
			@Override
			public void onFailure(PositionError error) {
				Label newLabel = new Label("poo");
				
				RootPanel.get("maintext").add(newLabel);
			}
         });
      }
      else
      {
         Label mainLabel = new Label("Goodbye");
         RootPanel.get("maintext").add(mainLabel);
      }
   }

   private void runLocation(double lon, double lat)
   {
      BusStop stop = getBusStop(lon, lat);
   }

   private BusStop getBusStop(double lon, double lat)
   {
      // Load the bus stops if necessary

      double bestDist = Double.MAX_VALUE;
      BusStop retStop = null;

      for (Entry<Position, BusStop> stop : stops.entrySet())
      {
         double dist = computeDist(stop.getKey(), lon, lat);
         if (dist < bestDist)
         {
            bestDist = dist;
            retStop = stop.getValue();
         }
      }

      return retStop;
   }

   private double computeDist(Position point, double lon, double lat)
   {
      return Math.sqrt((lon - point.getCoords().getLongitude())
            * (lon - point.getCoords().getLongitude()) - (lat - point.getCoords().getLatitude())
            * (lat - point.getCoords().getLatitude()));
   }
}
