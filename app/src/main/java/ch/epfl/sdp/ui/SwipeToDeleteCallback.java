package ch.epfl.sdp.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import ch.epfl.sdp.R;

/**
 * ItemTouchHelper callback that display a red banner and a bin when trying to dismiss a recycler
 * view item.
 * Taken from https://www.journaldev.com/23164/android-recyclerview-swipe-to-delete-undo
 */
public abstract class SwipeToDeleteCallback extends ItemTouchHelper.Callback {

    private Paint mClearPaint;
    private int mBackgroundColor;
    private ColorDrawable mBackground;
    private Drawable mDeleteDrawable;
    private int mDeleteIconColor;
    private int mIntrinsicWidth;
    private int mIntrinsicHeight;

    /**
     * Create a new SwipeToDeleteCallback for a given context
     * @param context The current application context
     */
    public SwipeToDeleteCallback(Context context) {
        mBackground = new ColorDrawable();
        mBackgroundColor = ContextCompat.getColor(context, R.color.red);
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mDeleteDrawable = ContextCompat.getDrawable(context, R.drawable.ic_delete_forever_black_24dp);
        mDeleteIconColor = ContextCompat.getColor(context, R.color.white);
        mIntrinsicWidth = mDeleteDrawable.getIntrinsicWidth();
        mIntrinsicHeight = mDeleteDrawable.getIntrinsicHeight();
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        if (dX == 0 && !isCurrentlyActive) {
            clearCanvas(c, itemView.getRight() + dX, (float)itemView.getTop(), (float)itemView.getRight(), (float)itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        mBackground.setColor(mBackgroundColor);
        mBackground.setBounds(itemView.getRight() + (int)dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        mBackground.draw(c);

        int deleteIconTop = itemView.getTop() + (itemHeight - mIntrinsicHeight) / 2;
        int deleteIconMargin = (itemHeight - mIntrinsicHeight) / 2;
        int deleteIconLeft = itemView.getRight() - deleteIconMargin - mIntrinsicWidth;
        int deleteIconRight = itemView.getRight() - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + mIntrinsicHeight;

        mDeleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        mDeleteDrawable.setTint(mDeleteIconColor);
        mDeleteDrawable.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void clearCanvas(Canvas c, float left, float top, float right, float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }

}
