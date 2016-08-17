package com.example.styleimageview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by chengdazhi on 8/10/16.
 */
public class Styler {
    private boolean enableAnimation;
    private long animationDuration;
    private int brightness;
    private float contrast;
    private float saturation;
    private int mode;
    private Drawable drawable;
    private float[] oldMatrix = StyleMatrixs.COMMON;
    private ValueAnimator animator;

    private Styler(Drawable drawable, boolean animate, long animationDuration, int brightness, float contrast, float saturation, int mode) {
        this.enableAnimation = animate;
        this.animationDuration = animationDuration;
        this.brightness = brightness;
        this.contrast = contrast;
        this.saturation = saturation;
        this.mode = mode;
        this.drawable = drawable;
    }

    public void updateStyle() {
        final float[] matrix = calculateMatrix();
        if (enableAnimation) {
            animateMatrix(oldMatrix, matrix, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    setDrawableStyleByMatrix(drawable, matrix);
                }
            });
        } else {
            setDrawableStyleByMatrix(drawable, matrix);
        }
    }

    public void clearStyle() {
        if (enableAnimation) {
            animateMatrix(oldMatrix, StyleMatrixs.COMMON, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    drawable.clearColorFilter();
                    mode = Mode.NONE;
                }
            });
        } else {
            drawable.clearColorFilter();
            mode = Mode.NONE;
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
                setDrawableStyleByMatrix(drawable, result);
                oldMatrix = result.clone();
            }
        });
        animator.addListener(listenerAdapter);
        animator.start();
    }

    public void setDrawableStyleByMatrix(Drawable drawable, float[] matrix) {
        drawable.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(matrix)));
        oldMatrix = matrix.clone();
    }

    private float[] calculateMatrix() {
        return applyBrightnessAndContrast(getMatrixByMode(mode));
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

    public float[] getSaturationMatrix(float saturation) {
        final float lumR = 0.3086F;
        final float lumG = 0.6094F;
        final float lumB = 0.0820F;
        float sr = (1 - saturation) * lumR;
        float sg = (1 - saturation) * lumG;
        float sb = (1 - saturation) * lumB;
        float[] result = StyleMatrixs.COMMON.clone();
        result[0] = sr + saturation;
        result[1] = sg;
        result[2] = sb;
        result[5] = sr;
        result[6] = saturation + sg;
        result[7] = sb;
        result[10] = sr;
        result[11] = sg;
        result[12] = saturation + sb;
        return result;
    }

    public boolean isEnableAnimation() {
        return enableAnimation;
    }

    public long getAnimationDuration() {
        return animationDuration;
    }

    public Styler turnOnAnimate(long animationDuration) {
        enableAnimation = true;
        this.animationDuration = animationDuration;
        return this;
    }

    public Styler turnOffAnimate() {
        enableAnimation = false;
        animationDuration = 0;
        return this;
    }

    public int getBrightness() {
        return brightness;
    }

    public Styler setBrightness(int brightness) {
        this.brightness = brightness;
        return this;
    }

    public float getContrast() {
        return contrast;
    }

    public Styler setContrast(float contrast) {
        this.contrast = contrast;
        return this;
    }

    public float getSaturation() {
        return saturation;
    }

    public Styler setSaturation(float saturation) {
        mode = Mode.SATURATION;
        this.saturation = saturation;
        return this;
    }

    public int getMode() {
        return mode;
    }

    public Styler setMode(int mode) {
        this.mode = mode;
        if (mode != Mode.SATURATION) {
            saturation = 1;
        }
        return this;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public static class Builder {
        private boolean animate = false;
        private long animationDuration = 0;
        private int brightness = 0;
        private float contrast = 1;
        private float saturation = 1;
        private int mode = Mode.NONE;
        private Drawable drawable;

        public Styler build() {
            return new Styler(drawable, animate, animationDuration, brightness, contrast, saturation, mode);
        }

        public Builder(View view, int mode) {
            this.mode = mode;
            if (view instanceof ImageView) {
                drawable = ((ImageView) view).getDrawable();
            } else {
                drawable = view.getBackground();
            }
            if (drawable == null) {
                throw new IllegalArgumentException("view doesn't have a drawable");
            }
        }

        public Builder(Drawable drawable, int mode) {
            this.drawable = drawable;
            this.mode = mode;
            if (drawable == null) {
                throw new IllegalArgumentException("drawable can't be null");
            }
        }

        public Builder turnOnAnimate(long animationDuration) {
            animate = true;
            this.animationDuration = animationDuration;
            return this;
        }

        public Builder turnOffAnimate() {
            animate = false;
            animationDuration = 0;
            return this;
        }

        public Builder setBrightness(int brightness) {
            if (brightness > 255) {
                throw new IllegalArgumentException("brightness can't be bigger than 255");
            } else if (brightness < -255) {
                throw new IllegalArgumentException("brightness can't be smaller than -255");
            }
            this.brightness = brightness;
            return this;
        }

        public Builder setContrast(float contrast) {
            if (contrast < 0) {
                throw new IllegalArgumentException("contrast can't be smaller than 0");
            }
            this.contrast = contrast;
            return this;
        }

        //this clears the original style
        public Builder setSaturation(float saturation) {
            mode = Mode.SATURATION;
            this.saturation = saturation;
            return this;
        }
    }

    public static class Mode {
        public static final int NONE = -1;
        public static final int SATURATION = 0;
        public static final int GREY_SCALE = 1;
        public static final int INVERT = 2;
        public static final int RGB_TO_BGR = 3;
        public static final int SEPIA = 4;
        public static final int BLACK_AND_WHITE = 5;
        public static final int BRIGHT = 6;
        public static final int VINTAGE_PINHOLE = 7;
        public static final int KODACHROME = 8;
        public static final int TECHNICOLOR = 9;
    }

    public float[] getMatrixByMode(int mode) {
        float[] targetMatrix;
        switch (mode) {
            case Mode.NONE:
                targetMatrix = StyleMatrixs.COMMON.clone();
                break;
            case Mode.GREY_SCALE:
                targetMatrix = StyleMatrixs.GREY_SCALE.clone();
                break;
            case Mode.INVERT:
                targetMatrix = StyleMatrixs.INVERT.clone();
                break;
            case Mode.RGB_TO_BGR:
                targetMatrix = StyleMatrixs.RGB_TO_BGR.clone();
                break;
            case Mode.SEPIA:
                targetMatrix = StyleMatrixs.SEPIA.clone();
                break;
            case Mode.BRIGHT:
                targetMatrix = StyleMatrixs.BRIGHT.clone();
                break;
            case Mode.BLACK_AND_WHITE:
                targetMatrix = StyleMatrixs.BLACK_AND_WHITE.clone();
                break;
            case Mode.VINTAGE_PINHOLE:
                targetMatrix = StyleMatrixs.VINTAGE_PINHOLE.clone();
                break;
            case Mode.KODACHROME:
                targetMatrix = StyleMatrixs.KODACHROME.clone();
                break;
            case Mode.TECHNICOLOR:
                targetMatrix = StyleMatrixs.TECHNICOLOR.clone();
                break;
            case Mode.SATURATION:
                targetMatrix = getSaturationMatrix(saturation);
                break;
            default:
                throw new IllegalArgumentException("mode not supported!");
        }
        return targetMatrix;
    }

}
