/***
	Copyright (c) 2010 CommonsWare, LLC
	
	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */

package mandiri.finance.faith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class LocationReceiver extends BroadcastReceiver {
	private AlarmManager mgr = null;
	private SQLiteDatabase db;
	private DataSource datasource;
	private SharedPreferences generalPrefs;
		
	@Override
	public void onReceive(Context context, Intent intent) {
			
		generalPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		datasource = new DataSource(context);
		db = datasource.getWritableDatabase();
		
		
		Calendar calTo = new GregorianCalendar();
	    calTo.setTimeInMillis(System.currentTimeMillis());
	    calTo.set(Calendar.HOUR_OF_DAY, 18);
	    calTo.set(Calendar.MINUTE, 00);
	    calTo.set(Calendar.SECOND, 0);
	    calTo.set(Calendar.MILLISECOND, 0);    
	    
	    Intent i= new Intent(context, LocationPoller.class);
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
	    		0, i, PendingIntent.FLAG_NO_CREATE);
	    	    		
	    if (System.currentTimeMillis() > calTo.getTimeInMillis() && pendingIntent != null){
	    	System.out.println("Stop alarm");
	    	
	    	mgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			
			Bundle bundle = new Bundle();
			LocationPollerParameter parameter = new LocationPollerParameter(bundle);
			parameter.setIntentToBroadcastOnCompletion(new Intent(context, LocationReceiver.class));
			
			parameter.setProviders(new String[] {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER});
			parameter.setTimeout(120000);
			i.putExtras(bundle);
			
			PendingIntent pi=PendingIntent.getBroadcast(context, 0, i, 0);
	    	mgr.cancel(pi);
	    	pi.cancel();
	    	db.close();
	    	
	    	return;
	    }	            
		
		ContentValues cv = new ContentValues();
		Bundle b=intent.getExtras();
		LocationPollerResult locationResult = new LocationPollerResult(b);
     
		Location loc=locationResult.getLocation();
		String msg;

		if (loc==null) {
			loc=locationResult.getLastKnownLocation();

			if (loc==null) {
				msg=null;
			}
			else {
				msg=null;
			}
		}
		else {
			msg=loc.toString();
		}
		
		Calendar cl = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String myTime = df.format(cl.getTime());
        
		if(msg!=null){
			cv.put("lat", loc.getLatitude());
			cv.put("lng", loc.getLongitude());
			cv.put("dtmupd", myTime);
			cv.put("userid", generalPrefs.getString("userID", null));
			cv.put("sourceid", generalPrefs.getString("sourceid", null));
			
			datasource.generateData(db, cv, "locationsource");
			cv.clear();			
		}
		
		if (db.isOpen()) {
			db.close();
		}
	    
	}	    
}
