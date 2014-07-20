package cz.jetcz.gastroid;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutFragment extends DialogFragment {
    int mNum;
    
    public static AboutFragment newInstance(int title) {
    	AboutFragment frag = new AboutFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //int title = getArguments().getInt("title");
     
        AlertDialog.Builder builder = new Builder(new ContextThemeWrapper(
        	    getActivity(),android.R.style.Theme_Holo));
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View view = factory.inflate(R.layout.fragment_about, null);
        
        builder.setTitle("O aplikaci Gastroid");
        
        TextView tvEmailJM = (TextView) view.findViewById(R.id.tvAuthorEmail);
        ImageView donate = (ImageView) view.findViewById(R.id.donate);
        
        donate.setImageResource(R.drawable.paypal);
        builder.setView(view);
        
        tvEmailJM.setClickable(true);
        tvEmailJM.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
			            "mailto","jiri.machacek87@gmail.com", null));
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Gastroid");
			startActivity(Intent.createChooser(emailIntent, "Send email..."));
			}
		});
        
        donate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=jiri%2emachacek87%40gmail%2ecom&lc=CZ&item_name=Ji%c5%99%c3%ad%20Mach%c3%a1%c4%8dek&item_number=Gastroid&currency_code=CZK&bn=PP%2dDonationsBF%3abtn_donate_SM%2egif%3aNonHosted"));
                startActivity(browserIntent);
            }
        });
  
        
        return builder.create();
    }

}
