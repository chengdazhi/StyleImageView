package it.chengdazhi.styleimageview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by chengdazhi on 8/10/16.
 */
public class Styler {
    private boolean enableAnimation;
    private Interpolator interpolator;
    private long animationDuration;
    private int brightness;
    private float contrast;
    private float saturation;
    private int mode;
    private DrawableHolder drawableHolder;
    private float[] oldMatrix = StyleMatrixs.common();
    private ValueAnimator animator;

    private Styler(Builder builder) {
        super();
        enableAnimation = builder.enableAnimation;
        animationDuration = builder.animationDuration;
        brightness = builder.brightness;
        contrast = builder.contrast;
        saturation = builder.saturation;
        mode = builder.mode;
        drawableHolder = builder.drawableHolder;
        interpolator = builder.interpolator;
    }

    public void updateStyle() {
        if (drawableHolder.getDrawable() == null) {
            return;
        }
        final float[] matrix = calculateMatrix(mode, brightness, contrast, saturation);
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
        if (drawableHolder.getDrawable() == null) {
            return;
        }
        if (enableAnimation) {
            animateMatrix(oldMatrix, StyleMatrixs.common(), new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    drawableHolder.getDrawable().clearColorFilter();
                    mode = Mode.NONE;
                }
            });
        } else {
            drawableHolder.getDrawable().clearColorFilter();
            mode = Mode.NONE;
        }
    }

    private void animateMatrix(final float[] startMatrix, final float[] endMatrix, AnimatorListenerAdapter onAnimationEndListener) {
        if (animator != null) {
            animator.cancel();
        }
        animator = ValueAnimator.ofFloat(0F, 1F).setDuration(animationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float[] result = new float[20];
                float fraction = valueAnimator.getAnimatedFraction();
                float ratio = interpolator.getInterpolation(fraction);
                for (int i = 0; i < 20; i++) {
                    result[i] = (startMatrix[i] * (1 - ratio)) + (endMatrix[i] * ratio);
                }
                setDrawableStyleByMatrix(result);
                oldMatrix = result.clone();
            }
        });
        animator.addListener(onAnimationEndListener);
        animator.start();
    }

    private void setDrawableStyleByMatrix(float[] matrix) {
        if (drawableHolder.getDrawable() == null) {
            return;
        }
        drawableHolder.getDrawable().setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(matrix)));
        oldMatrix = matrix.clone();
    }

    private static float[] calculateMatrix(int mode, int brightness, float contrast, float saturation) {
        return applyBrightnessAndContrast(getMatrixByMode(mode, saturation), brightness, contrast);
    }

    private static float[] applyBrightnessAndContrast(float[] matrix, int brightness, float contrast) {
        float t = (1.0F - contrast) / 2.0F * 255.0F;
        for (int i = 0; i < 3; i++) {
            for (int j = i * 5; j < i * 5 + 3; j++) {
                matrix[j] *= contrast;
            }
            matrix[5 * i + 4] += t + brightness;
        }
        return matrix;
    }

    private static float[] getMatrixByMode(int mode, float saturation) {
        float[] targetMatrix;
        switch (mode) {
            case Mode.NONE:
                targetMatrix = StyleMatrixs.common();
                break;
            case Mode.GREY_SCALE:
                targetMatrix = StyleMatrixs.greyScale();
                break;
            case Mode.INVERT:
                targetMatrix = StyleMatrixs.invert();
                break;
            case Mode.RGB_TO_BGR:
                targetMatrix = StyleMatrixs.rgbToBgr();
                break;
            case Mode.SEPIA:
                targetMatrix = StyleMatrixs.sepia();
                break;
            case Mode.BRIGHT:
                targetMatrix = StyleMatrixs.bright();
                break;
            case Mode.BLACK_AND_WHITE:
                targetMatrix = StyleMatrixs.blackAndWhite();
                break;
            case Mode.VINTAGE_PINHOLE:
                targetMatrix = StyleMatrixs.vintagePinhole();
                break;
            case Mode.KODACHROME:
                targetMatrix = StyleMatrixs.kodachrome();
                break;
            case Mode.TECHNICOLOR:
                targetMatrix = StyleMatrixs.technicolor();
                break;
            case Mode.SATURATION:
                targetMatrix = getSaturationMatrix(saturation);
                break;
            default:
                throw new IllegalArgumentException("mode not supported!");
        }
        return targetMatrix;
    }

    private static float[] getSaturationMatrix(float saturation) {
        final float lumR = 0.3086F;
        final float lumG = 0.6094F;
        final float lumB = 0.0820F;
        float sr = (1 - saturation) * lumR;
        float sg = (1 - saturation) * lumG;
        float sb = (1 - saturation) * lumB;
        float[] result = StyleMatrixs.common();
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

    public boolean isAnimationEnabled() {
        return enableAnimation;
    }

    public long getAnimationDuration() {
        return animationDuration;
    }

    public Styler enableAnimation(long animationDuration) {
        enableAnimation(animationDuration, new LinearInterpolator());
        return this;
    }

    public Styler enableAnimation(long animationDuration, Interpolator interpolator) {
        enableAnimation = true;
        this.animationDuration = animationDuration;
        this.interpolator = interpolator;
        return this;
    }

    public Styler disableAnimation() {
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
        return drawableHolder.getDrawable();
    }

    public Bitmap getBitmap() {
        Drawable drawable = drawableHolder.getDrawable();
        return getBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    public Bitmap getBitmap(int width, int height) {
        Drawable drawable = drawableHolder.getDrawable();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap addStyleToBitmap(Context context, Bitmap bitmap, int mode) {
        return addStyleToBitmap(context, bitmap, mode, 0, 1, 1);
    }

    public static Bitmap addStyleToBitmap(Context context, Bitmap bitmap, int mode, int brightness, float contrast, float saturation) {
        if (saturation != 1 && (mode != Mode.SATURATION || mode != Mode.NONE)) {
            throw new IllegalArgumentException("saturation must be 1.0 when mode is not Styler.Mode.SATURATION");
        }
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        context = context.getApplicationContext();
        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        drawable.setColorFilter(new ColorMatrixColorFilter(calculateMatrix(mode, brightness, contrast, saturation)));
        drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        drawable.draw(canvas);
        return newBitmap;
    }

    public static class DrawableHolder {
        private View view;
        private Drawable drawable;
        private boolean isView;

        public DrawableHolder(Drawable drawable) {
            this.drawable = drawable;
            isView = false;
        }

        public DrawableHolder(View view) {
            this.view = view;
            isView = true;
        }

        public Drawable getDrawable() {
            if (isView) {
                if (view instanceof ImageView && ((ImageView) view).getDrawable() != null) {
                    return ((ImageView) view).getDrawable();
                } else {
                    return view.getBackground();
                }
            } else {
                return drawable;
            }
        }
    }

    public static class Builder {
        private boolean enableAnimation = false;
        private Interpolator interpolator;
        private long animationDuration = 0;
        private int brightness = 0;
        private float contrast = 1;
        private float saturation = 1;
        private int mode = Mode.NONE;
        private DrawableHolder drawableHolder;

        public Styler build() {
            return new Styler(this);
        }

        public Builder(View view, int mode) {
            this.mode = mode;
            drawableHolder = new DrawableHolder(view);
        }

        public Builder(Drawable drawable, int mode) {
            this.mode = mode;
            if (drawable == null) {
                throw new IllegalArgumentException("drawable can't be null");
            }
            drawableHolder = new DrawableHolder(drawable);
        }

        public Builder enableAnimation(long animationDuration) {
            enableAnimation(animationDuration, new LinearInterpolator());
            return this;
        }

        public Builder enableAnimation(long animationDuration, Interpolator interpolator) {
            enableAnimation = true;
            this.animationDuration = animationDuration;
            this.interpolator = interpolator;
            return this;
        }

        public Builder disableAnimation() {
            enableAnimation = false;
            animationDuration = 0;
            interpolator = null;
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

        //this method clears the original style
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

}
