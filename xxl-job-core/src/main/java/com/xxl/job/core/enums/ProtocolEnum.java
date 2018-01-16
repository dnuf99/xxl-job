package com.xxl.job.core.enums;

import org.apache.http.entity.ContentType;

public enum ProtocolEnum {

    Hessian(ContentType.DEFAULT_BINARY,"Hessian 序列化"),
    JOSN(ContentType.APPLICATION_JSON,"JOSN 格式");

    private ContentType contentType;
    private String desc;

    ProtocolEnum(ContentType contentType, String desc){
        this.contentType = contentType;
        this.desc = desc;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static ProtocolEnum getByContentType(String contentType){
        for(ProtocolEnum protocol : ProtocolEnum.values()){
            if(protocol.getContentType().toString().equals(contentType)){
                return protocol;
            }
        }

        return Hessian;

    }
}
