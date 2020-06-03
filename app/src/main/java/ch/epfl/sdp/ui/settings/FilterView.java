package ch.epfl.sdp.ui.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.google.android.material.checkbox.MaterialCheckBox;

import ch.epfl.sdp.R;


/**
 * View for the events filters
 */
public class FilterView extends FrameLayout {
    public static final int MIN_VALUE = 1;

    public SeekBar mSeekBarRange;
    public TextView mSeekBarValue;
    public MaterialDialog mDialog;

    public MaterialCheckBox mOptionIndoor;
    public MaterialCheckBox mOptionOutdoor;
    public MaterialCheckBox mOptionSport;
    public MaterialCheckBox mOptionParty;

    /**
     * Constructor of the FilterView
     *
     * @param context
     * @param attrs
     */
    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.menu_main_search, this);

        mSeekBarRange = findViewById(R.id.seekBar_range);
        mSeekBarValue = findViewById(R.id.seekBar_value);

        mOptionIndoor = findViewById(R.id.optionIndoor);
        mOptionOutdoor = findViewById(R.id.optionOutdoor);
        mOptionSport = findViewById(R.id.optionSport);
        mOptionParty = findViewById(R.id.optionParty);

        mDialog = new MaterialDialog(context, MaterialDialog.getDEFAULT_BEHAVIOR());
//        mDialog.title(null, "Event parameter");

        DialogCustomViewExtKt.customView(mDialog, 0, findViewById(R.id.seekBar), false, false, true, true);

        mDialog.positiveButton(null, "Choose", null);
    }

    /**
     * Method to display the FilterView
     */
    public void show() {
        mDialog.show();
    }
}
