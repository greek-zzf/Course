package com.greek.Course.model;

public class AliOssConfig {
    private String host;
    private String policy;
    private String accessid;
    private String signature;

    private long expire;
    private String callback;
    private String dir;

    public AliOssConfig(Builder builder) {
        this.host = builder.host;
        this.policy = builder.policy;
        this.signature = builder.signature;
        this.accessid = builder.accessid;
        this.expire = builder.expire;
        this.callback = builder.callback;
        this.dir = builder.dir;
    }

    public static class Builder {

        private String host;
        private String policy;
        private String accessid;
        private String signature;
        private long expire;
        private String callback;
        private String dir;

        public Builder(String host, String accessid, String policy, String signature) {
            this.host = host;
            this.accessid = accessid;
            this.policy = policy;
            this.signature = signature;
        }

        public Builder expire(long expire) {
            this.expire = expire;
            return this;
        }

        public Builder callback(String callback) {
            this.callback = callback;
            return this;
        }

        public Builder dir(String dir) {
            this.dir = dir;
            return this;
        }

        public AliOssConfig build() {
            return new AliOssConfig(this);
        }
    }

}
