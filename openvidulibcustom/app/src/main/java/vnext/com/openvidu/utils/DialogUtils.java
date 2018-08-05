package vnext.com.openvidu.utils;

import android.app.ProgressDialog;
import android.content.Context;

import vnext.com.openvidu.R;

public class DialogUtils {
 private  ProgressDialog loadingDialog;
 private Context context;
 public DialogUtils(Context context){
     loadingDialog = null;
     this.context = context;
 }
    public  void showLoadingDialog(){
        loadingDialog = ProgressDialog.show(context,  context.getResources().getString(R.string.null_text),
               context.getResources().getString(R.string.loading_message), true);
    }
    public  void dismissDialog(){
        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }
    }
}
