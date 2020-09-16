package com.dx.common.core.sms;

import lombok.Data;

import java.util.Date;

/**
 * @author Darcy
 */
@Data
public class RuleEntity {

    private Date sendTime = new Date();

    private Integer sendCount = 0;
}
