package com.icplatform.dto;

public class FileUploadResponse {
    private String message;  // 成功与否
    private String newToken;  //新的token

    public FileUploadResponse(String message, String newToken) {
        this.message = message;
        this.newToken = newToken;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getNewToken() {
        return newToken;
    }
    public void setNewToken(String newToken) {
        this.newToken = newToken;
    }
}
