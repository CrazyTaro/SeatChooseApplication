package us.bestapp.henrytaro.entity;/**
 * Created by xuhaolin on 15/9/2.
 */

import com.google.gson.annotations.SerializedName;

import us.bestapp.henrytaro.entity.interfaces.IRowEntity;
import us.bestapp.henrytaro.entity.interfaces.ISeatEntity;
import us.bestapp.henrytaro.params.SeatParams;
import us.bestapp.henrytaro.utils.StringUtils;

/**
 * Created by xuhaolin on 15/9/2.<br/>
 * 来自JSON数据中的对象
 */
public class Row implements IRowEntity {

    @SerializedName("rownum")
    private int mRowNum;
    @SerializedName("rowid")
    private String mRowId;
    @SerializedName("columns")
    private String mColumns;

    private int mColumnCount = 0;
    private boolean mIsDraw = false;
    private boolean mIsEmpty = true;

    private AbsSeat[] mColumnData = null;

    public Row() {
    }


    /**
     * 构造函数,设置行对象的基本数据,<font color="#ff9900"><b>解析列数据时会自动计算当前行是否需要绘制及数据是否为空,
     * 但在此处是否绘制及数据是否为空将由参数3/4决定,此构造函数不自动进行解析列信息,需要解析请调用方法{@link #parseJson()}</b></font>
     *
     * @param rowNumber  行号
     * @param columnInfo 列数据信息
     * @param isDraw     是否绘制此行
     * @param isEmpty    此行数据是否为空
     */
    public Row(int rowNumber, String columnInfo, boolean isDraw, boolean isEmpty) {
        this.mRowNum = rowNumber;
        this.mColumns = columnInfo;
        //记录当前行的绘制与数据空满
        this.mIsDraw = isDraw;
        this.mIsEmpty = isEmpty;
    }


    /**
     * 获取行ID
     *
     * @return
     */
    public String getRowId() {
        return this.mRowId;
    }

    /**
     * 解析已经加载的Json数据,此方法解析列信息时会自动解析行是否绘制及数据是否为空,若之前设定过绘制选项此处将会覆盖
     *
     * @return
     */
    public boolean parseJson() {
        if (StringUtils.isNullOrEmpty(mColumns)) {
            this.mIsDraw = false;
            this.mIsEmpty = true;
            return false;
        } else {
            //原始数据大致格式：ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0
            //ZL表示走廊，无座位
            //01@A0，表示第1行，A表示可选座位，LK表示锁定座位（不可选）
            //0表示座位类型，普通座位，1/2组成情侣座，情侣座不可单选，必须两个座位一起选
            //1级分离数据类型
            String[] columInfo = mColumns.split(",");
            if (mColumns != null) {
                mColumnData = new Seat[columInfo.length];
                for (int i = 0; i < columInfo.length; i++) {
                    AbsSeat newSeat = Seat.getNewInstance(mRowNum, columInfo[i]);
                    //座位解析
                    mColumnData[i] = newSeat;
                    if (newSeat != null && newSeat.getType() != SeatParams.SEAT_TYPE_UNSHOW) {
                        mColumnCount++;
                    }
                }
                this.mIsDraw = true;
                this.mIsEmpty = false;
                return true;
            } else {
                throw new RuntimeException("column info is unable");
            }
        }
    }

    @Override
    public int getRealColumnCount() {
        return this.mColumnCount;
    }

    @Override
    public int getColumnCount() {
        return this.mColumnData.length;
    }

    @Override
    public int getRowNumber() {
        return this.mRowNum;
    }

    @Override
    public void setIsDraw(boolean isDraw) {
        this.mIsDraw = isDraw;
    }

    @Override
    public boolean getIsDraw() {
        return this.mIsDraw;
    }

    @Override
    public boolean getIsEmpty() {
        return this.mIsEmpty;
    }


    @Override
    public ISeatEntity getSeat(int columnIndex) {
        try {
            return this.mColumnData[columnIndex];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}
