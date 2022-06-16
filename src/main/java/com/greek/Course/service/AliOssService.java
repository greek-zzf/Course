package com.greek.Course.service;

import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.Credentials;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.auth.DefaultCredentials;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.greek.Course.dao.VideoRepository;
import com.greek.Course.exception.HttpException;
import com.greek.Course.model.AliOssConfig;
import com.greek.Course.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class AliOssService {

    @Autowired
    private VideoRepository videoRepository;

    @Value("${ali.oss.access-key-id}")
    private String accessKeyId;
    @Value("${ali.oss.secret-access-key}")
    private String secretAccessKey;
    @Value("${ali.oss.endpoint}")
    private String endpoint;
    @Value("${ali.oss.bucket}")
    private String bucket;
    @Value("${ali.oss.security-token}")
    private String securityToken;
    @Value("${ali.oss.dir}")
    private String dir;

    OSSClient client;

    @PostConstruct
    public void initClient() {
        Credentials credentials = new DefaultCredentials(accessKeyId, secretAccessKey);
        client = (OSSClient) OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(new DefaultCredentialProvider(credentials))
                .build();
    }

    public AliOssConfig getPolicyAndSign() {

        long expireTimeSeconds = 300;
        long expireTimeMillis = System.currentTimeMillis() + expireTimeSeconds * 1000;
        Date expiration = new Date(expireTimeMillis);
        PolicyConditions policyConds = new PolicyConditions();
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

        String postPolicy = client.generatePostPolicy(expiration, policyConds);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String policy = BinaryUtil.toBase64String(binaryData);
        String signature = client.calculatePostSignature(postPolicy);

        String host = StrUtil.format("https://{}.{}", bucket, endpoint);

        AliOssConfig result = new AliOssConfig();
        result.setPolicy(policy);
        result.setSignature(signature);
        result.setExpire(expireTimeMillis / 1000);
        result.setDir(dir);
        result.setAccessid(accessKeyId);
        result.setHost(host);
        return result;
    }

    public String getVideoUrl(Integer id) {
        Video video = videoRepository.findById(id).orElseThrow(() -> HttpException.notFound("该视频不存在"));

        String objectName = StrUtil.format("{}/{}", dir, video.getUrl());

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, secretAccessKey, securityToken);

        try {
            // 设置签名URL过期时间，单位为毫秒，过期时间为 1 小时。
            Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
            // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
            URL url = ossClient.generatePresignedUrl(bucket, objectName, expiration);
            return url.toString();
        } catch (OSSException | ClientException oe) {
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
