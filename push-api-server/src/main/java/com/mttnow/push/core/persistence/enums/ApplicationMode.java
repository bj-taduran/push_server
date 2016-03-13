package com.mttnow.push.core.persistence.enums;

public enum ApplicationMode {
    DEVELOPMENT("Development"), PRODUCTION("Production");


    private String name;

    ApplicationMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
