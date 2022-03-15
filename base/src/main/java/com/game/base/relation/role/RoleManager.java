package com.game.base.relation.role;

/**
 * @author zheng
 */
public class RoleManager {
    private Role role;

    public RoleManager(Role role) {
        this.role = role;
    }

//    public void callAllBehave(Room room){
//        for (Action behavior : behaviorMap.values()) {
//            behavior.operate(role,room);
//        }
//    }
//    public void callBehaveByName(String name,Room room){
//        behaviorMap.get(name).operate(role,room);
//    }

}
