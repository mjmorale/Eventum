package ch.epfl.sdp.ui.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public SeekBar mSeekBarRange;
    public TextView mSeekBarValue;

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_filters, this);

        mSeekBarRange = findViewById(R.id.seekBar_range);
        mSeekBarValue = findViewById(R.id.seekBar_value);

        String[] args = {"All","Party", "Sport", "AA", "Concert"};
        List<String> list = Arrays.asList(args);

        MaterialDialog dialog = new MaterialDialog(context, MaterialDialog.getDEFAULT_BEHAVIOR());
        dialog.title(null, "Event parameter");

        DialogCustomViewExtKt.customView(dialog, 0, findViewById(R.id.seekBar), false, false, true, true);

        dialog.message(null, "More precices  ? ", null);
        int[] selected = new int[]{};
        DialogMultiChoiceExtKt.listItemsMultiChoice(dialog, null, list, null, selected, true, false, (materialDialog, ints, strings) -> null);
        dialog.positiveButton(null, "Choose", null);
        dialog.show();
    }
}
