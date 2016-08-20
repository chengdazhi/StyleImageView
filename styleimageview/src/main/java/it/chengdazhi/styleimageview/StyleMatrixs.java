package it.chengdazhi.styleimageview;

/**
 * Created by chengdazhi on 8/10/16.
 */
public class StyleMatrixs {

    private static final float[] COMMON = new float[] {
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0
    };

    public static final float[] common() {
        return COMMON.clone();
    }

    private static final float[] GREY_SCALE = new float[] {
            0.33F, 0.59F, 0.11F, 0, 0,
            0.33F, 0.59F, 0.11F, 0, 0,
            0.33F, 0.59F, 0.11F, 0, 0,
            0, 0, 0, 1, 0,
    };

    public static final float[] greyScale() {
        return GREY_SCALE.clone();
    }

    private static final float[] INVERT = new float[] {
            -1, 0, 0, 0, 255,
            0, -1, 0, 0, 255,
            0, 0, -1, 0, 255,
            0, 0, 0, 1, 0,
    };

    public static final float[] invert() {
        return INVERT.clone();
    }

    private static final float[] RGB_TO_BGR = new float[] {
            0, 0, 1, 0, 0,
            0, 1, 0, 0, 0,
            1, 0, 0, 0, 0,
            0, 0, 0, 1, 0,
    };

    public static final float[] rgbToBgr() {
        return RGB_TO_BGR.clone();
    }

    private static final float[] SEPIA = new float[] {
            0.393F, 0.769F, 0.189F, 0, 0,
            0.349F, 0.686F, 0.168F, 0, 0,
            0.272F, 0.534F, 0.131F, 0, 0,
            0, 0, 0, 1, 0,
    };

    public static final float[] sepia() {
        return SEPIA.clone();
    }

    private static final float[] BRIGHT = new float[] {
            1.438F, -0.122F, -0.016F, 0, 0,
            -0.062F, 1.378F, -0.016F, 0, 0,
            -0.062F, -0.122F, 1.483F, 0, 0,
            0, 0, 0, 1, 0,
    };

    public static final float[] bright() {
        return BRIGHT.clone();
    }

    private static final float[] BLACK_AND_WHITE = new float[] {
            1.5F, 1.5F, 1.5F, 0, -255,
            1.5F, 1.5F, 1.5F, 0, -255,
            1.5F, 1.5F, 1.5F, 0, -255,
            0, 0, 0, 1, 0,
    };

    public static final float[] blackAndWhite() {
        return BLACK_AND_WHITE.clone();
    }

    private static final float[] VINTAGE_PINHOLE = new float[] {
            0.6279345635605994F, 0.3202183420819367F, -0.03965408211312453F, 0, 9.651285835294123F,
            0.02578397704808868F, 0.6441188644374771F, 0.03259127616149294F, 0, 7.462829176470591F,
            0.0466055556782719F, -0.0851232987247891F, 0.5241648018700465F, 0, 5.159190588235296F,
            0, 0, 0, 1, 0
    };

    public static final float[] vintagePinhole() {
        return VINTAGE_PINHOLE.clone();
    }

    private static final float[] KODACHROME = new float[] {
            1.1285582396593525F, -0.3967382283601348F, -0.03992559172921793F, 0, 63.72958762196502F,
            -0.16404339962244616F, 1.0835251566291304F, -0.05498805115633132F, 0, 24.732407896706203F,
            -0.16786010706155763F, -0.5603416277695248F, 1.6014850761964943F, 0, 35.62982807460946F,
            0, 0, 0, 1, 0
    };

    public static final float[] kodachrome() {
        return KODACHROME.clone();
    }

    private static final float[] TECHNICOLOR = new float[] {
            1.9125277891456083F, -0.8545344976951645F, -0.09155508482755585F, 0, 11.793603434377337F,
            -0.3087833385928097F, 1.7658908555458428F, -0.10601743074722245F, 0, -70.35205161461398F,
            -0.231103377548616F, -0.7501899197440212F, 1.847597816108189F, 0, 30.950940869491138F,
            0, 0, 0, 1, 0
    };

    public static final float[] technicolor() {
        return TECHNICOLOR.clone();
    }

}
