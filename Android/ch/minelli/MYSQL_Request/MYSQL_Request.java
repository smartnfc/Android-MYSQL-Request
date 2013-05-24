/*
 * Class for do request to a MYSQL Database (One class in your android project and one PHP file)
 * 
 * @Author Michaël Minelli
 * @Version 2.0.0
 * 
 * More info at https://github.com/MichaelMinelli/Android-MYSQL-Request
 * 
 * Project under Creative Commons License (CC-BY-SA) (Paternity, Modification allowed (Published under the same condition), sharing allowed, commercial use (free) allowed)
 */

package ch.minelli.MYSQL_Request;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MYSQL_Request
{
	// Variable for show DEBUG message in LogCat
	private final boolean DEBUG = false;

	// Initialization vector and secret key for encryption (CHANGE IT! AND CHANGE IN THE PHP FILE BY THE SAME VALUE)
	private String iv        = "fedcba9876543210";    // Must be 16bits
	private String SecretKey = "0123456789abcdef"; //Must be 16bits

	// Variable for the connection to the databse
	private String URL, dbname, host, username, password, request, resultID;

	// Variable for store the result
	int NB_Responce  = 0;
	int actual_index = -1;
	JSONArray                result;
	ArrayList<NameValuePair> gidentifiers;
	boolean                  isNoQueryRequest;
	boolean	isNeedToGetId;

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
	 * Get the last insert ID call by the function "executeRequestWithID"
	 */
	public String getResultID()
	{
		return resultID;
	}

	/**
	 * Constructor of the class with all the information for login to the database
	 */
	public MYSQL_Request(String php_page, String databaseName, String hostName, String user, String pass)
	{
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
		return ++actual_index < NB_Responce;
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
	 * @param index The index of the search result
	 *
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
				catch (Exception ignored)
				{}
			}
			else
				try
				{
					// Return the actual JSON value
					return result.getJSONObject(actual_index);
				}
				catch (Exception ignored)
				{}
		else
			try
			{
				// Return the requested JSON value
				return result.getJSONObject(index);
			}
			catch (Exception ignored)
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
	 * @param identifiers Identifies of the website
	 */
	public void executeRequest(ArrayList<NameValuePair> identifiers)
	{
		launchRequest(identifiers, true);
	}

	/**
	 * Execute the request and store the last insert ID
	 */
	public void executeRequestWithID()
	{
		executeRequestWithID(null);
	}

	/**
	 * Execute the request and store the last insert ID
	 *
	 * @param identifiers Identifies of the website
	 */
	public void executeRequestWithID(ArrayList<NameValuePair> identifiers)
	{
		isNeedToGetId = true;
		launchRequest(identifiers, false);
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
	 * @param identifiers Identifies of the website
	 */
	public void getServerData(ArrayList<NameValuePair> identifiers)
	{
		launchRequest(identifiers, false);
	}

	/**
	 * Launch the request in a different thread and wait for the response
	 *
	 * @param identifiers Identifies of the website
	 * @param isNoQuery   Is it a no query request
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
	 * The thread for execute the request (for the 4.0.0+ compatibility)
	 *
	 * @author Michaël Minelli
	 */
	class getServerData_Thread extends Thread
	{
		public void run()
		{
			// Reset the index value
			resetIndex();

			//Initialize the encryption class
			MCrypt mcrypt = new MCrypt(iv, SecretKey);

			InputStream is = null;
			String server_result = "";

			// Get the identifiers
			if (gidentifiers == null)
				gidentifiers = new ArrayList<NameValuePair>();

			try
			{
				// Show debug informations
				if (DEBUG)
				{
					Log.d("request", request);
					Log.d("url", URL);
				}

				//Parameter POST
				gidentifiers.add(new BasicNameValuePair("dbname", MCrypt.bytesToHex(mcrypt.encrypt(getDbname()))));
				gidentifiers.add(new BasicNameValuePair("host", MCrypt.bytesToHex(mcrypt.encrypt(getHost()))));
				gidentifiers.add(new BasicNameValuePair("username", MCrypt.bytesToHex(mcrypt.encrypt(getUsername()))));
				gidentifiers.add(new BasicNameValuePair("password", MCrypt.bytesToHex(mcrypt.encrypt(getPassword()))));
				gidentifiers.add(new BasicNameValuePair("request", getRequest()));
				if (isNeedToGetId)
					gidentifiers.add(new BasicNameValuePair("isNeedToGetId", "yes"));

				// Send an http request to the URL an take the result
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(URL);
				httppost.setEntity(new UrlEncodedFormEntity(gidentifiers));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			}
			catch (Exception e)
			{
				if (DEBUG)
					Log.d("log_tag", "Error in http connection " + e.toString());
			}

			// Don't enter if it's a no query request
			if (!isNoQueryRequest)
			{
				try
				{
					// Construct the string result
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 16384);
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null)
					{
						sb.append(line).append("\n");
					}

					is.close();
					server_result = new String(mcrypt.decrypt(sb.toString()));
				}
				catch (Exception e)
				{
					if (DEBUG)
						Log.d("log_tag", "Error converting result " + e.toString());
				}

				//Delete the null character
				if (server_result.contains("\u0000"))
					server_result = server_result.substring(0, server_result.indexOf("\u0000"));

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
						//If we need to récupérate ID who was insert
						if (isNeedToGetId)
							resultID = server_result;
						else
						{
							result = new JSONArray(server_result);
							NB_Responce = result.length();
						}
					}
				}
				catch (JSONException e)
				{
					if (DEBUG)
						Log.d("log_tag", "Error parsing data " + e.toString());
				}
			}

			isNeedToGetId = false;
		}
	}
}
