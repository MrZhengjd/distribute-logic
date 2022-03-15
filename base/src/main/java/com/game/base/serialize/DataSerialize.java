package com.game.base.serialize;

public interface DataSerialize {
    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] data, Class<T> cls);


}
