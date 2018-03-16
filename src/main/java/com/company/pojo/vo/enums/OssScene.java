package com.company.pojo.vo.enums;

public enum OssScene {
    DEFAULT("default", "默认路径"),
    ACCOUNT("account", "账号");

    OssScene(String filePath, String description) {
        this.filePath = filePath;
        this.description = description;
    }

    private String filePath;
    private String description;

    public String getFilePath() {
        return filePath;
    }
}
