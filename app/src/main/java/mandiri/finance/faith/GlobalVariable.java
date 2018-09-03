package mandiri.finance.faith;

import android.app.Application;
import android.content.Context;

public class GlobalVariable extends Application {
	private static String data = "";
	
	 /**
     * Keeps a reference of the application context
     */
    private static Context sContext;
 
    @Override
    public void onCreate() {
        super.onCreate();
 
        sContext = getApplicationContext();
 
    }
 
    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {
        return sContext;
    }
    
	public static String getResult(){
		return data;
	}
	
	public static void setResult(String nilai){
		data = nilai;
	}
	
	/* penggunaan variabel global
	 * 
	 * GlobalVariable g =(GlobalVariable)getApplication();
	 * String data = g.getData();
	 * g.setData(data);
	 */
}
