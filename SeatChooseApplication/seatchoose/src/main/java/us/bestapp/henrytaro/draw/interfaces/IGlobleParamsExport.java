package us.bestapp.henrytaro.draw.interfaces;

import android.graphics.Color;

/**
 * Created by xuhaolin on 15/8/25.
 * 全局设置参数接口,此接口内的方法是独立且涉及全局数据的设置的,是独立对外开放的接口
 * <p>此接口与{@link IBaseParamsExport}不同,此接口为设置全局性的参数,而{@link IBaseParamsExport}是设置具体参数类型(座位/舞台)对应的相同的参数(宽/高等)</p>
 */
public interface IGlobleParamsExport {

    /**
     * 设置是否绘制缩略图
     *
     * @param isDraw
     */
    public void setIsDrawThumbnail(boolean isDraw);

    /**
     * 设置是否保持缩略图的显示，若设为false，则缩略图只在拖动界面或者是缩放时显示，当界面静止是不显示缩略图
     *
     * @param isShowAlways
     */
    public void setIsShowThumbnailAlways(boolean isShowAlways);

    /**
     * 设置缩略图背景色及透明度
     *
     * @param color 颜色值,颜色值不作任何检测(颜色默认值为{@link Color#BLACK})
     * @param alpha 透明度,透明度必须在0-255之间,用默认值请用参数{@link IBaseParamsExport#DEFAULT_INT}
     * @return
     */
    public boolean setThumbnailBackgroundColorWithAlpha(int color, int alpha);


    /**
     * 设置缩略图的相对当前view界面显示的比例,此处只以Width为标准计算比例,height的高度是跟随改变的
     *
     * @param widthRate 宽比例,一般为0-1之间,缩略图宽/view宽,默认值为1/3;
     */
    public void setThumbnailWidthRate(float widthRate);

    /**
     * 设置背景色
     *
     * @param bgColor
     */
    public void setCanvasBackgroundColor(int bgColor);

    /**
     * 获取背景色
     *
     * @return
     */
    public int getCanvasBackgroundColor();

    /**
     * 获取缩略图背景色透明度
     *
     * @return
     */
    public int getThumbnailBgAlpha();

    /**
     * 获取缩略图背景色
     *
     * @return
     */
    public int getThumbnailBackgroundColor();

    /**
     * 获取缩略图显示的宽比
     *
     * @return
     */
    public float getThumbnailWidthRate();

    /**
     * 获取是否绘制缩略图
     *
     * @return
     */
    public boolean getIsDrawThumbnail();


    /**
     * 获取是否保持显示缩略图
     *
     * @return
     */
    public boolean getIsShowThumbnailAlways();

    /**
     * 设置座位类型需要自动绘制成几行
     * <p>分行绘制的规则如下:座位类型总数/rowCount,若整除则每行为相同的座位类型个数,
     * 若有余则最后一行比其它行将多一个座位类型</p>
     *
     * @param rowCount 此参数值必须大于0,否则将置为默认值1
     */
    public void setSeatTypeRowCount(int rowCount);

    /**
     * 获取座位类型绘制的行数
     *
     * @return
     */
    public int getSeatTypeRowCount();

    /**
     * 是否绘制行号
     *
     * @param isDrawRowNumber
     */
    public void setIsDrawRowNumber(boolean isDrawRowNumber);

    /**
     * 获取是否绘制行号
     */
    public boolean getIsDrawRowNumber();

    /**
     * 是否绘制列号
     *
     * @param isDrawColumnNumber
     */
    public void setIsDrawColumnNumber(boolean isDrawColumnNumber);

    /**
     * 获取是否绘制列号
     */
    public boolean getIsDrawColumnNumber();

    /**
     * 设置是否显示选中某行某列时的提醒
     *
     * @param isDrawNotification
     */
    public void setIsDrawSeletedRowColumnNotification(boolean isDrawNotification);

    /**
     * 获取是否绘制选中某行某列提醒
     *
     * @return
     */
    public boolean getIsDrawSeletedRowColumnNotification();

}