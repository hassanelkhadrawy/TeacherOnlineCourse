package com.example.teacheronlinecourse.Models;

public class FileModel {
    String fileUrl;
    boolean flag;

    public FileModel(String fileUrl, boolean flag) {
        this.fileUrl = fileUrl;
        this.flag = flag;
    }

    public FileModel() {
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
