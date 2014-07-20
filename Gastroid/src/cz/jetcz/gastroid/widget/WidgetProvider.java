/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Advanced Android Development_
    http://commonsware.com/AdvAndroid
*/

   
package cz.jetcz.gastroid.widget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import cz.jetcz.gastroid.R;

public class WidgetProvider extends AppWidgetProvider {
  public static String REFRESH_BUTTON="APPWIDGET_UPDATE";
  public static String APP_BUTTON="APP_RUN";

  private String getDayOfCurrentMenu(Context ctxt) {
	Locale l = Locale.getDefault();
	int daynum = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	  switch(daynum){
	  case 1: return ctxt.getString(R.string.monday);
	  case 2: return ctxt.getString(R.string.monday);
	  case 3: return ctxt.getString(R.string.tuesday);
	  case 4: return ctxt.getString(R.string.wednesday);
	  case 5: return ctxt.getString(R.string.thursday);
	  case 6: return ctxt.getString(R.string.friday);
	  case 7: return ctxt.getString(R.string.monday);	  
	  }
	return "";
}
  
private String getCurrentTime() {
	SimpleDateFormat currentTimeDF = new SimpleDateFormat("HH:mm");
	return currentTimeDF.format(Calendar.getInstance().getTime());
}

  @Override
  public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager, //vola se po pridani widgetu a v pravidelych intervalech specifikovanych v widget_provider.xml
                        int[] appWidgetIds) {
	
    for (int i=0; i<appWidgetIds.length; i++) {
    	Log.d("appwidgetsID", "on update, widgets num "+String.valueOf(i));
     
      Intent svcIntent=new Intent(ctxt, WidgetService.class);      
      svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
      svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));     
      
      RemoteViews views = new RemoteViews(ctxt.getPackageName(), R.layout.widget);      
      views.setRemoteAdapter(appWidgetIds[i], R.id.words, svcIntent);      
      views.setOnClickPendingIntent(R.id.ivRefresh, getPendingSelfIntent(ctxt, REFRESH_BUTTON));
      views.setOnClickPendingIntent(R.id.ivLogo, getPendingSelfIntent(ctxt, APP_BUTTON));
      views.setOnClickPendingIntent(R.id.tvGastrom, getPendingSelfIntent(ctxt, APP_BUTTON));
      
      //overim konektivitu
      ConnectivityManager connMgr = (ConnectivityManager)
  	        ctxt.getSystemService(ctxt.CONNECTIVITY_SERVICE);
  	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
  	    if (networkInfo != null && networkInfo.isConnected()) { //pokud mam sit, proved aktualizaci, dne, casu a jidel
  	      views.setTextViewText(R.id.tvTime, getCurrentTime());
  	      views.setTextViewText(R.id.tvDay, getDayOfCurrentMenu(ctxt));
  	      appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.words);		
  	      appWidgetManager.updateAppWidget(appWidgetIds[i], views);
  	    } else {
  	    	Toast.makeText(ctxt, ctxt.getString(R.string.app_name)+": "+ctxt.getString(R.string.noconnectivity), Toast.LENGTH_SHORT).show();
  	    }
      
    }

    super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
  }
  
  public void onReceive(Context ctxt, Intent intent) { //vola se po stisknuti tlacitka refresh

	  /*nepotrebuju
	  ComponentName thisAppWidget = new ComponentName(ctxt.getPackageName(), WidgetProvider.class.getName());
	  int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);*/
	  super.onReceive(ctxt, intent);
	  if (REFRESH_BUTTON.equals(intent.getAction())) {
		  
		  ConnectivityManager connMgr = (ConnectivityManager) 
			        ctxt.getSystemService(ctxt.CONNECTIVITY_SERVICE);
			    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			    if (networkInfo != null && networkInfo.isConnected()) {
			  	  AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ctxt);
				  RemoteViews views = new RemoteViews(ctxt.getPackageName(), R.layout.widget);
				  Toast.makeText(ctxt, ctxt.getString(R.string.app_name)+": "+ctxt.getString(R.string.refresh_widget), Toast.LENGTH_SHORT).show();
				  //nastavi cas posledni aktualizace
				  views.setTextViewText(R.id.tvDay, getDayOfCurrentMenu(ctxt));
				  views.setTextViewText(R.id.tvTime, getCurrentTime());
				  int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(ctxt, WidgetProvider.class));
				  appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.words); //zavola metodu onDataSetChanged ve views factory (ziska novy data)
				  appWidgetManager.updateAppWidget(appWidgetIds, views);	//aktualizuje widget
			    } else {
			    	Toast.makeText(ctxt, ctxt.getString(R.string.app_name)+": "+ctxt.getString(R.string.noconnectivity), Toast.LENGTH_SHORT).show();
			    }

	      }
	  if(APP_BUTTON.equals(intent.getAction())){
		  Log.d("app", "run Gastroid");
		  //tady by se mela nejak spustit samotna aplikace, ale nevim jak
	      }	  
	  
  }	  

  //tohle nevim na co je.. je to "prefferable way of doing things"
  protected PendingIntent getPendingSelfIntent(Context context, String action) {
      Intent intent = new Intent(context, getClass());
      intent.setAction(action);
      return PendingIntent.getBroadcast(context, 0, intent, 0);
  }
	  
  }
