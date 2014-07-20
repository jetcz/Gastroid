package cz.jetcz.gastroid.widget;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import cz.jetcz.gastroid.R;

public class ViewsFactory implements RemoteViewsService.RemoteViewsFactory {

	private Context ctxt=null;
	private int appWidgetId;
	RemoteViews views;
	private ArrayList<String> food = new ArrayList<String>();


  public ViewsFactory(Context ctxt, Intent intent) {
      this.ctxt=ctxt;
      appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
  }  
  
  @Override
  public void onCreate() {
	  //vsechno se dela v onDataSetChange
  }
  
  public ArrayList<String> getFood() {
	return food;
}

public void setFood(ArrayList<String> food) {
	this.food = food;
}

@Override
  public void onDestroy() {
    // no-op
  }

  @Override
  public int getCount() {
    return food.size();
  }

  @Override
  public RemoteViews getViewAt(int position) {	  
	 Log.d("getViewAt", "vypisuju jidla"); 
    RemoteViews row=new RemoteViews(ctxt.getPackageName(), R.layout.row);
    row.setTextViewText(android.R.id.text1, food.get(position));
    
    Intent i=new Intent();
    Bundle extras=new Bundle();
    
    i.putExtras(extras);
    row.setOnClickFillInIntent(android.R.id.text1, i);

    return(row);
  }
 

  @Override
  public RemoteViews getLoadingView() {
    return(null);
  }
  
  @Override
  public int getViewTypeCount() {
    return(1);
  }

  @Override
  public long getItemId(int position) {
    return(position);
  }

  @Override
  public boolean hasStableIds() {
    return(true);
  }

  @Override
  public void onDataSetChanged() {
	  Log.d("onDataSetChanged", "volani aktualizace");	   
	  DownloadPage dp = new DownloadPage();
	
		try {
			dp.execute().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("onDataSetChanged", "AsyncThread selhal");
		} //potrebuju pockat az dobehne asynchronni vlakno, proto .get()
   
	   food = dp.getStringArrayList();


	   
  }
}