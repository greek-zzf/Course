package com.greek.Course.model;

public class AliOssConfig {
    private String host;
    private String policy;
    private String accessid;
    private String signature;

    private long expire;
    private String callback;
    private String dir;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getAccessid() {
        return accessid;
    }

    public void setAccessid(String accessid) {
        this.accessid = accessid;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    // public AliOssConfig(Builder builder) {
    //     this.host = builder.host;
    //     this.policy = builder.policy;
    //     this.signature = builder.signature;
    //     this.accessid = builder.accessid;
    //     this.expire = builder.expire;
    //     this.callback = builder.callback;
    //     this.dir = builder.dir;
    // }
    //
    // public static class Builder {
    //
    //     private String host;
    //     private String policy;
    //     private String accessid;
    //     private String signature;
    //     private long expire;
    //     private String callback;
    //     private String dir;
    //
    //     public Builder(String host, String accessid, String policy, String signature) {
    //         this.host = host;
    //         this.accessid = accessid;
    //         this.policy = policy;
    //         this.signature = signature;
    //     }
    //
    //     public Builder expire(long expire) {
    //         this.expire = expire;
    //         return this;
    //     }
    //
    //     public Builder callback(String callback) {
    //         this.callback = callback;
    //         return this;
    //     }
    //
    //     public Builder dir(String dir) {
    //         this.dir = dir;
    //         return this;
    //     }
    //
    //     public AliOssConfig build() {
    //         return new AliOssConfig(this);
    //     }
    // }

}
