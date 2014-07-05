package com.androidhive.twitterconnect;

//import info.androidhive.googlemapsv2.R;
import android.annotation.TargetApi;
import java.io.IOException;
//import org.apache.tika.language.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.StringTokenizer;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Map extends Activity{
	ListView listView;
	Tweet [] tweets, tweetpos;
	String [] marker;
	String product;
	int banyak, jmltweetpos;
	// Google Map
	private GoogleMap googleMap;
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.ui_map);
        
        //panggil marker, tweet, simpan
        Tweet();
        
        //panggil map
        BuatMap();
        
                
        //panggil map dibaris ini, variabel tweetpos adalah global, tidak perlu diparsing                
        
	}
	
	
	public void Tweet()
	{
		Intent i = getIntent();
        
        //TextView txtProduct = (TextView) findViewById(R.id.product_label);  
        product = i.getStringExtra("penyakit");
        
        marker = getMarker(product);
        
        if(marker == null)
        {
        	marker = new String[5];
        	marker[0] = "Pengambilan Data Gagal";
        }
                
        //listView = (ListView) findViewById(R.id.listMarker);        
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //android.R.layout.simple_list_item_1, android.R.id.text1, marker);        
        //listView.setAdapter(adapter);                        
        //txtProduct.setText(product);
        
        Load(marker.length);
        insertTweetOld();
        tweetpos = allTweetPos(product);
        cetak();
	}
	
	public void Load(int lengt)
	{
		 	Crawling crawling = new Crawling();
	        tweets =  new Tweet[lengt * 200];
	        
	        int j = 0;
	  	    for(int iter = 0; iter<lengt ; iter++)
		  	{
		  		  Tweet [] temp = null;
		  		  try {
						temp = crawling.crawl(marker[iter]);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		  		  		  
		  		  
		  		  for(int x = 0; x < crawling.banyaktweet ; x++)
		  		  {
		  			  tweets[j] = new Tweet();
		  			  tweets[j++] = temp[x];
		  		  }
		  		  
		  	}
	        
	  	    banyak = j;
	  	    
	  	    System.out.println("banyak j:" + j);
	  	    
	  	    for(int tes = 0; tes<banyak ; tes++)
	  	    {
	  	    	System.out.println("TWEET ke - " + tes);
	  	    	System.out.println(tweets[tes].tweet);
	  	    	System.out.println(tweets[tes].date);
	  	    	System.out.println(tweets[tes].latitude+' '+tweets[tes].longitude);
	  	    }

	}
	
	public void insertTweetOld()
	{
		System.out.println("TES");
		for(int i = 0; i<banyak ; i++)
		{
			tweets[i].tweet = tweets[i].tweet.replaceAll("[^A-Za-z0-9\\d\\-_\\s]", "");
			tweets[i].tweet = tweets[i].tweet.replaceAll(" ","+");
			
			System.out.println(tweets[i].tweet);
			
			try
	    	{				
		    	URL url = new URL("http://tweetuenza.bl.ee/webservice/markertweet?tweet="+tweets[i].tweet+"&latitude="+tweets[i].latitude+"&longitude="+tweets[i].longitude+"&penyakit="+product+"&time="+tweets[i].date.replace(" ", "+"));				
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new InputSource(url.openStream()));
				doc.getDocumentElement().normalize();
				NodeList nodeList = doc.getElementsByTagName("xml");									
						
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
			
		}
	}
	
	//ambil latitude longitude dari variabel tweetpos.latitude atau tweetpos.langitude
	public void cetak()
	{
		System.out.println("CEK latitude longitude");
		for(int i = 0; i<jmltweetpos;i++)
		{
			System.out.println(tweetpos[i].tweet);
			
			//titik yang akan ditaruh google map
			System.out.println(tweetpos[i].latitude+" - "+tweetpos[i].longitude);
		}
		
	}
	
	public Tweet [] allTweetPos(String penyakit)
	{
		Tweet [] listTweet =  null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
    	try
    	{
	    	URL url = new URL("http://tweetuenza.bl.ee/webservice/allTweetPositif?penyakit="+penyakit+"&time="+ dateFormat.format(date).replace(" ", "+"));
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("item");		
			
			jmltweetpos = nodeList.getLength();
			listTweet = new Tweet[nodeList.getLength()];
			for(int i = 0 ; i < nodeList.getLength() ; i++)
			{
				listTweet[i] = new Tweet();
				
				Node node = nodeList.item(i);
				Element tweet = (Element) node;
				
				NodeList tweetList = tweet.getElementsByTagName("ISI_TWEET");
				Element element = (Element) tweetList.item(0);
				tweetList = element.getChildNodes();
				listTweet[i].tweet = ((Node)tweetList.item(0)).getNodeValue();	
				
				tweetList = tweet.getElementsByTagName("LATITUDE");
				element = (Element) tweetList.item(0);
				tweetList = element.getChildNodes();
				listTweet[i].latitude = ((Node)tweetList.item(0)).getNodeValue();
				
				tweetList = tweet.getElementsByTagName("LONGITUDE");
				element = (Element) tweetList.item(0);
				tweetList = element.getChildNodes();
				listTweet[i].longitude = ((Node)tweetList.item(0)).getNodeValue();
				
				tweetList = tweet.getElementsByTagName("TIME_OF_TWEET");
				element = (Element) tweetList.item(0);
				tweetList = element.getChildNodes();
				listTweet[i].date = ((Node)tweetList.item(0)).getNodeValue();
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
    	
    	return listTweet;
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
	    	
	public void BuatMap()
	{

		try {
			// Loading map
			initilizeMap();

			// Changing map type
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

			// Showing / hiding your current location
			googleMap.setMyLocationEnabled(true);

			// Enable / Disable zooming controls
			googleMap.getUiSettings().setZoomControlsEnabled(false);

			// Enable / Disable my location button
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);

			// Enable / Disable Compass icon
			googleMap.getUiSettings().setCompassEnabled(true);

			// Enable / Disable Rotate gesture
			googleMap.getUiSettings().setRotateGesturesEnabled(true);

			// Enable / Disable zooming functionality
			googleMap.getUiSettings().setZoomGesturesEnabled(true);


			// lets place some 10 random markers
			for (int i = 0; i < jmltweetpos; i++) {
				
				// Adding a marker
				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(Double.parseDouble(tweetpos[i].latitude), Double.parseDouble(tweetpos[i].longitude)))
						.title("Hello Maps " + i);

				Log.e("Random", "> " + tweetpos[i].latitude + ", "
						+ tweetpos[i].longitude);

				// changing marker color
				if (i%2 == 0)
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
				if (i%2 == 1)
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
								
				
				googleMap.addMarker(marker);

				// Move the camera to last position with a zoom level
				if (i == jmltweetpos-1) {
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(new LatLng(Double.parseDouble(tweetpos[i].latitude),
									Double.parseDouble(tweetpos[i].longitude))).zoom(15).build();

					googleMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}


	/*
	/**
	 * function to load map If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
	
}
}