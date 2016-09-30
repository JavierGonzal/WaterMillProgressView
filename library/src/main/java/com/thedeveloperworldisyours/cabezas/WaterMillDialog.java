package com.thedeveloperworldisyours.cabezas;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;

/**
 * Created by javierg on 30/09/2016.
 */

public class WaterMillDialog extends DialogFragment {

    Dialog mDialog;

    public WaterMillDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mDialog == null) {
            mDialog = new Dialog(getActivity(), R.style.water_mill);
            mDialog.setContentView(R.layout.water_mill);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.getWindow().setGravity(Gravity.CENTER);
        }
        return mDialog;
    }
}
