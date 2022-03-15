package com.game.base.relation.player;

/**
 * @author zheng
 */
public class PlayerBaseInfo {
    private String name;
    private String picture;

    public PlayerBaseInfo(String name, String picture) {
        this.name = name;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
