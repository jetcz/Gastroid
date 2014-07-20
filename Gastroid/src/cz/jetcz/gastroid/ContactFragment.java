package cz.jetcz.gastroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;


public class ContactFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_contact,
				container, false);			

		final ImageView img = (ImageView) rootView.findViewById(R.id.img);
		img.setVisibility(ImageView.INVISIBLE);
		TextView tvPhone = (TextView) rootView.findViewById(R.id.tvPhone);
		TextView tvEmail = (TextView) rootView.findViewById(R.id.tvEmail);
		TableRow tr1 = (TableRow) rootView.findViewById(R.id.tr1);
		tr1.setBackgroundColor(0xcbc60325);
		TableLayout tl = (TableLayout) rootView.findViewById(R.id.tl);
		tl.setBackgroundColor(0x60000000);
		LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.ll);
		ll.setBackgroundColor(0x60000000);
		
		final double latitude = 50.218892;
		final double longitude = 15.876992;
		
		String url = 	"http://maps.googleapis.com/maps/api/staticmap?center="
						+ latitude + "," + longitude
						+ "&zoom=15&size=1000x1000&markers="
						+ latitude + "," + longitude
						+ "&sensor=false";
	
		RequestQueue queue = Volley.newRequestQueue(getActivity());
		ImageRequest imgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {				 
		    @Override
		    public void onResponse(Bitmap response) {			    		    	
		        img.setImageBitmap(response);
				img.setVisibility(ImageView.VISIBLE);
				img.setBackgroundColor(0x60000000); 
		    }
		}, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
	        @Override
	        public void onErrorResponse(VolleyError error) {	
	        	
	        }
	    });
	queue.add(imgRequest);			

		img.setClickable(true);
		img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {				
				try {
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + latitude + ","+ longitude));
					startActivity(intent);
					
				} catch (Exception e) {
					Log.d(getActivity().getClass().getName(), "unable to handle ACTION_VIEW (geo) intent, no handler found");
				}				
			}

		});

		tvPhone.setClickable(true);
		tvPhone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {				
				try {
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:+420 495 445 367"));
					startActivity(intent);
					
				} catch (Exception e) {
					Log.d(getActivity().getClass().getName(), "unable to handle ACTION_DIAL intent, no handler found");
				}
			}
		});
		
		tvEmail.setClickable(true);
		tvEmail.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {				
				try {
					Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
				            "mailto","info@gastrom.cz", null));
				startActivity(Intent.createChooser(emailIntent, "Send email..."));
				} catch (Exception e) {
					Log.d(getActivity().getClass().getName(), "unable to handle ACTION_SENDTO intent, no handler found");
				}				
			}
		});

		return rootView;
	}
}
