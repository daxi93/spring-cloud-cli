package com.dx.common.core.sms;

import com.dx.common.core.exception.DxException;
import com.dx.common.core.util.ContextUtil;
import com.dx.common.core.util.RedisUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 短信发送规则
 * 一天小于5次  发送时间大于一分钟
 * @author Darcy
 */
public class SmsRule {

    private static final String REDIS_SMS_RULE_HASH = "DX:SMS:RULE:";

    public static void check(String phone) {
        RedisUtil redisUtil = ContextUtil.getBean(RedisUtil.class);
        RuleEntity ruleEntity = (RuleEntity) redisUtil.hget(REDIS_SMS_RULE_HASH, phone);
        LocalDateTime now = LocalDateTime.now();
        if (ruleEntity == null) {
            return;
        }
        //不是同一天
        LocalDateTime sendLocalTime = ruleEntity.getSendTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        if (!now.toLocalDate().equals(sendLocalTime.toLocalDate())) {
            redisUtil.hdel(REDIS_SMS_RULE_HASH, phone);
            return;
        }

        //次数 或者 时间
        long nowTime = System.currentTimeMillis() / 1000;
        long sendTime = ruleEntity.getSendTime().getTime() / 1000;
        if (ruleEntity.getSendCount() > 5 || nowTime - sendTime <= 60) {
            throw new DxException(2, "发送过于频繁");
        }


    }

    /**
     * 发送成功后加1
     *
     * @param phone 手机
     */

    public static void update(String phone) {
        RedisUtil redisUtil = ContextUtil.getBean(RedisUtil.class);
        RuleEntity ruleEntity = (RuleEntity) redisUtil.hget(REDIS_SMS_RULE_HASH, phone);
        if (ruleEntity == null) {
            ruleEntity = new RuleEntity();
        }
        ruleEntity.setSendCount(ruleEntity.getSendCount() + 1);
        redisUtil.hset(REDIS_SMS_RULE_HASH, phone, ruleEntity);
    }
}
