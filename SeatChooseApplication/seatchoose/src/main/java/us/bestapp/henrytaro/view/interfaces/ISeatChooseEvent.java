package us.bestapp.henrytaro.view.interfaces;

import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;

/**
 * Created by lenovo on 2015/8/24.
 * <p>选择座位触发的事件,包括当前选中的座位,选中成功/失败的状态等</p>
 */
public interface ISeatChooseEvent {
    /**
     * 选座失败,可能选中在座位上但该座位不存在或不可见
     */
    public static String FAIL_IN_CHOOSE_SEAT = "fail_in_choose_seat";
    /**
     * 单击列表失败,没有单击在有效区域中
     */
    public static String FAIL_IN_CLICK_MAP = "fail_in_click_map";

    /**
     * 座位选中成功回调
     *
     * @param rowInMap    座位在列表中的行索引,<font color="@ff9900"><b>此处的行列值为map中的索引,不是实际该座位的行列号</b></font>
     * @param columnInMap 座位在列表中的列索引,<font color="@ff9900"><b>此处的行列值为map中的索引,不是实际该座位的行列号</b></font>
     * @param seatEntity  座位接口
     */
    public void clickSeatSuccess(int rowInMap, int columnInMap, AbsSeatEntity seatEntity);

    /**
     * 座位选中成功回调,<font color="ff9900"><b>此方法只有在实际座位被单击选中的情况下才会被调用</b></font>,
     * 此方法被调用时{@link #clickSeatSuccess(int, int, AbsSeatEntity)} 必定会被回调<br/>
     *
     * @param rowNumber    座位<font color="@ff9900"><b>实际行号</b></font>
     * @param columnNumber 座位<font color="@ff9900"><b>实际列行号</b></font>
     * @param isChoosen    <font color="@ff9900"><b>座位被选中后的状态改变,</b></font>true为该座位当前已被选中,false为该座位当前未被选中(或被取消选中)
     */
    public void selectedSeatSuccess(int rowNumber, int columnNumber, boolean isChoosen);

    /**
     * 选中情侣座,此方法被调用时{@link #clickSeatSuccess(int, int, AbsSeatEntity)} 必定会被回调,<font color="ff9900"><b>此方法只有在实际座位被单击选中的情况下才会被调用</b></font>,
     *
     * @param rowIndexFirst     被选中的第一个情侣座map中的行索引
     * @param columnIndexFirst  被选中的第一个情侣座在map中的列索引
     * @param rowIndexSecond    被选中的第二个情侣座map中的行索引
     * @param columnIndexSecond 被选中的第二个情侣座在map中的列索引
     * @param coupleSeats       情侣座位列表
     */
    public void selectedCoupleSuccess(int rowIndexFirst, int columnIndexFirst, int rowIndexSecond, int columnIndexSecond, AbsSeatEntity[] coupleSeats);

    /**
     * 选择情侣座失败
     *
     * @param rowIndexOriginal    原始选中的座位
     * @param columnIndexOriginal 原始选中的座位
     * @param rowIndexCouple      基于原始选中座位计算出来的情侣座位置行索引
     * @param columnIndexCouple   基于原始选中座位计算出来的情侣座位置列索引
     */
    public void selectedCoupleFail(int rowIndexOriginal, int columnIndexOriginal, int rowIndexCouple, int columnIndexCouple);

    /**
     * 当前选座失败，可能是没有选中座位，可能是选中位置的座位出错,或者单击的位置在空白区域,不能选中有效的实际座位
     */
    public void seletedFail(String failMsg);

    /**
     * 当前选中座位数已达到上限,设置选座上限请通过{@link ISeatViewInterface#setMostSeletedCount(int)}
     */
    public void seletedFull(boolean isCoupleSeat);

    /**
     * 缩放到极限值(最大或最小值)
     */
    public void scaleMaximum();
}
