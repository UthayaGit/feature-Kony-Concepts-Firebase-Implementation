package kony.com.firebaseanalytics;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.konylabs.android.KonyMain;

import java.util.Iterator;

import org.json.JSONObject;


public class FireBaseActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_firebase);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(KonyMain.getAppContext());
        /*Button btnClick =(Button)findViewById(R.id.btnClick);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackEvents("Login_Event");
            }
        });

        Button btnClickScreen =(Button)findViewById(R.id.btnClickScreen);
        btnClickScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackScreenName("Login_Screen");
            }
        });

        Button btnCrash = (Button)findViewById(R.id.btnCrash);
        btnCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forceCrash();
            }
        }); */

    }

    public void initFirebaseAnalyticsFFI() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(KonyMain.getActContext());
    }

    public void trackScreenName(final String eventName) {
        Log.i("Firebase", "trackScreenName :: " + eventName);
        KonyMain context = KonyMain.getActivityContext();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        //mFirebaseAnalytics.setCurrentScreen(FirebaseActivity.this, eventName, eventName);

        KonyMain.getActivityContext().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFirebaseAnalytics.setCurrentScreen(KonyMain.getActContext(), eventName, eventName+"_Class");
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("App_Name", "Daman");
        bundle.putString("Screen_Name", eventName);
        bundle.putString("Screen_Class", eventName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "String");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.i("Firebase", "trackScreenName end:: " + KonyMain.getActivityContext().getClass().getSimpleName());
    }

    public void trackPageView(final String screenName) {
        Log.i("Firebase trackPageView", "TrackPageView :: " + KonyMain.getActContext().getClass().getSimpleName());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(KonyMain.getActContext());
        Log.i("Firebase_PageName ::", "TrackPageView::" + screenName);
        Bundle bundle = new Bundle();
        bundle.putString("App_Name", "Daman");
        bundle.putString("Screen_Name", screenName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "String");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        KonyMain.getActContext().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFirebaseAnalytics.setCurrentScreen(KonyMain.getActContext(), screenName, screenName+"_Class");
            }
        });

    }

    public void trackEvents(final String eventName, final String params, final String screenName) {
        Log.i("Firebase trackPageView", "screenName :: " + screenName);
        Log.i("Firebase trackPageView", "eventName :: " + eventName);
        Log.i("Firebase trackPageView", "params :: " + params);
        KonyMain context = KonyMain.getActivityContext();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(KonyMain.getActContext());
        if (params != null) {
            Bundle paramsBundle = new Bundle();
            paramsBundle = jsonStringToBundle(params);
            Log.d("Firebase_EventName ::", eventName);
            Log.d("Firebase_Params ::", bundle2string(paramsBundle));
            mFirebaseAnalytics.logEvent(eventName, paramsBundle);
            KonyMain.getActContext().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mFirebaseAnalytics.setCurrentScreen(KonyMain.getActContext(), screenName, screenName+"_Class");
                }
            });
        } else {
            Log.d("Firebase_EventName ::", eventName);
            mFirebaseAnalytics.logEvent(eventName, null);
        }
    }

    public void trackEvents(String eventName) {
        Log.d("Firebase_EventName ::", eventName);
        mFirebaseAnalytics.logEvent(eventName, null);
    }

    public void forceCrash() {
        Log.d("crashlyticsInstance", "crashlyticsInstance :: " + Crashlytics.getInstance().toString());
        Crashlytics.getInstance().crash();
    }

    public void trackFirebaseExceptions(String exception) {
        Log.d("trackFirebaseExceptions", "trackFirebaseExceptions :: ");
        Exception newExp = new Exception(exception);
        Crashlytics.logException(newExp);
    }

    public static Bundle jsonStringToBundle(String jsonString) {
        try {
            JSONObject jsonObject = toJsonObject(jsonString);
            return jsonToBundle(jsonObject);
        } catch (Exception e) {

        }
        return null;
    }

    public static JSONObject toJsonObject(String jsonString) throws Exception {
        return new JSONObject(jsonString);
    }

    public static Bundle jsonToBundle(JSONObject jsonObject) throws Exception {
        Bundle bundle = new Bundle();
        Iterator iter = jsonObject.keys();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String value = jsonObject.getString(key);
            bundle.putString(key, value);
        }
        return bundle;
    }

    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }

}
