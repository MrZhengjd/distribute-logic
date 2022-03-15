package com.game.base.register;

public interface BalanceProvider<T> {
     T getBalanceItem(String serviceName);
    void regist(String path, String content);
}
