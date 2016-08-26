# StyleImageView

## Introduction

![gif_sample](./images/style_sample.gif)

This library can set the style of ImageView/View background/Drawable/Bitmap and also brightness & contrast. This library now supports 10 different styles, you can check them out below. You can enable animation, and choose to set Interpolators and Listeners. You can acquire the Bitmap of ImageView/View/Drawable after style is set.

If you only need to operate ImageViews, you can use the StyleImageView class. You can specify attributes from layout file. If you are already using custom ImageViews, or you want to operate on View's background, or any Drawable or Bitmap, you can use the Styler class.

**Note that this library uses ColorFilter to achieve effects. If you are using ColorFilter in your project, this may cause conflicts.**


## Try Sample


## Feature

StyleImageView supports 10 styles as follow. Note that you can set brightness and contrast on top of them. Example of Saturation mode is not listed, since this mode needs a saturation input.

| Modes           | Sea Shore | Boat and Houses |
| --------------- | --------- | --------------- |
| Original        | ![sea_original](./images/sea_original.png =300x150) | ![boat_original](./images/boat_original.png) |
| Grey Scale      | ![sea_grey_scale](./images/sea_grey_scale.png) | ![boat_grey_scale](./images/boat_grey_scale.png) |
| Invert          | ![sea_invert](./images/sea_invert.png) | ![boat_invert](./images/boat_invert.png) |
| RGB to BGR      | ![sea_rgb_to_bgr](./images/sea_rgb_to_bgr.png) | ![boat_rgb_to_bgr](./images/boat_rgb_to_bgr.png) |