package com.robertogl.audiobalance; 
    
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.IXposedHookLoadPackage;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.AudioTrack;

public class MainNew implements IXposedHookLoadPackage{
	private final static float MAX_VOLUME = 100;
	
	public float do_math_left(float volume_left, float volume_right){
			if (volume_right > volume_left){
				return (float)(1 - Math.log(-volume_left + volume_right)/Math.log(MAX_VOLUME)); //for logarithmic function
			}
			if (volume_right < volume_left){
				return (float)(1);
			}
			else return (float)(1);
	}
	
	public float do_math_right(float volume_left, float volume_right){
			if (volume_right > volume_left){
				return (float)(1);
			}
			if (volume_right < volume_left){
				return (float)(1 - Math.log(+ volume_left - volume_right)/Math.log(MAX_VOLUME));
			}
			else return (float)(1);
	}
		
		@Override
	    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
			
			findAndHookMethod(MediaPlayer.class, "start", new XC_MethodHook() { 
	            @Override
	            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
	            	((MediaPlayer) param.thisObject).setVolume(do_math_left(Preferences.left(),Preferences.right()), do_math_right(Preferences.left(),Preferences.right()));
	            }
	            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
	            }
	        });
			
			findAndHookMethod(MediaPlayer.class, "setVolume", float.class, float.class, new XC_MethodHook() { 
	            @Override
	            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
	            	param.args[0] = do_math_left(Preferences.left(),Preferences.right());
	            	param.args[1] = do_math_right(Preferences.left(),Preferences.right());
	            	}
	            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
	            }
	        });
			
			findAndHookMethod(SoundPool.class, "setVolume", int.class, float.class, float.class, new XC_MethodHook() {
	            @Override
	            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
	            	param.args[1]= do_math_left(Preferences.left(),Preferences.right());
	                param.args[2]= do_math_right(Preferences.left(),Preferences.right());
	            }
	            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
	            }
	        });
			findAndHookMethod(AudioTrack.class, "play", new XC_MethodHook() { 
	            @Override
	            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
	            	if (Preferences.right()>Preferences.left()){
	            		((AudioTrack) param.thisObject).setStereoVolume( 1 - ((Preferences.right() - Preferences.left()) / MAX_VOLUME), 1);
	            		}
	            	if (Preferences.right()<Preferences.left()){
	            		((AudioTrack) param.thisObject).setStereoVolume(1, 1 - (( - Preferences.right() + Preferences.left()) / MAX_VOLUME));
		            	}
	            	else {
	            		((AudioTrack) param.thisObject).setStereoVolume(1, 1);
		            	}
	            }
	            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
	            }
	        });
			
		}
	 
}
