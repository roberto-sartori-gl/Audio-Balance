package com.robertogl.audiobalance; 
  import de.robv.android.xposed.XSharedPreferences;

public class Preferences {
    // This current package.
    private static final String PACKAGE_NAME = "com.robertogl.audiobalance";

    // Our single instance of an XSharedPreferences.
    private static XSharedPreferences instance = null;
    private static XSharedPreferences getInstance() {
        if (instance == null) {
            instance = new XSharedPreferences(PACKAGE_NAME);
            instance.makeWorldReadable();
        } else {
            instance.reload();
        }
        
        return instance;
    }
    
    public static float right() {
        return Float.valueOf(getInstance().getString("right", ""));
    }
    
    public static float left() {
    	return Float.valueOf(getInstance().getString("left", ""));
    }

}