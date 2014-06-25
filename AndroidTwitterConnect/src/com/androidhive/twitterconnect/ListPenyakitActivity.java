package com.androidhive.twitterconnect;

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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListPenyakitActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // storing string resources into Array
        String[] jenis_penyakit = getJenisPenyakit();
        
        if(jenis_penyakit == null)
        {
        	jenis_penyakit = new String[5];
        	jenis_penyakit[0] = "Pengambilan Data Gagal";
        }
        // Binding Array to ListAdapter
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.ui_penyakit, R.id.listPenyakit, jenis_penyakit));
        
        ListView lv = getListView();

        // listening to single list item on click
        lv.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
        	  
        	  // selected item 
        	  String product = ((TextView) view).getText().toString();
        	  
        	  // Launching new Activity on selecting single List Item
        	  Intent i = new Intent(getApplicationContext(), Map.class); // <---- isi class Map.java
        	  // sending data to new activity
        	  i.putExtra("product", product);
        	  startActivity(i);
        	
          }
        });
    }
    
    public String [] getJenisPenyakit()
    {
    	String [] listPenyakit =  null;
    	try
    	{
	    	URL url = new URL("http://tweetuenza.bl.ee/webservice/penyakit");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("item");		
			
			listPenyakit = new String[nodeList.getLength()];
			for(int i = 0 ; i < nodeList.getLength() ; i++)
			{
				Node node = nodeList.item(i);
				Element namaPenyakit = (Element) node;
				NodeList namaPenyakitList = namaPenyakit.getElementsByTagName("NAMA_PENYAKIT");
				Element nameElementNama = (Element) namaPenyakitList.item(0);
				namaPenyakitList = nameElementNama.getChildNodes();
				listPenyakit[i] = ((Node)namaPenyakitList.item(0)).getNodeValue();
								
				//System.out.println("Nma kost " + listKost[i].toString());
				
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

    	    
    	
    	return listPenyakit;
    }
}