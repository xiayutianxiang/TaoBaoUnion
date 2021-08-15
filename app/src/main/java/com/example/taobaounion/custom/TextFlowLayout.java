package com.example.taobaounion.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taobaounion.R;
import com.example.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextFlowLayout extends ViewGroup {
    private List<String> mTextList = new ArrayList<>();
    private static final int DEFAULT_SPACE = 10;
    private float mItemHorizontalSpace = DEFAULT_SPACE;
    private float mItemVerticalSpace = DEFAULT_SPACE;
    private int mSize;
    private int mMeasuredHeight;
    private OnFlowTextItemClickListener mFlowClickListener = null;

    public int getContentSize(){
        return mTextList.size();
    }

    public float getItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        mItemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        mItemVerticalSpace = itemVerticalSpace;
    }

    public TextFlowLayout(Context context) {
        this(context, null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //去拿到相关属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowTextStyle);
        mItemHorizontalSpace = ta.getDimension(R.styleable.FlowTextStyle_horizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = ta.getDimension(R.styleable.FlowTextStyle_verticalSpace, DEFAULT_SPACE);
        ta.recycle();
        LogUtils.d(this, "mItemHorizontalSpace ----> " + mItemHorizontalSpace);
        LogUtils.d(this, "mItemVerticalSpace ----> " + mItemVerticalSpace);
    }

    public void setTextList(List<String> textList) {
        removeAllViews();
        this.mTextList.clear();
        this.mTextList.addAll(textList);
        Collections.reverse(mTextList);
        //遍历内容
        for (String text : mTextList) {
            //添加子view
            //LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view,this,true); true与false的区别
            //等价于
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setText(text);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mFlowClickListener!=null){
                        mFlowClickListener.onFlowItemClick(text);
                    }
                }
            });
            addView(item);
            //TODO:
        }
    }


    //这个描述所有的行
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getChildCount()==0){
            return;
        }
        //这个是单行
        List<View> line = null;
        lines.clear();
        mSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        //LogUtils.d(this, "mSize --- >" + mSize);
        //测量
       //LogUtils.d(this, "onMeasure --- > " + getChildCount());
        //测量孩子
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility() != VISIBLE) {
                //不需要
                continue;
            }
            //测量前
           // LogUtils.d(this, "before height --- > " + itemView.getMeasuredHeight());
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);  //测量后可以获取高度
            //LogUtils.d(this, "after height --- > " + itemView.getMeasuredHeight());
            if (line == null) {
                //说明当前行为空，可以添加
                line = createNewLine(itemView);
            } else {
                if (canBeAdd(itemView, line)) {
                    //可以添加
                    line.add(itemView);
                } else {
                    //新建一行
                   line = createNewLine(itemView);
                }
            }
        }
        mMeasuredHeight = getChildAt(0).getMeasuredHeight();
        int selfHieght = (int) (lines.size() * mMeasuredHeight + mItemVerticalSpace * (lines.size() + 1) + 0.5f);
        //测量自己
        setMeasuredDimension(mSize, selfHieght);
    }

    private List<View> createNewLine(View itemView) {
        List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     * 判断当前行能否被添加
     *
     * @param itemView
     * @param line
     */
    private boolean canBeAdd(View itemView, List<View> line) {
        //所有的已经添加进去的view宽度加起来再加上间距
        int totalWidth = itemView.getMeasuredWidth();
        for (View view : line) {
            totalWidth += view.getMeasuredWidth();
        }
        //水平间距的宽度
        totalWidth += mItemHorizontalSpace * (line.size() + 1);
        LogUtils.d(this, "totalWidth --- >" + totalWidth);
        LogUtils.d(this, "totalWidth --- >" + mSize);
        //如果小于/等于当前控件的宽度，则可以添加，否则不能添加
        return totalWidth <= mSize;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //摆放孩子
        //LogUtils.d(this, "onLayout --- > " + getChildCount());
//        View itemOne = getChildAt(0);
//        itemOne.layout(0,0,itemOne.getMeasuredWidth(),itemOne.getMeasuredHeight());
//
//        View itemTwo = getChildAt(1);
//        itemTwo.layout((int) (itemOne.getRight()+mItemHorizontalSpace),
//                0,
//                itemOne.getRight()+(int) mItemHorizontalSpace+itemTwo.getMeasuredWidth(),
//                itemTwo.getMeasuredHeight());

        int topOffset = (int) mItemHorizontalSpace;
        for (List<View> views : lines) {
            //views是每一行
            int leftOffset = (int) mItemHorizontalSpace;
            for (View view : views) {
                //每一行的item
                view.layout(leftOffset, topOffset, leftOffset + view.getMeasuredWidth(), topOffset + view.getMeasuredHeight());
                leftOffset += view.getMeasuredWidth() + mItemHorizontalSpace;
            }
            topOffset += mMeasuredHeight + mItemHorizontalSpace;
        }
    }

    public void setOnFlowTextItemClickListener(OnFlowTextItemClickListener listener){
        this.mFlowClickListener = listener;
    }

    public interface OnFlowTextItemClickListener{
        void onFlowItemClick(String text);
    }
}
