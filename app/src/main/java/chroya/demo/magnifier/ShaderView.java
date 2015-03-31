package chroya.demo.magnifier;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 放大镜实现方式1
 *
 * @author chroya
 */
public class ShaderView extends TextView {
    private Bitmap bitmap;
    private ShapeDrawable drawable;
    //放大镜的半径
    private static final int RADIUS = 80;
    //放大倍数
    private static final int FACTOR = 2;
    private Matrix matrix = new Matrix();
    private BitmapShader mShader;
    private final String mDemoString;

    public ShaderView(Context context) {
        super(context);
        mDemoString = getContext().getString(R.string.demo);
        setText(mDemoString);

        setDrawingCacheEnabled(true);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (bitmap == null) {
                Bitmap bmp = getDrawingCache();

                bitmap = bmp;
                mShader = new BitmapShader(
                        Bitmap.createScaledBitmap(bmp, bmp.getWidth() * FACTOR,
                                bmp.getHeight() * FACTOR, true), TileMode.CLAMP, TileMode.CLAMP);
            }

            //圆形的drawable
            drawable = new ShapeDrawable(new OvalShape());
            drawable.getPaint().setShader(mShader);
            drawable.setBounds(0, 0, RADIUS * 2, RADIUS * 2);

            final int x = (int) event.getX();
            final int y = (int) event.getY();
            //这个位置表示的是，画shader的起始位置
            matrix.setTranslate(RADIUS - x * FACTOR, RADIUS - y * FACTOR);
            drawable.getPaint().getShader().setLocalMatrix(matrix);
            //bounds，就是那个圆的外切矩形
            drawable.setBounds(x - RADIUS, y - RADIUS, x + RADIUS, y + RADIUS);
            invalidate();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            bitmap = null;
            invalidate();

            int offsetForPosition = getOffsetForPosition(event.getX(), event.getY());
            String word = WordHelper.extractWordForPosition(mDemoString, offsetForPosition);
            if (word != null) {
                Toast.makeText(getContext(), String.format("You selected word is %s", word), Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            drawable.draw(canvas);
        }
    }
}
