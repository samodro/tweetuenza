package com.androidhive.twitterconnect;

//import info.androidhive.googlemapsv2.R;
//import android.annotation.TargetApi;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends Activity{
	ListView listView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.ui_map);
        
        Intent i = getIntent();
        
        TextView txtProduct = (TextView) findViewById(R.id.product_label);  
        String product = i.getStringExtra("penyakit");
        
        String [] marker = getMarker(product);
        
        if(marker == null)
        {
        	marker = new String[5];
        	marker[0] = "Pengambilan Data Gagal";
        }
        
        
        listView = (ListView) findViewById(R.id.listMarker);
        
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, android.R.id.text1, marker);
             
        listView.setAdapter(adapter); 
        
        
        // getting attached intent data
        
        // displaying selected product name
        txtProduct.setText(product);
        
	}
	
	 public String [] getMarker (String penyakit)
	    {
	    	String [] listMarker =  null;
	    	try
	    	{
		    	URL url = new URL("http://tweetuenza.bl.ee/webservice/markerPositif?penyakit="+penyakit);
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new InputSource(url.openStream()));
				doc.getDocumentElement().normalize();
				NodeList nodeList = doc.getElementsByTagName("item");		
				
				listMarker = new String[nodeList.getLength()];
				for(int i = 0 ; i < nodeList.getLength() ; i++)
				{
					Node node = nodeList.item(i);
					Element marker = (Element) node;
					NodeList markerList = marker.getElementsByTagName("MARKER_POS");
					Element nameElementNama = (Element) markerList.item(0);
					markerList = nameElementNama.getChildNodes();
					listMarker[i] = ((Node)markerList.item(0)).getNodeValue();										
				}    	
		    }catch(MalformedURLException e)
			{
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	return listMarker;
	    }
	    
}

//public class Map extends Activity {
//
//	// Google Map
//	private GoogleMap googleMap;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//
//		try {
//			// Loading map
//			initilizeMap();
//
//			// Changing map type
//			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//			// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//			// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//			// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//			// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
//
//			// Showing / hiding your current location
//			googleMap.setMyLocationEnabled(true);
//
//			// Enable / Disable zooming controls
//			googleMap.getUiSettings().setZoomControlsEnabled(false);
//
//			// Enable / Disable my location button
//			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//			// Enable / Disable Compass icon
//			googleMap.getUiSettings().setCompassEnabled(true);
//
//			// Enable / Disable Rotate gesture
//			googleMap.getUiSettings().setRotateGesturesEnabled(true);
//
//			// Enable / Disable zooming functionality
//			googleMap.getUiSettings().setZoomGesturesEnabled(true);
//
//			double latitude = -7.289166000000001;
//			double longitude = 112.73439800000005;
//
//			// lets place some 10 random markers
//			for (int i = 0; i < 10; i++) {
//				// random latitude and logitude
//				double[] randomLocation = createRandLocation(latitude,
//						longitude);
//
//				// Adding a marker
//				MarkerOptions marker = new MarkerOptions().position(
//						new LatLng(randomLocation[0], randomLocation[1]))
//						.title("Hello Maps " + i);
//
//				Log.e("Random", "> " + randomLocation[0] + ", "
//						+ randomLocation[1]);
//
//				// changing marker color
//				if (i == 0)
//					marker.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//				if (i == 1)
//					marker.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//				if (i == 2)
//					marker.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
//				if (i == 3)
//					marker.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//				if (i == 4)
//					marker.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//				if (i == 5)
//					marker.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
//				if (i == 6)
//					marker.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//				if (i == 7)
//					marker.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
//				if (i == 8)
//					marker.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
//				if (i == 9)
//					marker.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//
//				googleMap.addMarker(marker);
//
//				// Move the camera to last position with a zoom level
//				if (i == 9) {
//					CameraPosition cameraPosition = new CameraPosition.Builder()
//							.target(new LatLng(randomLocation[0],
//									randomLocation[1])).zoom(15).build();
//
//					googleMap.animateCamera(CameraUpdateFactory
//							.newCameraPosition(cameraPosition));
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		initilizeMap();
//	}
//
//	/**
//	 * function to load map If map is not created it will create it for you
//	 * */
//	private void initilizeMap() {
//		if (googleMap == null) {
//			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
//					R.id.map)).getMap();
//
//			// check if map is created successfully or not
//			if (googleMap == null) {
//				Toast.makeText(getApplicationContext(),
//						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
//						.show();
//			}
//		}
//	}
//
//	/*
//	 * creating random postion around a location for testing purpose only
//	 */
//	private double[] createRandLocation(double latitude, double longitude) {
//
//		return new double[] { latitude + ((Math.random() - 0.5) / 500),
//				longitude + ((Math.random() - 0.5) / 500),
//				150 + ((Math.random() - 0.5) * 10) };
//	}
//}