package com.muju.note.launcher.base;

/**
 * 基类
 * @param <T>
 */
public interface IPresenter<T extends IView> {
    void attachView(T view);

    void detachView();

}
