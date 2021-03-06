package com.cse110team14.placeit.server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.cse110team14.placeit.LoginActivity;
import com.cse110team14.placeit.controller.MapOnClickController;
import com.cse110team14.placeit.model.CPlaceIts;
import com.cse110team14.placeit.model.PlaceIt;

/**
 * Class UpdatePlaceItsOnServer
 * Description: this class is used for posting regular placeit and categorical placeit information
 * onto the server
 */
public class UpdatePlaceItsOnServer {
	public static final String CPLACEITS_URL = "http://placeitteam14.appspot.com/cplaceits";
	public static void postPlaceIts(PlaceIt p) {
		final PlaceIt tmp = p;
		Thread t = new Thread() {

			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(MapOnClickController.PLACEITS_URL);

			    try {
			      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			      nameValuePairs.add(new BasicNameValuePair("title",
			    		  tmp.getTitle()));
			      nameValuePairs.add(new BasicNameValuePair("description",
			    		  tmp.getDescription()));
			      nameValuePairs.add(new BasicNameValuePair("postDate",
			    		  tmp.getDate()));
			      nameValuePairs.add(new BasicNameValuePair("dateToBeReminded",
			    		  tmp.getDateReminded()));
			      nameValuePairs.add(new BasicNameValuePair("color",
			    		  tmp.getColor()));
			      //This is always a regular placeit
			      nameValuePairs.add(new BasicNameValuePair("type",
			    		  "1"));
			      nameValuePairs.add(new BasicNameValuePair("location",
			    		  tmp.getLocation().toString()));
			      nameValuePairs.add(new BasicNameValuePair("placeitType",
			    		  Integer.toString(tmp.getPlaceItType())));
			      nameValuePairs.add(new BasicNameValuePair("sneezeType",
			    		  Integer.toString(tmp.getSneezeType())));
			      nameValuePairs.add(new BasicNameValuePair("user",
			    		  LoginActivity.loginActivity.username));
			      nameValuePairs.add(new BasicNameValuePair("listType",
			    		  tmp.getListType()));
			      nameValuePairs.add(new BasicNameValuePair("action",
				          "put"));
			      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			 
			      HttpResponse response = client.execute(post);
			      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			      String line = "";
			      while ((line = rd.readLine()) != null) {
			        Log.d("hello", line);
			      }

			    } catch (IOException e) {
			    	Log.d("hello", "IOException while trying to conect to GAE");
			    }
			}
		};
		t.start();
	}
	
	public static void postCPlaceIts(CPlaceIts p) {
		final CPlaceIts tmp = p;
		Thread t = new Thread() {

			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(CPLACEITS_URL);

			    try {
			      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			      nameValuePairs.add(new BasicNameValuePair("title",
			    		  tmp.getTitle()));
			      nameValuePairs.add(new BasicNameValuePair("description",
			    		  tmp.getDescription()));
			      nameValuePairs.add(new BasicNameValuePair("postDate",
			    		  tmp.getDate()));
			      nameValuePairs.add(new BasicNameValuePair("dateToBeReminded",
			    		  tmp.getDateReminded()));
			      //This is always a category placeit
			      nameValuePairs.add(new BasicNameValuePair("type",
			    		  "2"));
			      String categories = "";
			      String [] tmpArray = tmp.getCategories();
			      for(String i : tmpArray){
			    	  categories = categories + i + "###";
			      }
			      nameValuePairs.add(new BasicNameValuePair("categories", 
			    		  categories));
			      nameValuePairs.add(new BasicNameValuePair("user",
			    		  LoginActivity.loginActivity.username));
			      nameValuePairs.add(new BasicNameValuePair("listType",
			    		  tmp.getListType()));
			      nameValuePairs.add(new BasicNameValuePair("action",
				          "put"));
			      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			 
			      HttpResponse response = client.execute(post);
			      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			      String line = "";
			      while ((line = rd.readLine()) != null) {
			        Log.d("hello", line);
			      }

			    } catch (IOException e) {
			    	Log.d("hello", "IOException while trying to conect to GAE");
			    }
			}
		};
		t.start();
	}
}
