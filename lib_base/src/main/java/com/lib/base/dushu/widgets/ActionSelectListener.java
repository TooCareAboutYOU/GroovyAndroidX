package com.lib.base.dushu.widgets;

/**
 * @author zhangshuai
 *  anchorNode 返回该选区起点所在的节点
 *  focusNode 返回该选区终点所在的节点
 */
public interface ActionSelectListener {

    /**
     * 获取选中文字、标记文本偏移量
     * @param value  选中的文本
     * @param title  菜单弹框类型
     * @param isCollapsed  用于判断选区的起始点和终点是否在同一位置
     * @param anchorOffset 返回一个数字，表示该选区起点在anchorNode中的位置偏移量
     * @param focusOffset 返回一个数字，表示该选区终点在focusNode中的位置偏移量。
     *
     *    1、当 focusOffset > anchorOffset，表示从右向左滑动选择文本，反之从左向右滑动
     */
    void onClickEvent(String value,String title,boolean isCollapsed,String anchorOffset,String focusOffset);

}
