package com.khabali.fft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import LoadImage.LazyAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.khabali.json.JSONParser;
import com.khabali.map.MymapActivity;

public class GridFriends extends Activity {
	
	public String FRIENDS_URL = "https://graph.facebook.com/me/friends";
	public String INFO_URL = "http://graph.facebook.com/";
	
	public static final String TAG_DATA = "data";
	public static final String TAG_ID ="id";
	public static final String TAG_NAME ="name";
	public static final String TAG_BIRHT = "birthday";
	public static final String TAG_RELATION ="relationship_status";
	public static final String TAG_LOCATION ="location";
	public static final String TAG_HOME = "hometown";
	public static final String TAG_PICTURE = "picture";
	
	private JSONParser jParser;
	private JSONArray friends;
	public  ArrayList<HashMap<String, String>> friendsList;
	
	ProgressDialog pDialog;
	GridView gridView;
	LazyAdapter lazyAdapter;
	
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.grid_layout);
	 
	        gridView = (GridView) findViewById(R.id.grid_view);
	        
	        jParser = new JSONParser();
	        friends = new JSONArray();
	        
	        friendsList = new ArrayList<HashMap<String, String>>();
	        
	        new GetFriendsList().execute();
	    }
	  
	  
	  	public class GetFriendsList extends AsyncTask<String, String, String> {

			@Override
			protected void onPreExecute() {

				super.onPreExecute();
				  pDialog = new ProgressDialog(GridFriends.this);
		          pDialog.setMessage("Loading Friends List ...");
		          pDialog.setIndeterminate(false);
		          pDialog.setCancelable(true);
		          pDialog.show();
			}
			
		@Override
		protected String doInBackground(String... args) {
			
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("access_token", MainActivity.facebook.getAccessToken()));

						JSONObject json = jParser.makeHttpRequest(FRIENDS_URL, "GET",params);
//						Log.d("My friends : ", json.toString());
						
						try {
								friends = json.getJSONArray(TAG_DATA);
							  	JSONObject c = null;
								String id;
								for (int i = 0; i < friends.length() ; i++) {
									HashMap<String, String> map = new HashMap<String, String>();
									c = friends.getJSONObject(i);
									id = c.getString(TAG_ID);
									map.put(TAG_ID,id);
									map.put(TAG_NAME, c.getString(TAG_NAME));
									map.put(TAG_PICTURE, INFO_URL+id+"/picture");
									
									friendsList.add(map);  //Add only user with home or location known
								}
							
						} catch (JSONException e) {
//							Log.d("Erreur", e.getMessage());
						}
					
						return null;
				}
	
		
		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			runOnUiThread(new Runnable() {
					@Override
					public void run() {
						
						lazyAdapter = new LazyAdapter(GridFriends.this, friendsList);
						gridView.setAdapter(lazyAdapter);
						
					}
				});
			
			/**
			 * OnClick Item listener sur la liste des amis
			 */
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent,
						View v, int position, long id) {

					new GetFriendsInfos().execute(""+position);
					
				}
				
			});
		}
		
	  }
	  	
	  	public class GetFriendsInfos extends AsyncTask<String, String, String>{
	  		String name;
			String home = null;
			String local = " ***** ";
			String birth = " ***** ";
			String relation = " ***** ";
			String id;
			
	  		@Override
	  		protected void onPreExecute() {
	  			super.onPreExecute();
	  		  pDialog = new ProgressDialog(GridFriends.this);
	          pDialog.setMessage("Locating ...");
	          pDialog.setIndeterminate(false);
	          pDialog.setCancelable(true);
	          pDialog.show();
	  		}
			@Override
			protected String doInBackground(String... args) {
				
				int pos = Integer.parseInt(args[0]);
				id = friendsList.get(pos).get(TAG_ID);
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("access_token", MainActivity.facebook.getAccessToken()));
				params.add(new BasicNameValuePair("fields", "hometown,name,birthday,location,relationship_status"));
			
				String url = "https://graph.facebook.com/" + id;
				JSONObject friend = jParser.makeHttpRequest(url, "GET", params);
				
				try {	home = friend.getJSONObject(GridFriends.TAG_HOME).getString(GridFriends.TAG_NAME);
//						Log.d("INFO", home);
				} catch (JSONException e) { //Log.d("Erreur", e.getMessage());
					}
				
				if(home != null) {
					
					try {	local = friend.getJSONObject(GridFriends.TAG_LOCATION).getString(GridFriends.TAG_NAME);
//							Log.d("INFO", local);
					} catch (JSONException e) { //Log.d("Erreur", e.getMessage()); 
						
					}
						
					try {	birth = friend.getString(GridFriends.TAG_BIRHT);
//							Log.d("INFO", birth);
				} catch (JSONException e) { //Log.d("Erreur", e.getMessage()); }
				}
					try {	name =  friend.getString(GridFriends.TAG_NAME);
//							Log.d("INFO", name);
					} catch (JSONException e) { //Log.d("Erreur", e.getMessage());
						
					}
					
					try {	relation = friend.getString(GridFriends.TAG_RELATION);
//							Log.d("INFO", relation);
				} catch (JSONException e) { //Log.d("Erreur", e.getMessage()); 
					
				}
					
				}
				
				return null;
			}
			
			@Override
			protected void onPostExecute(String result) {
				pDialog.dismiss();
				super.onPostExecute(result);
				if(home == null) {
					Toast.makeText(getApplicationContext(), "Unknown location for this user", Toast.LENGTH_LONG).show();
				}
				else {
					Intent i = new Intent(getApplicationContext(), MymapActivity.class);
					i.putExtra(TAG_ID, id);
					i.putExtra(TAG_NAME, name);
					i.putExtra(TAG_BIRHT, birth);
					i.putExtra(TAG_HOME, home);
					i.putExtra(TAG_LOCATION, local);
					i.putExtra(TAG_RELATION, relation);
					
					startActivity(i);
				}
			}
	  		
	  	}
}
	  