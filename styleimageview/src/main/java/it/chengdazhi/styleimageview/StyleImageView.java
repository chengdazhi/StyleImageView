package it.chengdazhi.styleimageview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by chengdazhi on 8/10/16.
 */
public class StyleImageView extends ImageView {
    private boolean enableAnimation;
    private long animationDuration;
    private int brightness;
    private float contrast;
    private float saturation;
    private int mode;
    private float[] oldMatrix = StyleMatrixs.COMMON;
    private ValueAnimator animator;

    public StyleImageView(Context context) {
        super(context);
    }

    public StyleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttrFromXml(attrs, 0);
    }

    public StyleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttrFromXml(attrs, defStyleAttr);
    }

    private void readAttrFromXml(AttributeSet attributeSet, int defStyleAttr){
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.StyleImageView, defStyleAttr, 0);
        mode = typedArray.getInt(R.styleable.StyleImageView_style, -1);
        brightness = typedArray.getInt(R.styleable.StyleImageView_brightness, 0);
        contrast = typedArray.getFloat(R.styleable.StyleImageView_contrast, 1);
        saturation = typedArray.getFloat(R.styleable.StyleImageView_saturation, 1);
        if (mode != Styler.Mode.SATURATION && saturation != 1) {
            if (mode != Styler.Mode.NONE) {
                throw new IllegalStateException("Mode must be SATURATION when saturation is set in xml");
            } else {
                mode = Styler.Mode.SATURATION;
            }
        }
        typedArray.recycle();
        updateStyle();
    }

    private Drawable getTargetDrawable() {
        if (getDrawable() != null) {
            return getDrawable();
        } else {
            return getBackground();
        }
    }

    public void updateStyle() {
        if (getTargetDrawable() == null) {
            return;
        }
        final float[] matrix = calculateMatrix();
        if (enableAnimation) {
            animateMatrix(oldMatrix, matrix, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    setDrawableStyleByMatrix(matrix);
                }
            });
        } else {
            setDrawableStyleByMatrix(matrix);
        }
    }

    public void clearStyle() {
        if (getTargetDrawable() == null) {
            return;
        }
        if (enableAnimation) {
            animateMatrix(oldMatrix, StyleMatrixs.COMMON, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    getTargetDrawable().clearColorFilter();
                    mode = Styler.Mode.NONE;
                }
            });
        } else {
            getTargetDrawable().clearColorFilter();
            mode = Styler.Mode.NONE;
        }
    }

    private void animateMatrix(final float[] startMatrix, final float[] endMatrix, AnimatorListenerAdapter listenerAdapter) {
        if (animator != null) {
            animator.cancel();
        }
        animator = ValueAnimator.ofFloat(0F, 1F).setDuration(animationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float[] result = new float[20];
                float fraction = valueAnimator.getAnimatedFraction();
                for (int i = 0; i < 20; i++) {
                    result[i] = (startMatrix[i] * (1 - fraction)) + (endMatrix[i] * fraction);
                }
                setDrawableStyleByMatrix(result);
                oldMatrix = result.clone();
            }
        });
        animator.addListener(listenerAdapter);
        animator.start();
    }

    private void setDrawableStyleByMatrix(float[] matrix) {
        if (getTargetDrawable() != null) {
            getTargetDrawable().setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(matrix)));
            oldMatrix = matrix.clone();
        }
    }

    private float[] calculateMatrix() {
        return applyBrightnessAndContrast(Styler.getMatrixByMode(mode, saturation));
    }

    public float[] applyBrightnessAndContrast(float[] matrix) {
        float t = (1.0F - contrast) / 2.0F * 255.0F;
        for (int i = 0; i < 3; i++) {
            for (int j = i * 5; j < i * 5 + 3; j++) {
                matrix[j] *= contrast;
            }
            matrix[5 * i + 4] += t + brightness;
        }
        return matrix;
    }

    public boolean isEnableAnimation() {
        return enableAnimation;
    }

    public long getAnimationDuration() {
        return animationDuration;
    }

    public StyleImageView turnOnAnimate(long animationDuration) {
        enableAnimation = true;
        this.animationDuration = animationDuration;
        return this;
    }

    public StyleImageView turnOffAnimate() {
        enableAnimation = false;
        animationDuration = 0;
        return this;
    }

    public int getBrightness() {
        return brightness;
    }

    public StyleImageView setBrightness(int brightness) {
        this.brightness = brightness;
        return this;
    }

    public float getContrast() {
        return contrast;
    }

    public StyleImageView setContrast(float contrast) {
        this.contrast = contrast;
        return this;
    }

    public float getSaturation() {
        return saturation;
    }

    public StyleImageView setSaturation(float saturation) {
        mode = Styler.Mode.SATURATION;
        this.saturation = saturation;
        return this;
    }

    public int getMode() {
        return mode;
    }

    public StyleImageView setMode(int mode) {
        this.mode = mode;
        if (mode != Styler.Mode.SATURATION) {
            saturation = 1;
        }
        return this;
    }
}
