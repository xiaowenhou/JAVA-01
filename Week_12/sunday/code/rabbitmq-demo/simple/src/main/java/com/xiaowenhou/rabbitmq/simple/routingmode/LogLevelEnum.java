package com.xiaowenhou.rabbitmq.simple.routingmode;

public enum LogLevelEnum {

    WARN("WARN"),

    ERROR("ERROR"),

    FATAL("FATAL");


    private String level;


    LogLevelEnum(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
}
