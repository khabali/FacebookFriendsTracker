package com.khabali.fft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class MainActivity extends Activity {
	
	// My Facebook APP ID
    private static final String APP_ID = "413581808678854";
    private final String[] PERMS = new String[] { "email", "publish_stream","friends_birthday","friends_hometown","friends_location" };

 
    // Instance of Facebook Class
    public static Facebook facebook;
    //private AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    
    
    //public static String access_token = null;
    //public static String id;
    
    public final  String TAG = "FACEBOOK";
	Button connect;
	
	OnClickListener onClickButton = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			if(isConnectingToInternet()) {
				
				if( !facebook.isSessionValid() ) 
					loginToFacebook();
				else {
					Intent i  = new Intent(getApplicationContext(), GridFriends.class);
					startActivity(i);
				}
			}
			else Toast.makeText(getApplicationContext(), "No Internet Network !", Toast.LENGTH_SHORT).show();
		}
	};

	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        facebook = new Facebook(APP_ID);
       // mAsyncRunner = new AsyncFacebookRunner(facebook);
        
        connect = (Button) findViewById(R.id.connectButton);
        connect.setOnClickListener(onClickButton);
        
        if(facebook.isSessionValid()) { 
        	connect.setBackgroundResource(R.drawable.tap);
        }
         
        String fontpath = "fonts/UrbanJungleDEMO.otf";
        TextView title = (TextView) findViewById(R.id.Title);
        Typeface tf = Typeface.createFromAsset(getAssets(),fontpath);
        title.setTypeface(tf);
        
    }
    
    @Override
    public void onResume() {    
        super.onResume();
        if(facebook.isSessionValid()) { 
        	connect.setBackgroundResource(R.drawable.tap);
        }
        facebook.extendAccessTokenIfNeeded(this, null);
    }
    
    /*
    public class GetProfileInfo extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			//getProfileInformation();
			return null;
		}
    	
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Intent i  = new Intent(getApplicationContext(), GridFriends.class);
			startActivity(i);
		}
    }

*/
    public void loginToFacebook() {
	/**
	 * Login to facebook account
	 */
    	
    	 	mPrefs = getPreferences(MODE_PRIVATE);
    	    String token = mPrefs.getString("access_token", null);
    	    long expires = mPrefs.getLong("access_expires", 0);
    	 
    	    if (token != null ) {
    	        
    	    	facebook.setAccessToken(token);
    	    	//access_token = facebook.getAccessToken();
//    	    	Log.d(TAG, "Token " + token);
    	    }
    	    
    	    if(expires != 0) {
    	    	facebook.setAccessExpires(expires);    
//    	        Log.d(TAG, "Expire " + expires);
    	    }
    	 
    	    if (!facebook.isSessionValid()) {
    	        facebook.authorize( this, PERMS,
    	                new DialogListener() {
    	                    @Override
    	                    public void onCancel() {
    	                        // Function to handle cancel event
//    	                    	Log.d(TAG, "OnCancel");
    	                    }
    	 
    	                    @Override
    	                    public void onComplete(Bundle values) {
    	                        // Function to handle complete event
    	                        // Edit Preferences and update facebook acess_token
    	                        SharedPreferences.Editor editor = mPrefs.edit();
    	                        
    	                        editor.putString("access_token", facebook.getAccessToken());
    	                        editor.putLong("access_expires", facebook.getAccessExpires());
    	                        
    	                        editor.commit();
    	                        
    	                        connect.setBackgroundResource(R.drawable.tap);
    	                        
    	                        
    	                        
    	                    }
    	 
    	                    @Override
    	                    public void onError(DialogError error) {
    	                        // Function to handle error
//    	                    	Log.d(TAG, "DialogError " + error.getMessage());
    	 
    	                    }
    	 
    	                    @Override
    	                    public void onFacebookError(FacebookError fberror) {
    	                        // Function to handle Facebook errors
//    	                    	Log.d(TAG,"FacebookError " + fberror.getMessage() );
    	 
    	                    }
    	 
    	                });
    	    }	    
    }
    
    /*
    public void postToWall() {
    	
    	
    	  // post on user's wall.
        facebook.dialog(this, "feed", new DialogListener() {
     
            @Override
            public void onFacebookError(FacebookError e) {
            }
     
            @Override
            public void onError(DialogError e) {
            }
     
            @Override
            public void onComplete(Bundle values) {
            }
     
            @Override
            public void onCancel() {
            }
        });
	} 
	
	*/
    
    /*
    public void getProfileInformation() {
    	
    	 mAsyncRunner.request("me", new RequestListener() {
    	        @Override
    	        public void onComplete(String response, Object state) {
    	            Log.d("Profile", response);
    	            String json = response;
    	            try {
    	            	
    	                 JSONObject profile = new JSONObject(json);
    	                 //id = profile.getString("id");
    	                  
    	                
    	            } catch (JSONException e) {
    	               Log.d("TAG","JsonExeption" + e.getMessage());
    	            }
    	        }
    	 
    	        @Override
    	        public void onIOException(IOException e, Object state) {
    	        }
    	 
    	        @Override
    	        public void onFileNotFoundException(FileNotFoundException e,
    	                Object state) {
    	        }
    	 
    	        @Override
    	        public void onMalformedURLException(MalformedURLException e,
    	                Object state) {
    	        }
    	 
    	        @Override
    	        public void onFacebookError(FacebookError e, Object state) {
    	        }
    	    });
    }
    
*/
    /*
    public void logoutFromFacebook() {
    	
    	mAsyncRunner.logout(this, new RequestListener() {
            @Override
            public void onComplete(String response, Object state) {
                Log.d("Logout from Facebook", response);
                if (Boolean.parseBoolean(response) == true) {
                    // User successfully Logged out
                }
            }
     
            @Override
            public void onIOException(IOException e, Object state) {
            }
     
            @Override
            public void onFileNotFoundException(FileNotFoundException e,
                    Object state) {
            }
     
            @Override
            public void onMalformedURLException(MalformedURLException e,
                    Object state) {
            }
     
            @Override
            public void onFacebookError(FacebookError e, Object state) {
            }
        });
	}
	*/
    
	   public boolean isConnectingToInternet(){
	        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	          if (connectivity != null)
	          {
	              NetworkInfo[] info = connectivity.getAllNetworkInfo();
	              if (info != null)
	                  for (int i = 0; i < info.length; i++)
	                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
	                      {
	                          return true;
	                      }
	 
	          }
	          return false;
	    }
    
}
