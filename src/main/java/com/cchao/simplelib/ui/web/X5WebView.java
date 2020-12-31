package com.cchao.simplelib.ui.web;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.cchao.simplelib.R;
import com.cchao.simplelib.core.AndroidHelper;
import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.util.UrlUtil;
import com.tencent.smtt.sdk.WebView;

import java.util.Map;

/**
 * 复写  x5webview
 *
 * @author cch
 * @version 2020/12/28
 */
public class X5WebView extends WebView {
    public int mTouchX;
    public int mTouchY;

    public X5WebView(Context context) {
        this(context, null, 0, null, false);
    }

    public X5WebView(Context context, boolean b) {
        this(context, null, 0, null, b);
    }

    public X5WebView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, null, false);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, null, false);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i, boolean b) {
        this(context, attributeSet, i, null, b);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i, Map<String, Object> map, boolean b) {
        super(context, attributeSet, i, map, b);
        Logs.logEvent("xwebview X5WebView");
        initImageDown();
    }

    private void initImageDown() {
        setOnLongClickListener(v -> {
            Logs.logEvent("xwebview 长按唤起");
            HitTestResult result = getHitTestResult();
            if (null == result) {
                return false;
            }
            int type = result.getType();
            switch (type) {
                case HitTestResult.EDIT_TEXT_TYPE: // 选中的文字类型
                    break;
                case HitTestResult.PHONE_TYPE: // 处理拨号
                    break;
                case HitTestResult.EMAIL_TYPE: // 处理Email
                    break;
                case HitTestResult.GEO_TYPE: // 　地图类型
                    break;
                case HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                    break;
                case HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型
                case HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                    if (!UrlUtil.isValidUrl(result.getExtra())) {
                        break;
                    }
                    ImageDialogFragment dialogFragment = new ImageDialogFragment(mTouchX, mTouchY, result.getExtra());
                    dialogFragment.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "dsf");
                    return true;
                case HitTestResult.UNKNOWN_TYPE: //未知
                    break;
            }
            return false;
        });
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (clampedX || clampedY) {
            ViewParent parent = getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(false);
            }
        }
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        mTouchX = (int) event.getRawX();
        mTouchY = (int) event.getRawY();
        return super.onInterceptTouchEvent(event);
    }

    private ViewParent findViewParentIfNeeds(ViewParent parent) {
        while (true) {
            if (parent == null) {
                return null;
            }
            if (parent instanceof ViewPager
                || parent instanceof AbsListView
                || parent instanceof ScrollView
                || parent instanceof HorizontalScrollView) {
                return parent;
            } else if (parent instanceof View) {
                parent = parent.getParent();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ViewParent parent = getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
        return super.onTouchEvent(event);
    }

    public static class ImageDialogFragment extends DialogFragment {
        public String imageUrl;
        public int mTouchX;
        public int mTouchY;

        public ImageDialogFragment(int x, int y, String content) {
            mTouchX = x;
            mTouchY = y;
            imageUrl = content;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_save_image, container, false);
        }

        @Override
        public void onStart() {
            super.onStart();
            Logs.logEvent("ImageDialogFragment start");
            if (getDialog() == null) {
                return;
            }
            Window window = getDialog().getWindow();
            if (window == null) {
                return;
            }
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setGravity(Gravity.LEFT | Gravity.TOP);
            lp.x = mTouchX;//横坐标位置
            lp.y = mTouchY;//纵坐标位置
            lp.width = UiHelper.dp2px(160);
            lp.dimAmount = 0.0f;//外层背景透明，默认变暗
//                        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//内部背景透明
            Logs.logEvent("ImageDialogFragment start end");
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            OnClickListener clickListener = v -> {
                if (v.getId() == R.id.save_image) {
                    ImageLoader.downloadImageToLocalPicture(imageUrl);
                } else if (v.getId() == R.id.save_image_link) {
                    AndroidHelper.setClipboardData(imageUrl, null);
                }
                dismiss();
            };
            view.findViewById(R.id.save_image).setOnClickListener(clickListener);
            view.findViewById(R.id.save_image_link).setOnClickListener(clickListener);
            super.onViewCreated(view, savedInstanceState);
        }
    }
}
