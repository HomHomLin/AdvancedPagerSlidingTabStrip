package com.lhh.apst.advancedpagerslidingtabstrip;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by linhonghong on 2015/9/28.
 */
public class FrescoThumbnailView extends SimpleDraweeView {

    private final static String HTTP_PERFIX = "http://";
    private final static String HTTP_PERFIXS = "https://";

    private String mThumbnailUri = null;

    private ImageRequest mRequest;

    private boolean mAnim = false;//默认开启动画

    private DraweeController mController;

    public FrescoThumbnailView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public FrescoThumbnailView(Context context) {
        super(context);
    }

    public FrescoThumbnailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrescoThumbnailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void setController(int resid){
        if(resid == 0){
            return;
        }

        mRequest = ImageRequestBuilder.newBuilderWithResourceId(resid)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();

        mController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(mRequest)
                .setAutoPlayAnimations(mAnim)
                .setOldController(this.getController())
                .build();

        this.setController(mController);
    }

    private void setController(Uri uri){

        if(uri == null){
            return;
        }

        mRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        mController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(mRequest)
                .setAutoPlayAnimations(mAnim)
                .setOldController(this.getController())
                .build();
        this.setController(mController);
    }

    public void loadView(String thumbnailUri, int defaultResID) {
        try {
            if (thumbnailUri == null || thumbnailUri.length() <= 0) {
                this.getHierarchy().setPlaceholderImage(defaultResID);
                this.setController(defaultResID);
                mThumbnailUri = thumbnailUri;
                return;
            }
            mThumbnailUri = thumbnailUri;

            if (mThumbnailUri.startsWith(HTTP_PERFIX) || mThumbnailUri.startsWith(HTTP_PERFIXS)) {

                Uri uri = Uri.parse(mThumbnailUri);
                this.getHierarchy().setPlaceholderImage(defaultResID);

                setController(uri);

//			if(mThumbnailPath.contains(KasGlobalDef.CIRCLE_IMAGE)){
//				ExtraFuncMgr.Instance().loadView(this, thumbnailUri, defaultResID, 1000);
//			}else if(mThumbnailPath.contains(KasGlobalDef.PARTCIRCLE_IMAGE)){
//				ExtraFuncMgr.Instance().loadView(this,
//						thumbnailUri, defaultResID,
//						(int) KasUtil.getRawSize(TypedValue.COMPLEX_UNIT_DIP, 5, KasConfigManager.mApplication));
//			}else{
//			}

            } else {
                this.getHierarchy().setPlaceholderImage(defaultResID);
                this.setController(defaultResID);
            }

        }catch (OutOfMemoryError e){

        }

    }

    public void loadLocalImageNoshowImageOnLoading(String path, int defaultRes){
        this.getHierarchy().setPlaceholderImage(defaultRes);
        if(null == path || path.length() == 0){
            this.setController(defaultRes);
            return;
        }
        if(!path.startsWith("file://")){
            path = "file://" + path;
        }
        Uri uri = Uri.parse(path);
        setController(uri);

    }

    public void setCornerRadius(float radius){
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(radius);
        this.getHierarchy().setRoundingParams(roundingParams);
    }

    public void setCornerRadius(float radius, int overlay_color){
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(radius).
                setRoundingMethod(RoundingParams.RoundingMethod.OVERLAY_COLOR).
                setOverlayColor(overlay_color);
        this.getHierarchy().setRoundingParams(roundingParams);
    }

    public void setCircle(int overlay_color){
        RoundingParams roundingParams = RoundingParams.asCircle().
                setRoundingMethod(RoundingParams.RoundingMethod.OVERLAY_COLOR).
                setOverlayColor(overlay_color);
        this.getHierarchy().setRoundingParams(roundingParams);
    }

    public void setAnim(boolean b){
        mAnim = b;
    }
}
