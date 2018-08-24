package com.infinity_coder.infinity.airtop.utils.decorations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.infinity_coder.infinity.airtop.R;

import org.jetbrains.annotations.NotNull;

public class SearchUserHorizontalLineItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable divider;

    public SearchUserHorizontalLineItemDecoration(@NonNull @NotNull Context context, @DrawableRes int id){
        divider = ContextCompat.getDrawable(context, id);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        for (int i = 0; i < parent.getChildCount() - 1; i++) {
            View child = parent.getChildAt(i);
            View tvUsername = child.findViewById(R.id.tvUsername);
            int right = child.getWidth();
            int left = tvUsername.getLeft();
            int top = child.getBottom() + 2;
            int bottom = top + divider.getMinimumHeight();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
