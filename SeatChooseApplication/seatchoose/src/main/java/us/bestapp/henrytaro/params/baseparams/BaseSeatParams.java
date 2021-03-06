package us.bestapp.henrytaro.params.baseparams;

import android.graphics.Color;

import us.bestapp.henrytaro.params.interfaces.ISeatParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuhaolin on 15/9/10.<br/>
 * 座位绘制参数基本类,该类继承了{@link AbsBaseParams}及对外公开接口{@link ISeatParams},此接口所有的方法用于对外公开提供给用户调用;
 * 其余此类中的 {@code public} 方法均是用于提供给绘制时使用的方法,需要自定义参数类时请继承此方法重写部分方法即可
 * <br/>
 * <font color="#ff9900"><b>需要自定义类型时,请务必考虑是否需要清除原有的样式或者添加新的样式</b></font>
 * <br/>
 * <br/>
 * <font color="#ff9900"><b>关于标签及样式的设定,默认情况下{@link us.bestapp.henrytaro.params.baseparams.BaseSeatParams}预存了4个样式,
 * 其中包括<br/>
 * {@link #DRAW_STYLE_OPTIONAL_SEAT} 可选标签样式<br/>
 * {@link #DRAW_STYLE_SELECTED_SEAT} 已选标签样式<br/>
 * {@link #DRAW_STYLE_LOCK_SEAT} 锁定标签样式<br/>
 * {@link #DRAW_STYLE_COUPLE_OPTIONAL_SEAT} 情侣标签样式<br/></b></font>
 * <br/>
 * 其余的样式并没有预存,但是一样可以使用{@link #DRAW_STYLE_ERROR_SEAT}/{@link #DRAW_STYLE_UNSHOW_SEAT}两个标签,因为这两个样式默认是不进行绘制的,
 * 获取某个标签对象的样式若不存在的情况下,并不会做任何的绘制,所以需要绘制的样式请确保提供的样式及标签是有效的<br/>
 */
public class BaseSeatParams extends AbsBaseParams implements ISeatParams {

    /**
     * 默认座位颜色值
     */
    public static final int DEFAULT_SEAT_COLOR = Color.WHITE;
    /**
     * 默认座位宽度
     */
    public static final float DEFAULT_SEAT_WIDTH = 50f;
    /**
     * 默认座位高度
     */
    public static final float DEFAULT_SEAT_HEIGHT = 50f;
    /**
     * 默认座位列表中座位间的水平间隔宽度
     */
    public static final float DEFAULT_SEAT_HORIZONTAL_INTERVAL = DEFAULT_SEAT_WIDTH * 0.1f;
    /**
     * 默认座位列表中座位间的垂直间隔宽度
     */
    public static final float DEFAULT_SEAT_VERTICAL_INTERVAL = DEFAULT_SEAT_HEIGHT * 0.8f;
    /**
     * 默认座位类型与其描述文字的间隔宽度
     */
    public static final float DEFAULT_SEAT_TEXT_INTERVAL = 10f;
    /**
     * 默认每个座位类型之前的间隔(此处的座位指包含了其描述文字的全部整体)
     */
    public static final float DEFAULT_SEAT_TYPE_INTERVAL = 50f;
    /**
     * 默认座位圆角度
     */
    public static final float DEFAULT_SEAT_RADIUS = DEFAULT_SEAT_HEIGHT * 0.1f;

    //座位间水平间隔
    protected float mSeatHorizontalInterval = DEFAULT_SEAT_HORIZONTAL_INTERVAL;
    //座位间垂直间隔
    protected float mSeatVerticalInterval = DEFAULT_SEAT_VERTICAL_INTERVAL;
    //座位与描述文字之间的间隔
    protected float mDrawStyleDescInterval = DEFAULT_SEAT_TEXT_INTERVAL;
    //座位类型之间的间隔
    protected float mDrawStyleInterval = DEFAULT_SEAT_TYPE_INTERVAL;
    //绘制类型表格,每一种类型都对应唯一的一个类型对象及TAG
    private Map<String, BaseDrawStyle> mDrawStyles = null;

    //是否按指定顺序绘制座位类型
    protected boolean mIsDrawSampleStyleInOrder = false;
    //座位类型指定顺序
    protected List<String> mTagInOrder = null;
    //是否绘制座位类型
    protected boolean mIsDrawSampleStyle = true;

    protected float mDescriptionSize = DEFAULT_DESCRIPTION_SIZE;
    //移动缩放时用于暂时存放缩放前的数据(以便于正常使用比例缩放)
    protected float[] mValueHolder = null;
    //用于保存最原始的数据
    protected OriginalValuesHolder mOriginalHolder = null;

    /**
     * 创建并初始化参数
     */
    public BaseSeatParams() {
        this(DEFAULT_SEAT_WIDTH, DEFAULT_SEAT_HEIGHT, DEFAULT_SEAT_RADIUS, DEFAULT_SEAT_COLOR, DEFAULT_SEAT_LARGE_SCALE, DEFAULT_SEAT_SMALL_SCALE);
    }

    /**
     * 创建并初始化参数
     *
     * @param defaultWidth  默认宽度
     * @param defaultHeight 默认高度
     * @param defaultRadius 默认圆角弧度
     * @param defaultColor  默认颜色
     */
    public BaseSeatParams(float defaultWidth, float defaultHeight, float defaultRadius, int defaultColor, float largeScale, float smallScale) {
        super(defaultWidth, defaultHeight, defaultRadius, defaultColor, largeScale, smallScale);
        initial();
    }

    /**
     * 初始化
     */
    protected void initial() {
        int selectColor = Color.rgb(228, 24, 99);
        int optionalColor = Color.WHITE;
        int lockColor = Color.rgb(196, 195, 196);
        int coupleColor = Color.rgb(243, 115, 162);

        mDrawStyles = new HashMap<>();
        BaseDrawStyle selectInfo = new BaseDrawStyle(DRAW_STYLE_SELECTED_SEAT, true, selectColor, selectColor, Color.BLACK, "已选", DEFAULT_INT, null);
        BaseDrawStyle optionalInfo = new BaseDrawStyle(DRAW_STYLE_OPTIONAL_SEAT, true, optionalColor, optionalColor, Color.BLACK, "可选", DEFAULT_INT, null);
        BaseDrawStyle lockInfo = new BaseDrawStyle(DRAW_STYLE_LOCK_SEAT, true, lockColor, lockColor, Color.BLACK, "已售", DEFAULT_INT, null);
        BaseDrawStyle coupleInfo = new BaseDrawStyle(DRAW_STYLE_COUPLE_OPTIONAL_SEAT, true, coupleColor, coupleColor, Color.BLACK, "情侣", DEFAULT_INT, null);

        mDrawStyles.put(DRAW_STYLE_SELECTED_SEAT, selectInfo);
        mDrawStyles.put(DRAW_STYLE_OPTIONAL_SEAT, optionalInfo);
        mDrawStyles.put(DRAW_STYLE_LOCK_SEAT, lockInfo);
        mDrawStyles.put(DRAW_STYLE_COUPLE_OPTIONAL_SEAT, coupleInfo);
    }

    /**
     * <font color="#ff9900"><b>此方法不对用户开放使用,设置参数请忽略此方法</b></font><br/>
     * 设置缩放比例,缩放比是相对开始缩放前数据的缩放;<font color="#ff9900"><b>且缩放的大小有限制,当缩放的字体超过800时不允许继续缩放.因为此时会造成系统无法缓存文字</b></font>
     *
     * @param scaleRate      新的缩放比
     * @param isTrueSetValue 是否将此次缩放结果记录为永久结果
     */
    @Override
    public void setScaleRate(float scaleRate, boolean isTrueSetValue) {
        super.setScaleRate(scaleRate, isTrueSetValue);
        //创建缓存数据对象
        if (mValueHolder == null) {
            mValueHolder = new float[4];
            //第一次更新数据记录下最原始的数据
            mValueHolder[0] = this.mSeatVerticalInterval;
            mValueHolder[1] = this.mDrawStyleDescInterval;
            mValueHolder[2] = this.mDrawStyleInterval;
            mValueHolder[3] = this.mDescriptionSize;
        }
        //每一次变化都处理为相对原始数据的变化
        this.mSeatVerticalInterval = mValueHolder[0] * scaleRate;
        this.mDrawStyleDescInterval = mValueHolder[1] * scaleRate;
        this.mDrawStyleInterval = mValueHolder[2] * scaleRate;
        this.mDescriptionSize = mValueHolder[3] * scaleRate;

        //若确认更新数据,则将变化后的数据作为永久性数据进行缓存
        if (isTrueSetValue) {
            mValueHolder[0] = this.mSeatVerticalInterval;
            mValueHolder[1] = this.mDrawStyleDescInterval;
            mValueHolder[2] = this.mDrawStyleInterval;
            mValueHolder[3] = this.mDescriptionSize;
        }
    }


    @Override
    public float getSeatHorizontalInterval() {
        if (this.isDrawThumbnail()) {
            return mSeatHorizontalInterval * this.getThumbnailRate();
        } else {
            return mSeatHorizontalInterval;
        }
    }

    @Override
    public void setSeatHorizontalInterval(float mSeatHorizontalInterval) {
        if (mSeatHorizontalInterval == DEFAULT_FLOAT) {
            this.mSeatHorizontalInterval = DEFAULT_SEAT_HORIZONTAL_INTERVAL;
        } else {
            this.mSeatHorizontalInterval = mSeatHorizontalInterval;
        }
    }

    @Override
    public float getSeatVerticalInterval() {
        if (this.isDrawThumbnail()) {
            return mSeatVerticalInterval * this.getThumbnailRate();
        } else {
            return mSeatVerticalInterval;
        }
    }

    @Override
    public void setSeatVerticalInterval(float mSeatVerticalInterval) {
        if (mSeatVerticalInterval == DEFAULT_FLOAT) {
            this.mSeatVerticalInterval = DEFAULT_SEAT_VERTICAL_INTERVAL;
        } else {
            this.mSeatVerticalInterval = mSeatVerticalInterval;
        }
    }

    @Override
    public float getDrawStyleDescInterval() {
        if (this.isDrawThumbnail()) {
            return mDrawStyleDescInterval * this.getThumbnailRate();
        } else {
            return mDrawStyleDescInterval;
        }
    }

    @Override
    public float getDrawStyleInterval() {
        if (this.isDrawThumbnail()) {
            return mDrawStyleInterval * this.getThumbnailRate();
        } else {
            return mDrawStyleInterval;
        }
    }

    @Override
    public void setDrawStyleInterval(float drawStyleInterval) {
        if (drawStyleInterval == DEFAULT_FLOAT) {
            this.mDrawStyleInterval = DEFAULT_SEAT_TYPE_INTERVAL;
        } else {
            this.mDrawStyleInterval = drawStyleInterval;
        }
    }

    /**
     * 获取当前绘制的座位的颜色,<font color="#ff9900"><b>注意此处是当前绘制的座位的颜色,该颜色值只用于当前绘制的座位,此座位包括了普通座位及绘制座位类型时的示例座位</b></font><br/>
     * <p>若使用默认的座位绘制方式,则应该保证在每次座位绘制之前设置该值,否则可能会使后面大量的座位使用同一个颜色值</p>
     *
     * @return
     */
    @Override
    public int getColor() {
        return super.getColor();
    }

    /**
     * 设置绘制时使用的座位颜色，<font color="#ff9900"><b>该颜色并没有特别的意义，但绘制时使用的颜色必定是此颜色</b></font><br/>
     * <p><font color="#ff9900"><b>在任何一次绘制座位之前都必须考虑是否需要调用此方法，否则绘制使用的颜色将是上一次绘制使用的颜色</b></font></p>
     *
     * @param seatColor
     */
    @Override
    public void setColor(int seatColor) {
        super.setColor(seatColor);
    }

    @Override
    protected void updateWidthAndHeightWhenSet(float newWidth, float newHeight) {
        //TODO:更新宽高的某些计算,在此基类中并不需要特别计算,所以实现方法为空.
        //TODO:若子类需要自定义绘制图案等则需要考虑是否要基于宽高的某些计算,可参照 (SeatParams}
    }


    @Override
    public int getDrawStyleLength() {
        return mDrawStyles.size();
    }

    @Override
    public void setIsDrawSampleStyle(boolean isDraw) {
        this.mIsDrawSampleStyle = isDraw;
    }

    @Override
    public boolean isDrawDrawStyle() {
        return this.mIsDrawSampleStyle;
    }

    @Override
    public void setDrawStyleDescInterval(float mSeatTextInterval) {
        if (mSeatTextInterval == DEFAULT_FLOAT) {
            this.mDrawStyleDescInterval = DEFAULT_SEAT_TEXT_INTERVAL;
        } else {
            this.mDrawStyleDescInterval = mSeatTextInterval;
        }
    }

    @Override
    public List<String> getDrawStyleTags() {
        List<String> tagList = new ArrayList<>();
        for (String tag : mDrawStyles.keySet()) {
            tagList.add(tag);
        }
        return tagList;
    }

    @Override
    public BaseDrawStyle getDrawStyle(String typeTag) {
        return mDrawStyles.get(typeTag);
    }

    @Override
    public BaseDrawStyle addNewDrawStyle(String typeTag, BaseDrawStyle newStyle) {
        return mDrawStyles.put(typeTag, newStyle);
    }

    @Override
    public BaseDrawStyle removeDrawStyle(String typeTag) {
        return mDrawStyles.remove(typeTag);
    }

    @Override
    public void clearDrawStyles() {
        if (mDrawStyles != null) {
            mDrawStyles.clear();
        }
        if (mTagInOrder != null) {
            mTagInOrder.clear();
        }
    }

    @Override
    public void setIsDrawStyleByOrder(boolean isInOrder, List<String> tagInOrder) {
        this.mIsDrawSampleStyleInOrder = isInOrder;
        if (isInOrder) {
            this.mTagInOrder = tagInOrder;
        } else {
            this.mTagInOrder = null;
        }
    }

    @Override
    public boolean isDrawStyleByOrder() {
        return this.mIsDrawSampleStyleInOrder;
    }

    @Override
    public List<BaseDrawStyle> getDrawStyles(boolean isInOrder) {
        if (isInOrder) {
            if (this.mIsDrawSampleStyleInOrder && this.mTagInOrder != null) {
                List<BaseDrawStyle> orderStyleList = new ArrayList<>();
                for (String tag : mTagInOrder) {
                    orderStyleList.add(mDrawStyles.get(tag));
                }

                return orderStyleList;
            } else {
                return null;
            }
        } else {
            List<BaseDrawStyle> orderStyleList = new ArrayList<>();
            orderStyleList.addAll(mDrawStyles.values());
            return orderStyleList;
        }
    }

    /**
     * 获取座位类型样式的所有文本
     *
     * @param isInOrder 是否按顺序加载,若使用此功能必须是先设定{@link #setIsDrawStyleByOrder(boolean, List)}
     * @return
     */
    public List<String> getDrawStyleDescription(boolean isInOrder) {
        if (isInOrder) {
            if (mIsDrawSampleStyleInOrder && mTagInOrder != null) {
                List<String> typeDescList = new ArrayList<>();
                for (String tag : mTagInOrder) {
                    typeDescList.add(mDrawStyles.get(tag).description);
                }
                return typeDescList;
            } else {
                return null;
            }
        } else {
            List<String> typeDescList = new ArrayList<>();
            for (BaseDrawStyle style : mDrawStyles.values()) {
                typeDescList.add(style.description);
            }
            return typeDescList;
        }
    }


    /**
     * 获取自动计算并分离的seatParams,用于座位类型的分批绘制
     *
     * @param seatTypeRowCount 座位类型绘制的行数
     * @return
     */
    public BaseSeatParams[] getAutoSeparateParams(int seatTypeRowCount) {
        BaseSeatParams[] seatTypeParams = null;
        //座位类型个数
        int seatTypeLength = this.getDrawStyleLength();
        //每行个数
        int eachRowCount = seatTypeLength / seatTypeRowCount;
        //判断特别情况,当前座位类型个数不能分离成指定行数(行数远超过座位类型个数)
        //取消分离情况,按一行绘制
        if (seatTypeRowCount <= 0) {
            seatTypeParams = new BaseSeatParams[1];
            //创建当前变量类型的对象
            seatTypeParams[0] = this.getSelectableClone(this.mDrawStyles);
        } else {
            //正常分行
            //创建分离的行数的参数对象
            seatTypeParams = new BaseSeatParams[seatTypeRowCount];
            List<String> originalTagList = null;
            if (mIsDrawSampleStyleInOrder) {
                originalTagList = mTagInOrder;
            } else {
                originalTagList = new ArrayList<>();
                originalTagList.addAll(this.mDrawStyles.keySet());
            }

            for (int i = 0; i < seatTypeRowCount; i++) {
                Map<String, BaseDrawStyle> newStyleMap = new HashMap<>();
                //计算当前行需要处理的座位类型个数
                //当最后一行时,座位个数不是按计算出来每行个数进行处理
                //而是所有的座位数减去前面N行已处理的座位类型个数,剩下的就是最后一行的座位类型个数
                //此部分保证了奇偶情况座位类型都可以正常分配完毕
                int seatTypeCount = (i + 1) == seatTypeRowCount ? (seatTypeLength - i * eachRowCount) : eachRowCount;
                for (int j = 0; j < seatTypeCount; j++) {
                    String tag = originalTagList.get(i * eachRowCount + j);
                    newStyleMap.put(tag, mDrawStyles.get(tag));
                }

                seatTypeParams[i] = this.getSelectableClone(newStyleMap);
            }
        }
        return seatTypeParams;
    }

    /**
     * 根据参数部分性复制当前对象.部分复制对象像,会跟据参数进行复制部分的 tagInOrder 列表,因为此列表与 map 是相关联的
     *
     * @param styleMap 需要复制的map,仅此map对象的数据会被复制,其它的不会.若需要复制完整的对象则将原对象的map引用作为参数即可
     * @return
     */
    protected BaseSeatParams getSelectableClone(Map<String, BaseDrawStyle> styleMap) {
        BaseSeatParams params = (BaseSeatParams) super.clone();
        //深度复制保存的值对象
        if (params.mOriginalHolder != null) {
            params.mOriginalHolder = this.mOriginalHolder.clone();
        }
        if (params.mValueHolder != null) {
            params.mValueHolder = this.mValueHolder.clone();
        }
        //深度复制绘制类型
        if (styleMap != null) {
            HashMap<String, BaseDrawStyle> newMap = new HashMap<>(styleMap.size());
            for (Map.Entry<String, BaseDrawStyle> entry : styleMap.entrySet()) {
                newMap.put(entry.getKey(), entry.getValue().clone());
            }
            params.mDrawStyles = newMap;
        } else {
            params.mDrawStyles = new HashMap<>();
        }
        //深度复制顺序绘制标签
        if (params.mTagInOrder == null || styleMap == null) {
            params.mTagInOrder = null;
        } else if (styleMap == this.mDrawStyles) {
            //原对象完整复制
            List<String> newList = new ArrayList<>(params.mTagInOrder.size());
            for (String tag : params.mTagInOrder) {
                newList.add(tag);
            }
        } else {
            //部分复制
            List<String> newList = new ArrayList<>(params.mTagInOrder.size());
            for (int i = 0; i < params.mTagInOrder.size(); i++) {
                String tag = params.mTagInOrder.get(i);
                if (styleMap.containsKey(tag)) {
                    newList.add(tag);
                }
            }
            params.mTagInOrder = newList;
        }
        //更新width/height相关参数信息
        this.updateWidthAndHeightWhenSet(this.getWidthNotInThumbnail(), this.getHeightNotInThumbnail());
        return params;
    }

    @Override
    public float setScaleDefaultValuesToReplaceCurrents(float fixScaleRate) {
        float oldScaleRate = super.setScaleDefaultValuesToReplaceCurrents(fixScaleRate);
        if (mOriginalHolder != null && oldScaleRate != DEFAULT_FLOAT) {
            this.mSeatHorizontalInterval = mOriginalHolder.DEFAULT_HORI_INTERVAL * fixScaleRate;
            this.mSeatVerticalInterval = mOriginalHolder.DEFAULT_VERT_INTERVAL * fixScaleRate;
            this.mDrawStyleInterval = mOriginalHolder.DEFAULT_STYLE_INTERVAL * fixScaleRate;
            this.mDrawStyleDescInterval = mOriginalHolder.DEFAULT_DESC_INTERVAL * fixScaleRate;
            this.mDescriptionSize = mOriginalHolder.DEFAULT_DESC_SIZE * fixScaleRate;
        }
        return oldScaleRate;
    }


    @Override
    public void storeDefaultValues(DefaultValuesHolder holder) {
        if (mOriginalHolder == null) {
            mOriginalHolder = new OriginalValuesHolder();
        }
        mOriginalHolder.updateValues(holder);
        mOriginalHolder.DEFAULT_HORI_INTERVAL = this.getSeatHorizontalInterval();
        mOriginalHolder.DEFAULT_VERT_INTERVAL = this.getSeatVerticalInterval();
        mOriginalHolder.DEFAULT_STYLE_INTERVAL = this.getDrawStyleInterval();
        mOriginalHolder.DEFAULT_DESC_SIZE = this.getDescriptionSize(DEFAULT_FLOAT);
        mOriginalHolder.DEFAULT_DESC_INTERVAL = this.getDrawStyleDescInterval();
    }

    @Override
    public OriginalValuesHolder getDefaultValues() {
        return new OriginalValuesHolder(mOriginalHolder);
    }

    @Override
    public BaseSeatParams clone() {
        return getSelectableClone(this.mDrawStyles);
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        if (mDrawStyles != null) {
            for (BaseDrawStyle style : mDrawStyles.values()) {
                strBuilder.append("/***** style *******/\n");
                strBuilder.append(style.toString());
                strBuilder.append("=================================\n");
            }
        }
        return super.toString() +
                "style\t|\n" + strBuilder.toString();
    }

    /**
     * 原始座位数据的保存,此处的原始是指<font color="#ff9900"><b>座位默认设定的宽高或者用户设定的宽高,即第一次运行并显示出来的界面即为原始界面;
     * 当用户对宽高做修改时,也会重新记录此数据</b></font>
     */
    protected class OriginalValuesHolder extends DefaultValuesHolder implements Cloneable {
        public float DEFAULT_HORI_INTERVAL = 0f;
        public float DEFAULT_VERT_INTERVAL = 0f;
        public float DEFAULT_STYLE_INTERVAL = 0f;
        public float DEFAULT_DESC_INTERVAL = 0f;
        public float DEFAULT_DESC_SIZE = 0f;

        public OriginalValuesHolder() {
        }

        public OriginalValuesHolder(OriginalValuesHolder holder) {
            if (holder != null) {
                this.updateValues(holder.DEFAULT_WIDTH, holder.DEFAULT_HEIGHT, holder.DEFAULT_RADIUS, holder.DEFAULT_COLOR,
                        holder.DEFAULT_HORI_INTERVAL, holder.DEFAULT_VERT_INTERVAL, holder.DEFAULT_STYLE_INTERVAL, holder.DEFAULT_DESC_INTERVAL, holder.DEFAULT_DESC_SIZE);
            }
        }

        public void updateValues(float width, float height, float radius, int color,
                                 float horizontalInterval, float verticalInterval, float typeInterval, float descInterval, float descSize) {
            super.updateValues(width, height, radius, color);
            this.DEFAULT_HORI_INTERVAL = horizontalInterval;
            this.DEFAULT_VERT_INTERVAL = verticalInterval;
            this.DEFAULT_STYLE_INTERVAL = typeInterval;
            this.DEFAULT_DESC_INTERVAL = descInterval;
            this.DEFAULT_DESC_SIZE = descSize;
        }

        @Override
        public OriginalValuesHolder clone() {
            return (OriginalValuesHolder) super.clone();
        }
    }

}
