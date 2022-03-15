package com.game.base.register;

import java.util.List;

public abstract class AbstractBalanceProvider<T> implements BalanceProvider<T> {
    protected abstract T balanceAlgorithm(List<T> items);
    protected abstract List<T> getBalanceItems(String serviceName);

    @Override
    public void regist(String path, String content) {

    }

    @Override
    public T getBalanceItem(String serviceName){
        return balanceAlgorithm(getBalanceItems(serviceName));
    }
}
