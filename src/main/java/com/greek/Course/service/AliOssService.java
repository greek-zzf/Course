package com.greek.Course.service;

import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.Credentials;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.auth.DefaultCredentials;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
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
import java.util.Objects;

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
        Video video = videoRepository.findById(id).orElseThrow(() -> HttpException.notFound("??????????????????"));

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, secretAccessKey, "");

        try {
            // ????????????URL???????????????????????????????????????????????? 1 ?????????
            Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
            // ?????????GET?????????????????????URL?????????????????????????????????????????????????????????

            String objectName = StrUtil.format("{}/{}", dir, video.getUrl());
            URL url = ossClient.generatePresignedUrl(bucket, objectName, expiration);
            return url.toString();
        } catch (OSSException e) {
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
