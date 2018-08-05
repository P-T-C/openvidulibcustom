package vnext.com.openvidu.handlers;

import android.os.SystemClock;

public class HandlersUtils {
	private long mLastClickTime = 0;

	public boolean checkDoubleClick() {
		if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
			return false;
		}
		mLastClickTime = SystemClock.elapsedRealtime();
		return true;
	}
}
