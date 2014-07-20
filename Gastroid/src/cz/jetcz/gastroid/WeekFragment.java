package cz.jetcz.gastroid;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class WeekFragment extends Fragment {
	View rootView;
	ProgressBar pd;
	WebView wv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_week, container,	false);
		wv = (WebView) rootView.findViewById(R.id.wvWeek);
		wv.setBackgroundColor(0x60000000);	
		wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		return rootView;
	}
	
	public void onStart(){
		super.onStart();
		((GastroidActivity)getActivity()).setRefreshActionButtonState(true);	
		RequestQueue queue = Volley.newRequestQueue(getActivity());			
		StringRequest stringRequest = new StringRequest("http://gastrom.cz/aktualni-tyden", new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {						
				Document doc = Jsoup.parse(response);						
				Elements e = doc.select("div#tyden-page");
				for( Element element : doc.select("div.header.print"))  element.remove();
				for( Element element : doc.select("div.footer.print"))  element.remove();
				for( Element element : doc.select("div.icon"))  element.remove();
				for( Element element : doc.select("h2"))  element.tagName("h3");
				for( Element element : doc.select("span.class"))  element.tagName("h3");					
				String jidelnicek = e.toString();	
				String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> <link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />";
				String htmlData = header+jidelnicek;
				wv.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "charset=UTF-8", null);
				wv.setBackgroundColor(0x60000000);
				((GastroidActivity)getActivity()).setRefreshActionButtonState(false);	
			}
		}, new Response.ErrorListener(){					
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
