package com.lapakkreatiflamongan.smdsforce.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import com.lapakkreatiflamongan.smdsforce.R;
import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;


public class UtilsHelper {

    private Context appContext;
    private Activity appActivity;

    Dialog dialog = null;

    private DecimalFormat formatter;
    private DecimalFormatSymbols symbol;

    public UtilsHelper(Context appContext, Activity appActivity) {
        this.appContext = appContext;
        this.appActivity = appActivity;
    }

    // TODO TUTORIAL : HANDY 08/01/2020
    public MaterialIntroView showTutorial (View target, String id, String text, Boolean performClick, MaterialIntroListener listener) {
        if (target == null) {
            return null;
        }

        return new MaterialIntroView.Builder(appActivity)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(100)
                .enableFadeAnimation(false)
                .performClick(performClick)
                .enableDotAnimation(false)
                .setInfoText(text)
                .setTarget(target)
                .setUsageId(id)
                .setListener(listener)
                .dismissOnTouch(true)
                .show();

    }

    public static void showDialogErrorNetwork(Context appContext,String problem,String errCode) {
        new AlertDialog.Builder(appContext)
                .setTitle(appContext.getString(R.string.app_name))
                .setCancelable(false)
                .setMessage(problem+" Error code ("+errCode+")"+ NetworkUtils.code(errCode))
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public String loadJsonAsset(String loadJson) {
        String json = null;
        try {
            InputStream is = appContext.getAssets().open(loadJson);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                json = new String(buffer, StandardCharsets.UTF_8);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.d("response file json", "" + loadJson + ": " + json);
        return json;
    }

    public void showLoadingProgress(String s) {
        dialog = new Dialog(appContext);
        dialog.setContentView(R.layout.d_logindownload);
        dialog.setCancelable(false);

        final TextView TxtStatus = (TextView) dialog.findViewById(R.id.Login_DStatus);
        TxtStatus.setText("Mohon Tunggu. . ");
        dialog.show();
    }

    public void hideLoadingProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static String unFormat(String value) {

        DecimalFormatSymbols symbol = new DecimalFormatSymbols(Locale.GERMANY);
        symbol.setCurrencySymbol("");

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.GERMANY);
        formatter.setDecimalFormatSymbols(symbol);
        formatter.setMaximumFractionDigits(0);

        value = value.replaceAll(symbol.getCurrencySymbol(), "");
        value = value.replaceAll("\\.", "");
        value = value.replaceAll(" ", "");
        value = value.replaceAll("\\s", "");

        if (value.length() == 0) {
            value = "0";
        }

        return value;
    }

}