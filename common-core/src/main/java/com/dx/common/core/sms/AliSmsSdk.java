package com.dx.common.core.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.dx.common.core.exception.DxException;
import com.dx.common.core.util.ContextUtil;
import com.dx.common.core.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AliSmsSdk {


    private static final String ACCESS_KEY_ID = "***";

    private static final String ACCESS_KEY_SECRET = "***";

    private static final String SIGN_NAME = "***";

    private static final String REDIS_SMS_CODE_PREFIX = "DX:SMS:PREFIX";

    private static final IAcsClient CLIENT;

    static {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        CLIENT = new DefaultAcsClient(profile);
    }

    public static void sendSms(String phone, String templateCode, String templateParam) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", SIGN_NAME);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", templateParam);

        try {
            CommonResponse response = CLIENT.getCommonResponse(request);
            log.info("---发送短信成功:{}", response.getData());
        } catch (ClientException ex) {
            log.error("===>发送短信失败", ex);
        }
    }


    public static void codeCheck(String phone, String code) {
        RedisUtil redisUtil = ContextUtil.getBean(RedisUtil.class);
        Object o = redisUtil.get(REDIS_SMS_CODE_PREFIX + phone);
        if (o == null) {
            throw new DxException(4988, "验证码超过有效期");
        }
        if (!code.equals(o)) {
            throw new DxException(4966, "验证码不正确");
        }
        redisUtil.del(REDIS_SMS_CODE_PREFIX + phone);
    }


}
