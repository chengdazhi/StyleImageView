# StyleImageView

## Introduction

![gif_sample](./images/style_sample.gif)

[中文见这里](https://github.com/chengdazhi/StyleImageView/wiki/%E4%B8%AD%E6%96%87%E8%AF%B4%E6%98%8E%E9%A1%B5)

This library can set the style of ImageView/View background/Drawable/Bitmap and also brightness & contrast. This library now supports 10 different styles, you can check them out below. You can enable animation, and choose to set Interpolators and Listeners. You can acquire the Bitmap of ImageView/View/Drawable after style is set.

If you only need to operate ImageViews, you can use the StyleImageView class. You can specify attributes from layout file. If you are already using custom ImageViews, or you want to operate on View's background, or any Drawable or Bitmap, you can use the Styler class.

**Note that this library uses ColorFilter to achieve effects. If you are using ColorFilter in your project, this may cause conflicts.**


## Try Sample

You can go to [chengdazhi.com/styleimageview](http://chengdazhi.com/styleimageview) to download sample APK file or scan this QR code:

![qr](./images/style_image_view_qr.png)

## Feature

StyleImageView supports 10 styles as follow. Note that you can set brightness and contrast on top of them. Example of Saturation mode is not listed, since this mode needs a saturation input.

| Modes           | Sea Shore | Boat and Houses |
| --------------- | --------- | --------------- |
| Original        | ![sea_original](./images/sea_original.png) | ![boat_original](./images/boat_original.png) |
| Grey Scale      | ![sea_grey_scale](./images/sea_grey_scale.png) | ![boat_grey_scale](./images/boat_grey_scale.png) |
| Invert          | ![sea_invert](./images/sea_invert.png) | ![boat_invert](./images/boat_invert.png) |
| RGB to BGR      | ![sea_rgb_to_bgr](./images/sea_rgb_to_bgr.png) | ![boat_rgb_to_bgr](./images/boat_rgb_to_bgr.png) |
| Sepia           | ![sea_sepia](./images/sea_sepia.png) | ![boat_sepia](./images/boat_sepia.png) |
| Black & White   | ![sea_black_and_white](./images/sea_black_and_white.png) | ![boat_black_and_white](./images/boat_black_and_white.png) |
| Bright          | ![sea_bright](./images/sea_bright.png) | ![boat_bright](./images/boat_bright.png) |
| Vintage Pinhole | ![sea_vintage_pinhole](./images/sea_vintage_pinhole.png) | ![boat_vintage_pinhole](./images/boat_vintage_pinhole.png) |
| Kodachrome      | ![sea_kodachrome](./images/sea_kodachrome.png) | ![boat_kodachrome](./images/boat_kodachrome.png) |
| Technicolor     | ![sea_technicolor](./images/sea_technicolor.png) | ![boat_technicolor](./images/boat_technicolor.png) |

Some combinations I recommend:

* Invert + (150 brightness) + (2.0F contrast)
* Sepia + (-50 brightness) + (2.0F contrast)
* Kodachrome + (-100 brightness) + (1.6F contrast)

**Try out the sample to find out more!**

## Import

Gradle

    dependencies {
        compile 'it.chengdazhi.styleimageview:styleimageview:1.0.3'
    }

Maven

    <dependency>
        <groupId>it.chengdazhi.styleimageview</groupId>
        <artifactId>styleimageview</artifactId>
        <version>1.0.3</version>
        <type>pom</type>
    </dependency>

## Usage

See [Wiki](https://github.com/chengdazhi/StyleImageView/wiki) or [中文版](https://github.com/chengdazhi/StyleImageView/wiki/%E4%B8%AD%E6%96%87%E8%AF%B4%E6%98%8E%E9%A1%B5)

## License

    Copyright 2016 chengdazhi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
