package it.chengdazhi.styleimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by chengdazhi on 8/10/16.
 */
public class StyleImageView extends ImageView {
    private Styler styler;

    public StyleImageView(Context context) {
        super(context);
        init();
    }

    public StyleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        readAttrFromXml(attrs, 0);
    }

    public StyleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        readAttrFromXml(attrs, defStyleAttr);
    }

    private void init() {
        styler = new Styler.Builder(this, Styler.Mode.NONE).build();
    }

    private void readAttrFromXml(AttributeSet attributeSet, int defStyleAttr){
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.StyleImageView, defStyleAttr, 0);
        styler.setMode(typedArray.getInt(R.styleable.StyleImageView_style, -1));
        styler.setBrightness(typedArray.getInt(R.styleable.StyleImageView_brightness, 0));
        styler.setContrast(typedArray.getFloat(R.styleable.StyleImageView_contrast, 1));
        float saturation = typedArray.getFloat(R.styleable.StyleImageView_saturation, 1);
        if (styler.getMode() != Styler.Mode.SATURATION && saturation != 1) {
            if (styler.getMode() != Styler.Mode.NONE) {
                throw new IllegalStateException("Mode must be SATURATION when saturation is set in xml");
            } else {
                styler.setMode(Styler.Mode.SATURATION);
                styler.setSaturation(saturation);
            }
        }
        boolean animate = typedArray.getBoolean(R.styleable.StyleImageView_animate, false);
        long animationDuration = typedArray.getInt(R.styleable.StyleImageView_animation_duration, 0);
        if (!animate && animationDuration != 0) {
            throw new IllegalStateException("Animate can't be false when animation_duration is set");
        } else if (animate) {
            styler.enableAnimation(animationDuration);
        }
        typedArray.recycle();
        updateStyle();
    }

    public void updateStyle() {
        styler.updateStyle();
    }

    public void clearStyle() {
        styler.clearStyle();
    }

    public boolean isAnimationEnabled() {
        return styler.isAnimationEnabled();
    }

    public long getAnimationDuration() {
        return styler.getAnimationDuration();
    }

    public StyleImageView enableAnimation(long animationDuration) {
        styler.enableAnimation(animationDuration);
        return this;
    }

    public StyleImageView disableAnimation() {
        styler.disableAnimation();
        return this;
    }

    public int getBrightness() {
        return styler.getBrightness();
    }

    public StyleImageView setBrightness(int brightness) {
        styler.setBrightness(brightness);
        return this;
    }

    public float getContrast() {
        return styler.getContrast();
    }

    public StyleImageView setContrast(float contrast) {
        styler.setContrast(contrast);
        return this;
    }

    public float getSaturation() {
        return styler.getSaturation();
    }

    public StyleImageView setSaturation(float saturation) {
        styler.setSaturation(saturation);
        return this;
    }

    public int getMode() {
        return styler.getMode();
    }

    public StyleImageView setMode(int mode) {
        styler.setMode(mode);
        return this;
    }

    public Bitmap getBitmap() {
        return styler.getBitmap();
    }

    public Bitmap getBitmap(int width, int height) {
        return styler.getBitmap(width, height);
    }
}
