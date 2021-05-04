package com.beestudio.beecore.recyclerviewadapter.diff;

import androidx.annotation.NonNull;

/**
 * 使用java接口定义方法
 * @param <T>
 */
public interface DifferImp<T> {
    void addListListener(@NonNull ListChangeListener<T> listChangeListener);
}
