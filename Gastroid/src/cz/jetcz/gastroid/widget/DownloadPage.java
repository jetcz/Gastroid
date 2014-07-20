package cz.jetcz.gastroid.widget;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.util.Log;

public class DownloadPage extends AsyncTask<String, String, ArrayList<String>> {
	
	private ArrayList<String> stringArrayList = new ArrayList<String>();
	
	@Override
	  protected ArrayList<String> doInBackground(String... params) {
		Log.d("AsyncTask", "stahuji stranku");
		  String url = "http://gastrom.cz";
		  	Document doc = null;
			try {
				doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements jidelnicek = doc.select("div#jidelnicek li").not("*.overlined");	
			for (Element x : jidelnicek) { 
				stringArrayList.add(x.text());
			}

	   return stringArrayList;
	  }

	  @Override
	  protected void onPostExecute(ArrayList<String> stringArrayList) {
		  setStringArrayList(stringArrayList);			  
	  }

	  @Override
	  protected void onPreExecute() {

	  }


	  @Override
	  protected void onProgressUpdate(String... text) {

	  }

	public ArrayList<String> getStringArrayList() {
		return stringArrayList;
	}

	public void setStringArrayList(ArrayList<String> stringArrayList) {
		this.stringArrayList = stringArrayList;
	}
	 }