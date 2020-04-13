package gala.com.kidstv.Connectivity;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by admin on 06/04/17.
 */

public class GEAlertDialog {

    public static void checkForAlertbox(Context context){
        androidx.appcompat.app.AlertDialog.Builder builder =new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please turn on Internet connection to continue");
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
