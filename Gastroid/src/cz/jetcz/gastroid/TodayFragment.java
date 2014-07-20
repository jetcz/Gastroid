package cz.jetcz.gastroid;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class TodayFragment extends Fragment{
	View rootView;
	WebView wv;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_today, container, false);		
		wv = (WebView) rootView.findViewById(R.id.wvToday);				
		wv.setBackgroundColor(0x60000000);
		wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		return rootView;	
	}
	

	public void onStart(){	
		super.onStart();
		((GastroidActivity)getActivity()).setRefreshActionButtonState(true);		
		RequestQueue queue = Volley.newRequestQueue(getActivity());
		//pouziju volley string request pro ziskani cely html stranky ve stringu
		StringRequest stringRequest = new StringRequest("http://gastrom.cz", new Response.Listener<String>() {
		
			@Override
			public void onResponse(String response) {
				Document doc = Jsoup.parse(response);	 //prevedu si pomoci jsoup ziskanou stranku ze stringu na doc				
				String jidelnicek = doc.select("ul.polevky").toString() + doc.select("ol.jidla").toString();		//nahazim si vybrane komonenty (ul a ol) z html kodu do noveho stringu s				
				String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> <link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />";
				String htmlData = header+jidelnicek; //pripravim si nove html s css pro webview
				wv.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "charset=UTF-8", null); //naplnim webview
				wv.setBackgroundColor(0x60000000);
				((GastroidActivity)getActivity()).setRefreshActionButtonState(false);	

			} //konec onResponse
		} //konec stringRequest
		, new Response.ErrorListener(){					
				public void onErrorResponse(VolleyError error) {				
					((GastroidActivity)getActivity()).setRefreshActionButtonState(false);	
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
					builder.setMessage("Nepodaøilo se pøipojit k serveru").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) {				            	
			            	
			            };});
					AlertDialog alert = builder.create();
				    alert.show();
				    
			}					
		});			
		queue.add(stringRequest);		
	}	
}
