package ch.epfl.sdp.ui.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.afollestad.materialdialogs.list.DialogMultiChoiceExtKt;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sdp.R;


public class FilterView extends FrameLayout {
    public static final int MIN_VALUE = 1;

    public SeekBar mSeekBarRange;
    public TextView mSeekBarValue;
    public MaterialDialog mDialog;

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.menu_main_search, this);

        mSeekBarRange = findViewById(R.id.seekBar_range);
        mSeekBarValue = findViewById(R.id.seekBar_value);

        String[] args = {"All","Party", "Sport", "AA", "Concert"};
        List<String> list = Arrays.asList(args);

        mDialog = new MaterialDialog(context, MaterialDialog.getDEFAULT_BEHAVIOR());
        mDialog.title(null, "Event parameter");

        DialogCustomViewExtKt.customView(mDialog, 0, findViewById(R.id.seekBar), false, false, true, true);

        mDialog.message(null, "More precices  ? ", null);
        int[] selected = new int[]{};
        DialogMultiChoiceExtKt.listItemsMultiChoice(mDialog, null, list, null, selected, true, false, (materialDialog, ints, strings) -> null);
        mDialog.positiveButton(null, "Choose", null);
    }

    public void show() {
        mDialog.show();
    }
}
