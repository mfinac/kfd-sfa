package com.datamation.kfdupgradesfa.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.libs.progress.ProgressWheel;


/**
 *
 * The custom built progress dialog with material progress animations.
 */
public class CustomProgressDialogUpdated extends Dialog {

    private TextView tvProgressText;
    private ProgressWheel progressWheel;
    private Context context;
    private String initMessage;
    private String exitMessage;

    public CustomProgressDialogUpdated(Context context, String initMsg, String exitMsg) {
        super(context, false, null);
        this.context = context;
        this.initMessage = initMsg;
        this.exitMessage = exitMsg;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom_progress);

        tvProgressText = (TextView)findViewById(R.id.progress_dialog_progress_txt);
        progressWheel = (ProgressWheel)findViewById(R.id.progress_dialog_progress_wheel);

        progressWheel.setBarColor(context.getResources().getColor(R.color.blueColor));
        progressWheel.spin();

        tvProgressText.setText(initMessage);
        tvProgressText.setText(initMessage);

    }

    public void setInitMessage() {
        if(tvProgressText != null) {
            tvProgressText.setText(initMessage);
        }
    }

    public void setExitMessage() {
        if(tvProgressText != null) {
            tvProgressText.setText(exitMessage);
        }
    }
}
