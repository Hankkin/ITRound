package com.hankkin.itround.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.hankkin.itround.R;

import java.util.Random;

/**
 * Created by wwl on 2016/4/11.
 * 验证码
 */
public class CodeView extends View{
    private int DEFAULT_COUNT = 4;//默认数字个数
    private int DEFAULT_LINE = 50;//默认干扰线条数
    private int DEFAULT_COLOR = Color.RED;//默认字体颜色
    private int DEFAULT_FONT_SIZE = 18;//默认字体大小sp

    private int count;
    private int line_count;
    private int font_color;
    private int font_size;
    private Random rmd;
    private Paint mPaint;
    private String code;

    public CodeView(Context context) {
        this(context,null);
    }

    public CodeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CodeView);
        font_color = a.getColor(R.styleable.CodeView_font_color,DEFAULT_COLOR);
        font_size = a.getDimensionPixelSize(R.styleable.CodeView_font_size,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_FONT_SIZE, getResources().getDisplayMetrics()));
        count = a.getInt(R.styleable.CodeView_count, DEFAULT_COUNT);
        line_count = a.getInt(R.styleable.CodeView_line, DEFAULT_LINE);

        rmd = new Random();
        mPaint = new Paint();

        code = getCode();
        initPaint();
    }
    //获取验证码
    private String getCode(){
        String code = "";
        for (int i = 0 ; i < count; i++){
            code += rmd.nextInt(10);
        }
        return code;
    }
    //初始化paint
    private void initPaint(){
//        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(font_color);
        mPaint.setTextSize(font_size);
    }
    //计算宽度
    private int measureWidth(int widthMeasureSpec){
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY){
            return width;
        }else if(mode == MeasureSpec.AT_MOST){
            return getPaddingLeft()+getRect().width()+getPaddingRight();
        }
        return  0;
    }
    //计算高度
    private int measureHeght(int heightMeasureSpec){
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY){
            return height;
        }else if (mode == MeasureSpec.AT_MOST){
            return getPaddingTop()+getRect().height()+getPaddingBottom();
        }
        return  0 ;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeght(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        Rect rect = new Rect(0,0,width,height);

        //绘制外围矩形框
        Rect rect1 = new Rect(rect);
        rect1.inset(2,2);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(0xff00ffff);
        canvas.drawRect(rect1, mPaint);
        mPaint.setStyle(Paint.Style.FILL);

        //绘制随机干扰线
        mPaint.setColor(Color.GRAY);
        for (int i = 0 ; i < line_count;i++){
            int startX = rmd.nextInt(width);
            int startY = rmd.nextInt(height);
            int stopX = rmd.nextInt(width);
            int stopY = rmd.nextInt(height);
            canvas.drawLine(startX,startY,stopX,stopY,mPaint);

        }

        //绘制文字
        mPaint.setColor(font_color);
        Rect textRect = getRect();
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        int x = (width - textRect.width())/2;
        int y = (int)(height/2+(fontMetrics.descent-fontMetrics.ascent)/2-fontMetrics.descent);
        canvas.drawText(code,x,y,mPaint);
    }

    //获取字体所占的区域
    private Rect getRect(){
        Rect rect = new Rect();
        mPaint.getTextBounds(code,0,code.length(),rect);
        return rect;
    }

    //刷新验证码
    public void refresh(){
        code = getCode();
        invalidate();//重新ondraw
    }
    //获取验证码
    public String getTheCode(){
        return code;
    }
    //判断用户输入验证码是否正确
    public boolean isRigth(String inputCode){
        if (inputCode == null)
            return false;
        if (inputCode.equals(code))
            return true;
        return false;
    }
}