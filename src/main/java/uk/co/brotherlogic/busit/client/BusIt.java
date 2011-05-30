package uk.co.brotherlogic.busit.client;

import com.google.code.gwt.geolocation.client.Coordinates;
import com.google.code.gwt.geolocation.client.Geolocation;
import com.google.code.gwt.geolocation.client.Position;
import com.google.code.gwt.geolocation.client.PositionCallback;
import com.google.code.gwt.geolocation.client.PositionError;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BusIt implements EntryPoint
{
   String base_url = "http://edip:8085/busittomcat";

   /**
    * This is the entry point method.
    */
   @Override
   public void onModuleLoad()
   {
	   
      // Do everything on the location callbac
      if (Geolocation.isSupported())
      {
         Geolocation loc = Geolocation.getGeolocation();
         System.err.println("Getting location");
         loc.getCurrentPosition(new PositionCallback()
         {
            @Override
            public void onSuccess(Position position)
            {
               System.err.println("Got location");
               Coordinates coords = position.getCoords();
               getBusStop(coords.getLatitude(), coords.getLongitude());
            }

            @Override
            public void onFailure(PositionError error)
            {
               Label mainLabel = new Label("Cannot access location: " + error.getMessage());
               RootPanel.get("maintext").add(mainLabel);
            }
         });
      }
      else
      {
         Label mainLabel = new Label("Your browser does not support locations");
         RootPanel.get("maintext").add(mainLabel);
      }

   }

   private void runLocation(double lon, double lat)
   {
      getBusStop(lon, lat);
   }

   private void displayBusStop(String stop)
   {
      try
      {
         final String url = base_url + "/stop/" + stop;
         RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
         System.out.println(base_url + "/stop/" + stop);
         builder.sendRequest("", new RequestCallback()
         {
            @Override
            public void onResponseReceived(Request request, Response response)
            {
               Label lab = new Label(url);
               RootPanel.get("maintext").add(lab);
            }

            @Override
            public void onError(Request request, Throwable exception)
            {
               System.err.println("Error!");
            }
         });
      }
      catch (RequestException e)
      {
         e.printStackTrace();
      }
   }

   private void getBusStop(double lon, double lat)
   {
      System.err.println("Getting Bus Stops");
      try
      {
         String url = base_url + "/stops/" + lon + "/" + lat;
         RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
         System.err.println("URL = " + url);
         builder.sendRequest(null, new RequestCallback()
         {
            @Override
            public void onResponseReceived(Request request, Response response)
            {
               System.err.println("Got: " + response.getText());
               System.err.println("Also: " + response.getStatusText());
               System.err.println("Thinking: " + request.toString());
               System.err.println("And: " + response.getHeadersAsString());
               System.err.println("Indeed: " + response.getStatusCode());
               String stopID = response.getText();

               Label lab2 = new Label("" + response.getStatusCode() + " => " + stopID);
               RootPanel.get("maintext").add(lab2);

               displayBusStop(stopID);
            }

            @Override
            public void onError(Request request, Throwable exception)
            {
               System.err.println("Error!");
            }
         });
      }
      catch (RequestException e)
      {
         e.printStackTrace();
      }
   }
}
