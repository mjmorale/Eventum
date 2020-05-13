package ch.epfl.sdp.ui.createevent;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Text view that shows completion suggestions
 */
public class DelayAutoCompleteTextView extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {
    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 750;

    private int mAutoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY;
    private ProgressBar mLoadingIndicator;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DelayAutoCompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
        }
    };

    /**
     * The constructor of the DelayAutoCompleteTextView
     *
     * @param context
     * @param attrs
     */
    public DelayAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Method to set a loading indicator
     *
     * @param progressBar to be used as a loading indicator
     */
    public void setLoadingIndicator(ProgressBar progressBar) {
        mLoadingIndicator = progressBar;
    }

    /**
     * Method to set a delay to the auto completion
     *
     * @param autoCompleteDelay the delay
     */
    public void setAutoCompleteDelay(int autoCompleteDelay) { mAutoCompleteDelay = autoCompleteDelay; }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text), mAutoCompleteDelay);
    }

    @Override
    public void onFilterComplete(int count) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.GONE);
        }
        super.onFilterComplete(count);
    }
}
