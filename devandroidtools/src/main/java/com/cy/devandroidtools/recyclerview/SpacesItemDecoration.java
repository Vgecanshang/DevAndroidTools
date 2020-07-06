package com.cy.devandroidtools.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
	private Context context;
	private int topBottom ;
	private int leftRight ;
    public SpacesItemDecoration( Context context ,int  topBottom , int leftRight) {
        this.context = context;
        this.topBottom = topBottom;
        this.leftRight = leftRight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
//        outRect.left = space;
//        outRect.right = space;
//        outRect.bottom = space;
//        outRect.top = space;
        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildPosition(view) == 0){
//        	outRect.top = space;
//        	
//        }
    	
//        System.out.println("测试Item = " + parent.getChildPosition(view));
        if(parent.getChildAdapterPosition(view) % 2 == 0){
        	if(parent.getChildAdapterPosition(view) == 1){
        		outRect.top = leftRight;
        	}else{
        		outRect.top = topBottom;
        	}
        	outRect.left = topBottom;
        	outRect.right = leftRight;
        	outRect.bottom = topBottom;
        	
        }else{
        	outRect.left = leftRight;
        	outRect.right = topBottom;
        	outRect.bottom = topBottom;
        	
        	if(parent.getChildAdapterPosition(view) == 0){
        		outRect.top = leftRight;
        	}else{
        		outRect.top = topBottom;
        	}
        	
        }
        
//        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
//        //判断总的数量是否可以整除
//        int totalCount = layoutManager.getItemCount();
//        int surplusCount = totalCount % layoutManager.getSpanCount();
//        int childPosition = parent.getChildAdapterPosition(view);
//        if (layoutManager.getOrientation() == GridLayoutManager.VERTICAL) {//竖直方向的
//            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
//                //后面几项需要bottom
//                outRect.bottom = topBottom;
//            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
//                outRect.bottom = topBottom;
//            }
//            if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {//被整除的需要右边
//                outRect.right = leftRight;
//            }
//            outRect.top = topBottom;
//            outRect.left = leftRight;
//        } else {
//            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
//                //后面几项需要右边
//                outRect.right = leftRight;
//            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
//                outRect.right = leftRight;
//            }
//            if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {//被整除的需要下边
//                outRect.bottom = topBottom;
//            }
//            outRect.top = topBottom;
//            outRect.left = leftRight;
//        }
    }
    
}
