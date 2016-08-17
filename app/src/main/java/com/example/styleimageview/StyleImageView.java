package com.example.styleimageview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by chengdazhi on 8/10/16.
 */
public class StyleImageView extends ImageView {
    private float[] oldMatrix = null;
    private ValueAnimator animator;

    public StyleImageView(Context context) {
        super(context);
    }

    public StyleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StyleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
/*
    public void setStyle(int mode) {
        switch (mode) {
            case Styler.Mode.GREY:
            case Styler.Mode.INVERT:
            case Styler.Mode.RGB_TO_BGR:
            case Styler.Mode.SEPIA:
            case Styler.Mode.BLACK_AND_WHITE:
            case Styler.Mode.BRIGHT:
            case Styler.Mode.VINTAGE_PINHOLE:
            case Styler.Mode.KODACHROME:
            case Styler.Mode.TECHNICOLOR:
                setFloatMatrix(Styler.getMatrixByMode(mode));
                oldMatrix = Styler.getMatrixByMode(mode);
                break;
            default:
                throw new IllegalArgumentException("oldMode is not supported!");
        }
    }

    public void setStyle(int mode, long animationDuration) {
        switch (mode) {
            case Styler.Mode.GREY:
            case Styler.Mode.INVERT:
            case Styler.Mode.RGB_TO_BGR:
            case Styler.Mode.SEPIA:
            case Styler.Mode.BLACK_AND_WHITE:
            case Styler.Mode.BRIGHT:
            case Styler.Mode.VINTAGE_PINHOLE:
            case Styler.Mode.KODACHROME:
            case Styler.Mode.TECHNICOLOR:
                if (oldMatrix == null) {
                    animateToGivenMatrix(null, Styler.getMatrixByMode(mode), animationDuration);
                } else {
                    animateToGivenMatrix(oldMatrix, Styler.getMatrixByMode(mode), animationDuration);
                }
                break;
            default:
                throw new IllegalArgumentException("oldMode is not supported!");
        }
    }

    private void setFloatMatrix(float[] matrix) {
        setColorFilter(new ColorMatrixColorFilter(matrix));
    }

    public Bitmap getBitmap() {
        //这里可以new canvas，并在其上绘制。new Bitmap时要根据drawable的intrinsicWidth/Height来指定宽高
        this.setDrawingCacheEnabled(true);
        return getDrawingCache();
    }

    public void setBrightness(int brightness) {
        setBrightnessContrastSaturation(brightness, 1F, 1F);
    }

    public void setContrast(float contrast) {
        setBrightnessContrastSaturation(0, contrast, 1F);
    }

    public void setSaturation(float saturation) {
        setBrightnessContrastSaturation(0, 1F, saturation);
    }

    public void setBrightnessContrast(int brightness, float contrast) {
        setBrightnessContrastSaturation(brightness, contrast, 1F);
    }

    public void setBrightnessSaturation(int brightness, float saturation) {
        setBrightnessContrastSaturation(brightness, 1F, saturation);
    }

    public void setContrastSaturation(float contrast, float saturation) {
        setBrightnessContrastSaturation(0, contrast, saturation);
    }

    public void setBrightnessContrastSaturation(int brightness, float contrast, float saturation) {
        if (brightness < -255) {
            throw new IllegalArgumentException("brightness can't be smaller than -255");
        }
        if (brightness > 255) {
            throw new IllegalArgumentException("brightness can't be bigger than 255");
        }
        if (contrast < 0) {
            throw new IllegalArgumentException("contrast can't be smaller than 0");
        }
        setFloatMatrix(Styler.addBrightnessContrastSaturationMatrix(brightness, contrast, saturation));
    }

    public void animateToGivenMatrix(float[] oldMatrix, final float[] newMatrix, long animationDuration) {
        if (oldMatrix == null) {
            oldMatrix = new float[20];
            for (int i = 0; i < 20; i++) {
                if (i == 0 || i == 6 || i == 12 || i == 18) {
                    oldMatrix[i] = 1;
                } else {
                    oldMatrix[i] = 0;
                }
            }
        }
        if (animator != null) {
            animator.cancel();
        }
        animator = ValueAnimator.ofFloat(0F, 1F).setDuration(animationDuration);

        final float[] finalOldMatrix = oldMatrix;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float[] result = new float[20];
                float fraction = valueAnimator.getAnimatedFraction();
                for (int i = 0; i < 20; i++) {
                    result[i] = (finalOldMatrix[i] * (1 - fraction)) + (newMatrix[i] * fraction);
                }
                setFloatMatrix(result);
                StyleImageView.this.oldMatrix = result;
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setFloatMatrix(newMatrix);
                StyleImageView.this.oldMatrix = newMatrix;
            }
        });
        animator.start();
    }


    public void clearStyle() {
        clearColorFilter();
        oldMatrix = null;
    }

    public void clearStyle(long animationDuration) {
        if (oldMatrix == null) {
            return;
        }

        final float[] clearMatrix = new float[20];
        for (int i = 0; i < 20; i++) {
            if (i == 0 || i == 6 || i == 12 || i == 18) {
                clearMatrix[i] = 1;
            } else {
                clearMatrix[i] = 0;
            }
        }

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
                    result[i] = (oldMatrix[i] * (1 - fraction)) + (clearMatrix[i] * fraction);
                }
                setFloatMatrix(result);
                oldMatrix = result;
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clearStyle();
            }
        });
        animator.start();
    }*/
}
