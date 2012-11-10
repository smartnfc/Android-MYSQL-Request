/*
 * Class for do request to a MYSQL Database (One class in your android project and one PHP file)
 * 
 * @Author Michaël Minelli
 * @Version 1.1.0
 * 
 * More info at https://github.com/MichaelMinelli/Android-MYSQL-Request
 * 
 * Project under Creative Commons License (CC-BY-SA) (Paternity, Modification allowed (Published under the same condition), sharing allowed, commercial use (free) allowed)
 */

package ch.minelli.MYSQL_Request;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class MYSQL_Request
{
	// Variable for show DEBUG message in LogCat
	private final boolean		DEBUG			= true;

	// Initialization vector and secret key for encryption (CHANGE IT! AND CHANGE IN THE PHP FILE BY THE SAME VALUE)
	private String				iv				= "fedcba9876543210";	// Must be 16bits
	private String				SecretKey		= "0123456789abcdef"; //Must be 16bits

	// Variable for the connection to the databse
	private String				URL, dbname, host, username, password, request;

	// Variable for store the result
	String						server_result;
	private int					NB_Responce		= 0;
	private int					actual_index	= -1;
	private JSONArray			result;
	ArrayList<NameValuePair>	gidentifiers;
	boolean						isNoQueryRequest;

	/**
	 * Get the URL value
	 */
	public String getURL()
	{
		return URL;
	}

	/**
	 * Set the URL value
	 */
	public void setURL(String uRL)
	{
		URL = uRL;
	}

	/**
	 * Get the UserName of the Database
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * Set the UserName of the Database
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * Get the Database Name of the Database
	 */
	public String getDbname()
	{
		return dbname;
	}

	/**
	 * Set the Database Name of the Database
	 */
	public void setDbname(String dbname)
	{
		this.dbname = dbname;
	}

	/**
	 * Get the Host address of the Database
	 */
	public String getHost()
	{
		return host;
	}

	/**
	 * Set the Host address of the Database
	 */
	public void setHost(String host)
	{
		this.host = host;
	}

	/**
	 * Get the password of the user of the Database
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Set the password of the user of the Database
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * Get the request
	 */
	public String getRequest()
	{
		return request;
	}

	/**
	 * Set the request
	 */
	public void setRequest(String request)
	{
		this.request = request;
	}

	/**
	 * Constructor of the class with all the information for login to the database
	 */
	public MYSQL_Request(String php_page, String databaseName, String hostName, String user, String pass) {
		setURL(php_page);
		setDbname(databaseName);
		setHost(hostName);
		setUsername(user);
		setPassword(pass);
	}

	/**
	 * If possible get the next value of the result
	 * 
	 * @return If a entry is found
	 */
	public boolean getNextEntry()
	{
		if (++actual_index < NB_Responce)
			return true;
		else
			return false;
	}

	/**
	 * Reset the index of the entry in the result of the database
	 */
	public void resetIndex()
	{
		actual_index = -1;
		NB_Responce = 0;
	}

	/**
	 * Function for get the Json value of the actual index in the result of the database
	 * 
	 * @return The JSON value
	 */
	public JSONObject getJsonValue()
	{
		// -1 is a code for get the JSON value of the element of the actual
		// index
		return getJsonValue(-1);
	}

	/**
	 * Get the JSON value of a specified index
	 * 
	 * @param index
	 *            The index of the search result
	 * @return The JSON value
	 */
	public JSONObject getJsonValue(int index)
	{
		// Verify if it is the code for take the actual index
		if (index == -1)
			// Verify if the actual index is not initialized
			if (actual_index == -1)
			{
				// Set the actual index to the first
				actual_index = 0;
				try
				{
					return result.getJSONObject(actual_index);
				}
				catch (Exception e)
				{}
			}
			else
				try
				{
					// Return the actual JSON value
					return result.getJSONObject(actual_index);
				}
				catch (Exception e)
				{}
		else
			try
			{
				// Return the requested JSON value
				return result.getJSONObject(index);
			}
			catch (Exception e)
			{}

		return null;
	}

	/**
	 * Execute the request without identifiers
	 * 
	 * It's for request without responce
	 */
	public void executeRequest()
	{
		executeRequest(null);
	}

	/**
	 * Execute the request with the identifiers
	 * 
	 * It's for request without responce
	 * 
	 * @param identifiers
	 *            Identifies of the website
	 */
	public void executeRequest(ArrayList<NameValuePair> identifiers)
	{
		launchRequest(identifiers, true);
	}

	/**
	 * Execute the request without identifiers Stock the response of the page
	 */
	public void getServerData()
	{
		getServerData(null);
	}

	/**
	 * Execute the request with identifiers Stock the response of the page
	 * 
	 * @param identifiers
	 *            Identifies of the website
	 */
	public void getServerData(ArrayList<NameValuePair> identifiers)
	{
		launchRequest(identifiers, false);
	}

	/**
	 * Launch the request in a different thread and wait for the response
	 * 
	 * @param identifiers
	 *            Identifies of the website
	 * @param isNoQuery
	 *            Is it a no query request
	 */
	private void launchRequest(ArrayList<NameValuePair> identifiers, boolean isNoQuery)
	{
		isNoQueryRequest = isNoQuery;
		gidentifiers = identifiers;
		getServerData_Thread work_thread;
		work_thread = new getServerData_Thread();
		work_thread.start();
		while (work_thread.isAlive())
		{}
	}

	/**
	 * the thread for execute the request (for the 4.0.0+ compatibility)
	 * 
	 * @author Michaël Minelli
	 */
	class getServerData_Thread extends Thread
	{
		@SuppressWarnings("deprecation")
		public void run()
		{
			// Reset the index value
			resetIndex();
			
			//Initialize the encryption class
			MCrypt mcrypt = new MCrypt(iv, SecretKey);

			InputStream is = null;
			String server_result = "", url_reel = "";

			try
			{
				// Encode the url
				url_reel = URL + "?dbname=" + URLEncoder.encode(MCrypt.bytesToHex(mcrypt.encrypt(getDbname())), "UTF-8") + "&host=" + URLEncoder.encode(MCrypt.bytesToHex(mcrypt.encrypt(getHost())), "UTF-8") + "&username=" + URLEncoder.encode(MCrypt.bytesToHex(mcrypt.encrypt(getUsername())), "UTF-8") + "&password=" + URLEncoder.encode(MCrypt.bytesToHex(mcrypt.encrypt(getPassword())), "UTF-8") + "&request=" + URLEncoder.encode(getRequest(), "UTF-8");
			}
			catch (UnsupportedEncodingException e1)
			{
				if (DEBUG) e1.printStackTrace();
				this.stop();
			}

			// Get the identifiers
			if (gidentifiers == null) gidentifiers = new ArrayList<NameValuePair>();

			try
			{
				// Show debug informations
				if (DEBUG)
				{
					Log.d("request", request);
					Log.d("url", url_reel);
				}

				// Send an http request to the URL an take the result
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url_reel);
				httppost.setEntity(new UrlEncodedFormEntity(gidentifiers));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			}
			catch (Exception e)
			{
				if (DEBUG) Log.d("log_tag", "Error in http connection " + e.toString());
			}

			// Don't enter if it's a no query request
			if (!isNoQueryRequest)
			{
				try
				{
					// Construct the string result
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null)
					{
						sb.append(line + "\n");
					}

					is.close();
					server_result = new String(mcrypt.decrypt(sb.toString()));
				}
				catch (Exception e)
				{
					if (DEBUG) Log.d("log_tag", "Error converting result " + e.toString());
				}

				try
				{
					actual_index = -1;

					// Verify if a result found
					if (server_result.equals("null\n"))
					{
						result = null;
						NB_Responce = 0;
					}
					else
					{
						result = new JSONArray(server_result);
						NB_Responce = result.length();
					}
				}
				catch (JSONException e)
				{
					if (DEBUG) Log.d("log_tag", "Error parsing data " + e.toString());
				}
			}
		}
	}
}
