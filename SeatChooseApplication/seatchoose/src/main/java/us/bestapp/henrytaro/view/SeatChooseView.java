package us.bestapp.henrytaro.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import us.bestapp.henrytaro.draw.interfaces.ISeatDrawInterface;
import us.bestapp.henrytaro.draw.interfaces.ISeatInformationListener;
import us.bestapp.henrytaro.draw.utils.SimpleDrawUtils;
import us.bestapp.henrytaro.entity.absentity.AbsMapEntity;
import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;
import us.bestapp.henrytaro.params.interfaces.ISeatParams;
import us.bestapp.henrytaro.view.interfaces.ISeatChooseEvent;
import us.bestapp.henrytaro.view.interfaces.ISeatViewInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuhaolin
 * @version 5.0
 *          <p/>
 *          created by xuhaolin at 2015/08/10
 *          <p>可以自定义view并使用已有的绘制参数类进行处理,会更加灵活</p>
 *          <p>使用自定义View时需要创建内部对象{@link SimpleDrawUtils},此类是处理所有绘制方法的重要类,必须使用该类才能完成绘制功能,
 *          同时需要重写view的onDraw事件,通过调用seatDrawUtil.drawCanvas()完成绘制.</p>
 *          <p>如果需要处理选座事件,请实现接口{@link ISeatInformationListener},
 *          并为seatDrawUtil设置该接口对应的监听事件</p>
 *          <br/>
 *          <p>不需要自定义view实现,仅使用此控件的话,请实现{@link ISeatChooseEvent}接口,以处理此控件事件处理后的回调</p>
 *          <p/>
 *          <br/>
 *          <br/>
 *          此view初始化情况下使用的参数值全部都是默认值及默认设置<br/>
 *          1.默认不绘制列数<br/>
 *          2.默认绘制缩略图并不保持显示<br/>
 *          3.默认绘制图形界面而不使用图片<br/>
 *          4.默认座位类型只有四种,已选,可选,已售,情侣<br/>
 *          5.默认缩略图座位颜色跟随界面座位颜色<br/>
 *          6.默认不提醒当前选中座位行列值<br/>
 *          7.默认可选座位最大值为5<br/>
 */
public class SeatChooseView extends View implements ISeatInformationListener, ISeatViewInterface {
    private ISeatDrawInterface mSeatDrawHandle = null;
    private ISeatChooseEvent mSeatChooseEvent = null;
    private ISeatParams mSeatParams = null;
    private int mMostSeletedCount = 5;
    private List<AbsSeatEntity> mCurrentSeletedSeats = null;
    private Context mContext = null;

    public SeatChooseView(Context context) {
        super(context);
        initial(context);
    }

    public SeatChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial(context);
    }

    //初始化
    private void initial(Context context) {
        mContext = context.getApplicationContext();
        //创建绘制对象
        mSeatDrawHandle = new SimpleDrawUtils(this.getContext(), this);
        mSeatParams = mSeatDrawHandle.getSeatParams();

        //不显示log
        mSeatDrawHandle.setIsShowLog(true, null);
        //设置监听事件
        mSeatDrawHandle.setSeatInformationListener(this);
        //创建选择座位的存储列表
        mCurrentSeletedSeats = new ArrayList<>(mMostSeletedCount);
    }


    @Override
    public boolean removeSeat(AbsSeatEntity seatEntity) {
        for (AbsSeatEntity seat : mCurrentSeletedSeats) {
            if (seat.equals(seatEntity)) {
                mCurrentSeletedSeats.remove(seat);
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean addSeat(AbsSeatEntity seatEntity) {
        //重复的座位将不添加
        for (AbsSeatEntity seat : mCurrentSeletedSeats) {
            if (seat.equals(seatEntity)) {
                return false;
            }
        }

        //空座位不添加
        if (seatEntity == null) {
            return false;
        }
        //对添加的座位进行判断是否在当前列表中
        int rowIndex = seatEntity.getX();
        int columnIndex = seatEntity.getY();
        AbsMapEntity map = mSeatDrawHandle.getSeatDrawMap();
        if (map != null && rowIndex < map.getRowCount() && columnIndex < map.getMaxColumnCount()) {
            mCurrentSeletedSeats.add(seatEntity);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mSeatDrawHandle.drawCanvas(canvas);
    }


    @Override
    public void seatStatus(int status) {
    }

    @Override
    public void chooseInMapSuccess(AbsSeatEntity seatEntity) {
    }


    @Override
    public void chosseInMapFail() {
        if (mSeatChooseEvent != null) {
            mSeatChooseEvent.seletedFail(false);
        }
    }

    @Override
    public void chooseSeatSuccess(AbsSeatEntity seatEntity) {
        //通知选中事件
        if (mSeatChooseEvent != null) {
            mSeatChooseEvent.clickSeatSuccess(seatEntity);
        }

        if (updateCoupleSeat(seatEntity)) {
            return;
        } else {
            updateSingleSeat(seatEntity);
        }
    }

    /**
     * 更新情侣座选座位,通过当前座位计算出对应的情侣座
     *
     * @param seatEntity 当前座位
     * @return
     */
    protected boolean updateCoupleSeat(AbsSeatEntity seatEntity) {
        if (seatEntity == null) {
            return false;
        }

        //是否被选中状态
        boolean isChoosen = false;
        boolean isHandle = false;
        int rowIndexInMap = seatEntity.getX();
        int columnIndexInMap = seatEntity.getY();

        //是否情侣座位
        if (seatEntity.isCouple()) {
            //匹配的情侣座位的列索引
            int coupleColumnIndex = 0;
            //当前情侣座是否从左到右的(即在左边的座位)
            //是则向右边计算,否则向左边计算,此方法应该仅在情侣座的情况下使用
            if (seatEntity.isCoupleLeftToRight()) {
                coupleColumnIndex = columnIndexInMap + 1;
            } else {
                coupleColumnIndex = columnIndexInMap - 1;
            }
            //获取计算到的匹配情侣座位
            AbsSeatEntity coupleSeat = mSeatDrawHandle.getSeatDrawMap().getSeatEntity(rowIndexInMap, coupleColumnIndex);
            if (coupleSeat == null) {
                //该座位不存在,,则返回选中座位失败
                isHandle = false;
                if (mSeatChooseEvent != null) {
                    mSeatChooseEvent.seletedFail(true);
                }
            } else {
                //是否处理了情侣座
                isHandle = true;
                //座位存在且座位的选中状态必须与当前选中座位的相同才行
                //因为情侣座是并联的,只能一起被选中或者不被选中
                if (seatEntity.isChosen() == coupleSeat.isChosen()) {
                    if (seatEntity.isChosen() > 0) {
                        //当前情侣座状态为已选
                        //更新状态为未选
                        mSeatDrawHandle.updateSeatInMap(-1, rowIndexInMap, columnIndexInMap);
                        mSeatDrawHandle.updateSeatInMap(-1, rowIndexInMap, coupleColumnIndex);
                        //尝试移除存放在选中列表中的座位
                        removeSeat(seatEntity);
                        removeSeat(coupleSeat);
                        //标志已处理选中事件
                        isChoosen = true;
                    } else if (seatEntity.isChosen() < 0) {
                        //当前情侣座状态为未选
                        //且当前保存的座位数小于最大座位数-2
                        //留下两个座位是用于保存情侣座
                        if ((mCurrentSeletedSeats.size() + 2) <= mMostSeletedCount) {
                            //更新座位为已选
                            mSeatDrawHandle.updateSeatInMap(1, rowIndexInMap, columnIndexInMap);
                            mSeatDrawHandle.updateSeatInMap(1, rowIndexInMap, coupleColumnIndex);
                            //将座位添加到选中座位列表中
                            addSeat(seatEntity);
                            addSeat(coupleSeat);
                            isChoosen = true;
                        } else {
                            isChoosen = false;
                            //选中失败,座位满
                            if (mSeatChooseEvent != null) {
                                mSeatChooseEvent.seletedFull(true);
                            }
                        }
                    }
                }

                if (mSeatChooseEvent != null) {
                    //成功选中(更新了选中的状态)
                    if (isChoosen) {
                        mSeatChooseEvent.seatStatusChanged(new AbsSeatEntity[]{seatEntity, coupleSeat}, seatEntity.isChosen() > 0);
                    } else {
                        //没有更新选中状态,选中失败
                        mSeatChooseEvent.seletedFail(true);
                    }
                }
            }
        } else {
            isHandle = false;
        }
        return isHandle;
    }

    /**
     * 更新单击座位选中事件
     *
     * @param seatEntity 选中座位
     */
    protected boolean updateSingleSeat(AbsSeatEntity seatEntity) {
        if (seatEntity == null) {
            return false;
        }
        //是否被选中状态
        boolean isChoosen = false;
        int rowIndexInMap = seatEntity.getX();
        int columnIndexInMap = seatEntity.getY();

        if (seatEntity.isChosen() > 0) {
            //选座成功，当前座位为选中状态，将状态改为未选中，且从选中座位列表中移除该座位
            mSeatDrawHandle.updateSeatInMap(-1, rowIndexInMap, columnIndexInMap);
            removeSeat(seatEntity);
            isChoosen = true;
        } else if (seatEntity.isChosen() < 0) {
            if (mCurrentSeletedSeats.size() < mMostSeletedCount) {
                //选座成功，当前座位为未选中状态，选中该座位并将座位加入选中座位列表中
                mSeatDrawHandle.updateSeatInMap(1, rowIndexInMap, columnIndexInMap);
                addSeat(seatEntity);
                isChoosen = true;
            } else {
                //当前选中座位数已达上限，回调接口，不将座位加入选中列表中
                if (mSeatChooseEvent != null) {
                    mSeatChooseEvent.seletedFull(false);
                    return false;
                }
            }
        }

        if (mSeatChooseEvent != null) {
            if (isChoosen) {
                //若座位有效且未满，则回调选中座位结果
                mSeatChooseEvent.seatStatusChanged(new AbsSeatEntity[]{seatEntity}, seatEntity.isChosen() > 0);
                return true;
            } else {
                mSeatChooseEvent.seletedFail(false);
                return false;
            }
        }
        return false;
    }

    @Override
    public void chooseSeatFail() {
        if (mSeatChooseEvent != null) {
            mSeatChooseEvent.seletedFail(false);
        }
    }

    @Override
    public void scaleMaximum() {
        if (mSeatChooseEvent != null) {
            mSeatChooseEvent.scaleMaximum();
        }
    }

    @Override
    public ISeatDrawInterface getSeatDrawInterface() {
        return mSeatDrawHandle;
    }

    /**
     * 默认最多选择的个数为5，当参数不合法时该重置为默认值
     */
    @Override
    public void setMostSeletedCount(int mostCount) {
        mMostSeletedCount = mostCount > 0 ? mostCount : 5;
    }

    @Override
    public List<AbsSeatEntity> getSeletedSeats() {
        if (mCurrentSeletedSeats == null || mCurrentSeletedSeats.size() <= 0) {
            return null;
        } else {
            List<AbsSeatEntity> seletedSeatsList = new ArrayList<>(mCurrentSeletedSeats.size());
            seletedSeatsList.addAll(mCurrentSeletedSeats);
            return seletedSeatsList;
        }
    }

    @Override
    public void setISeatChooseEvent(ISeatChooseEvent eventListener) {
        mSeatChooseEvent = eventListener;
    }
}
