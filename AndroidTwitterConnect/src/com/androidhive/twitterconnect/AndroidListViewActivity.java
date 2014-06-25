package com.androidhive.twitterconnect;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import org.w3c.dom.Element;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AndroidListViewActivity extends ListActivity {
	
    static final String KEY_ITEM = "item"; // parent node
    static final String KEY_ID = "ID_PENYAKIT";
    static final String KEY_NAME = "NAMA_PENYAKIT";
    
    //String url="http://10.0.2.2/flu/index.php/webservice/penyakit";
    String url="http://tweetuenza.bl.ee/webservice/penyakit";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        

        ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
        XMLParser parser = new XMLParser();
        String xml = parser.getXmlFromUrl(url); // getting XML
        Document doc = parser.getDomElement(xml); // getting DOM element
        
        NodeList nl = doc.getElementsByTagName(KEY_ITEM);
        Log.d("My Message", xml);
        String []jnsPenyakit=new String[nl.getLength()];
        // looping through all item nodes <item>
        for (int i = 0; i < nl.getLength(); i++) {
            // creating new HashMap
//            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);
            // adding each child node to HashMap key => value
//            map.put(KEY_ID, parser.getValue(e, KEY_ID));
//            map.put(KEY_NAME, parser.getValue(e, KEY_NAME));
 
            // adding HashList to ArrayList
//            menuItems.add(map);
            
            jnsPenyakit[i]=parser.getValue(e,KEY_NAME);
        }
        
        // storing string resources into Array
        String[] jenis_penyakit = getResources().getStringArray(R.array.jenis_penyakit);
        
        // Binding Array to ListAdapter
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.ui_penyakit, R.id.listPenyakit, jnsPenyakit));
        
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
}