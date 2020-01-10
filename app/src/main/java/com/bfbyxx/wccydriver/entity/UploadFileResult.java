package com.bfbyxx.wccydriver.entity;

/**
 * 上传文件接口返回的数据对象
 */
public class UploadFileResult<T> {

    private String Id;
    private String FileName;
    private String Url;

    public UploadFileResult(String id, String fileName, String url) {
        Id = id;
        FileName = fileName;
        Url = url;
    }


    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }
}
