/*
 * Copyright 2012 Midhun Harikumar
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package apps.com.pratilipi.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Utils {
	/**
	 * This method checks whether Internet connectivity is available on the device
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isInternetAvailable(Context context) {
		// Request the Connectivity service to the OS
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		// Check the current state of the Network Information
		if (networkInfo == null)
			return false;
		if (networkInfo.isConnected() == false)
			return false;
		if (networkInfo.isAvailable() == false)
			return false;
		return true;
	}

}