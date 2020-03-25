package ch.epfl.sdp.ui.event;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.facebook.FacebookActivity;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;

import ch.epfl.sdp.databinding.EventFragmentBinding;

public class ShareEvent implements View.OnClickListener{
    private EventFragmentBinding mBinding;
    private ShareDialog mShareDialog;
    private ShareContent mShareContent;
    private Fragment mFragment;
    private ShareLinkContent mShareLinkContent;

    public ShareEvent(Fragment fragment, @NonNull EventFragmentBinding binding) {
        mBinding = binding;
        mFragment = fragment;
        mShareDialog = new ShareDialog(mFragment);
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) mBinding.imageView.getDrawable());
        Bitmap bitmap = bitmapDrawable.getBitmap();
        mShareContent = new ShareMediaContent.Builder()
                .setShareHashtag(
                new ShareHashtag.Builder().setHashtag(
                        mBinding.title.getText().toString() + " :\n\n " +
                                mBinding.description.getText().toString()).build())
                .addMedium(new SharePhoto.Builder().setBitmap(bitmap).build())
                .build();

        mBinding.sharingButton.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        if(view.getId() == mBinding.sharingButton.getId()){
                mShareDialog.show(mFragment,mShareContent);
        }
    }
}

