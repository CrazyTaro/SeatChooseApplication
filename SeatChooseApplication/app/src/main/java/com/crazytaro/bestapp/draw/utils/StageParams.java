package com.crazytaro.bestapp.draw.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by xuhaolin on 2015/8/9.
 * <p>舞台参数，包括舞台绘制需要的所有参数</p>
 */
public class StageParams extends BaseParams {
    /**
     * 默认舞台颜色
     */
    public static final int DEFAULT_STAGE_COLOR = Color.GREEN;
    /**
     * 默认舞台宽度
     */
    public static final float DEFAULT_STAGE_WIDTH = 350f;
    /**
     * 默认舞台高度
     */
    public static final float DEFAULT_STAGE_HEIGHT = 55f;
    /**
     * 默认舞台圆角度
     */
    public static final float DEFAULT_STAGE_RADIUS = 10f;
    /**
     * 默认舞台与顶端间距
     */
    public static final float DEFAULT_STAGE_MARGIN_TOP = 30f;
    /**
     * 默认舞台下方空白高度（与座位的间隔）
     */
    public static final float DEFAULT_STAGE_MARGIN_BOTTOM = 30f;
    /**
     * 默认舞台文字
     */
    public static final String DEFAULT_STAGE_TEXT = "舞台";
    private float mStageMarginTop = DEFAULT_STAGE_MARGIN_TOP;
    private float mStageMarginBottom = DEFAULT_STAGE_MARGIN_BOTTOM;
    private String mStageText = DEFAULT_STAGE_TEXT;

    private float[] mDefaultEnlargeHolder = null;
    private float[] mDefaultReduceHolder = null;
    //用于缩放暂存舞台数据
    private float[] mValueHolder = null;
    //用于检测缩放时是否已暂存数据
    private boolean mIsValueHold = false;
    //默认资源ID
    private int mStageImageID = DEFAULT_INT;
    private Bitmap mStageImageBitmap = null;

    public StageParams() {
        super(DEFAULT_STAGE_WIDTH, DEFAULT_STAGE_HEIGHT, DEFAULT_STAGE_RADIUS, DEFAULT_STAGE_COLOR);
    }

    /**
     * 设置图片资源ID,该该法会默认将绘制方式设置为图片绘制方式,并且不检测资源ID的可用性,请尽可能保证ID可用
     *
     * @param imageID
     */
    public void setImage(int imageID) {
        this.mStageImageID = imageID;
        super.setDrawType(DRAW_TYPE_IMAGE);
    }

    /**
     * 设置图片资源,该方法会默认将绘制方式设置为图片绘制方式,参数可为null
     *
     * @param imageBitmap
     */
    public void setImage(Bitmap imageBitmap) {
        if (imageBitmap != null) {
            this.mStageImageBitmap = imageBitmap;
            super.setDrawType(DRAW_TYPE_IMAGE);
        } else {
            this.mStageImageBitmap = null;
        }
    }

    /**
     * stage中的imageID与bitmap（即图片资源ID与图片资源）是两个相互独立的部分，其中以资源ID为主（存在资源ID的情况下）。
     * 当设置了资源ID获取的图片资源一般是由该ID生成的图片资源，但是如果资源ID还未被加载，此时获取的图片资源可能为null（在绘制舞台时如果资源ID未被加载会自动加载）
     *
     * @return
     */
    public Bitmap getImage() {
        return mStageImageBitmap;
    }

    /**
     * 加载资源图片，当isReload为true时，即重新加载，以资源ID为主重新加载资源，此时不管bitmap是存为null都会重新加载；
     * 当isReload为false时，则存在bitmap优先返回bitmap，否则加载资源ID，两者都不存在抛出异常
     *
     * @param context
     * @param isReload 是否以资源ID重新加载数据
     */
    protected void loadStageImage(Context context, boolean isReload) {
        int[] imageID = new int[]{this.mStageImageID};
        Bitmap[] imageBitmap = new Bitmap[]{this.mStageImageBitmap};
        super.loadSeatImage(context, imageID, imageBitmap, (int) this.getWidth(), (int) this.getHeight(), isReload);
    }

    @Override
    /**
     * 设置缩放比例,缩放比是相对开始缩放前数据的缩放;<font color="yellow"><b>且缩放的大小有限制,当缩放的字体超过800时不允许继续缩放.因为此时会造成系统无法缓存文字</b></font>
     *
     * @param scaleRate 新的缩放比
     * @param isTrueSet 是否将此次缩放结果记录为永久结果
     */
    protected void setScaleRate(float scaleRate, boolean isTrueSet) {
        if (mValueHolder == null) {
            mValueHolder = new float[4];
        }
        if (!mIsValueHold) {
            mValueHolder[0] = this.mWidth;
            mValueHolder[1] = this.mHeight;
            mValueHolder[2] = this.mStageMarginTop;
            mValueHolder[3] = this.mStageMarginBottom;
            mIsValueHold = true;
        }
        this.mWidth = mValueHolder[0] * scaleRate;
        this.mHeight = mValueHolder[1] * scaleRate;
        this.mStageMarginTop = mValueHolder[2] * scaleRate;
        this.mStageMarginBottom = mValueHolder[3] * scaleRate;
        if (isTrueSet) {
            mValueHolder[0] = this.mWidth;
            mValueHolder[1] = this.mHeight;
            mValueHolder[2] = this.mStageMarginTop;
            mValueHolder[3] = this.mStageMarginBottom;
            mIsValueHold = false;
        }
    }

    public String getStageText() {
        return mStageText;
    }

    public void setStageText(String text) {
        this.mStageText = text;
    }

    public float getStageMarginTop() {
        if (super.getIsDrawThumbnail()) {
            return mStageMarginTop * super.getThumbnailRate();
        } else {
            return mStageMarginTop;
        }
    }

    /**
     * 设置舞台上方顶端的高度，使用默认值请用{@link #DEFAULT_FLOAT}
     *
     * @param mStageMarginTop
     */
    public void setStageMarginTop(float mStageMarginTop) {
        if (mStageMarginTop == DEFAULT_FLOAT) {
            this.mStageMarginTop = DEFAULT_STAGE_MARGIN_TOP;
        } else {
            this.mStageMarginTop = mStageMarginTop;
        }
    }

    public float getStageMarginBottom() {
        if (super.getIsDrawThumbnail()) {
            return mStageMarginBottom * super.getThumbnailRate();
        } else {
            return mStageMarginBottom;
        }
    }

    /**
     * 设置舞台与下方（座位）间隔的高度，，使用默认值请用{@link #DEFAULT_FLOAT}
     *
     * @param mStageMarginBottom
     */
    public void setStageMarginBottom(float mStageMarginBottom) {
        if (mStageMarginBottom == DEFAULT_FLOAT) {
            this.mStageMarginBottom = DEFAULT_STAGE_MARGIN_BOTTOM;
        } else {
            this.mStageMarginBottom = mStageMarginBottom;
        }
    }

    /**
     * 获取舞台占用的高度，包括舞台距顶端的高度+舞台实际高度+舞台与下方（座位）间隔高度
     *
     * @return 返回舞台占用的高度
     */
    protected float getStageTotalHeight() {
        return this.getHeight() + this.getStageMarginBottom() + this.getStageMarginTop();
    }

    protected void autoCalculateParams(float widthPrecent, float viewWidth) {
        if (widthPrecent == DEFAULT_FLOAT || widthPrecent < 0 || widthPrecent > 1) {
            widthPrecent = 0.3f;
        }
        mWidth = viewWidth * widthPrecent;
        mHeight = mWidth * 1 / 5;
    }

    protected float getScaleRateCompareToOriginal() {
        if (mDefaultReduceHolder != null) {
            return this.mWidth / mDefaultReduceHolder[0];
        } else {
            return DEFAULT_FLOAT;
        }
    }

    protected float setDefaultScaleValue(boolean isSetEnlarge) {
        float scaleRate = 0f;
        float[] defaultValues = null;
        if (isSetEnlarge) {
            defaultValues = mDefaultEnlargeHolder;
        } else {
            defaultValues = mDefaultReduceHolder;
        }
        scaleRate = this.mWidth / defaultValues[0];

        this.mWidth = defaultValues[0];
        this.mHeight = defaultValues[1];
        this.mStageMarginTop = defaultValues[2];
        this.mStageMarginBottom = defaultValues[3];

        return scaleRate;
    }

    @Override
    protected void storeDefaultScaleValue() {
        if (mDefaultEnlargeHolder == null) {
            mDefaultEnlargeHolder = new float[4];
        }
        if (mDefaultReduceHolder == null) {
            mDefaultReduceHolder = new float[4];
        }
        mDefaultEnlargeHolder[0] = this.mWidth * 3;
        mDefaultEnlargeHolder[1] = this.mHeight * 3;
        mDefaultEnlargeHolder[2] = this.mStageMarginTop * 3;
        mDefaultEnlargeHolder[3] = this.mStageMarginBottom * 3;

        mDefaultReduceHolder[0] = this.mWidth * 1;
        mDefaultReduceHolder[1] = this.mHeight * 1;
        mDefaultReduceHolder[2] = this.mStageMarginTop * 1;
        mDefaultReduceHolder[3] = this.mStageMarginBottom * 1;
    }

}
