package apps.com.pratilipi;

import android.app.Application;


/**
 * Created by Nithin
 */
public class MyApplication extends Application {

    private String someVariable;

    public String getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}