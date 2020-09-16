package com.dx.common.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

@JsonComponent
public class BigDecimalJsonComponent extends JsonSerializer<BigDecimal> {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        if(bigDecimal != null) {
            df.setMaximumFractionDigits(2);//显示几位修改几
            df.setGroupingSize(0);
            df.setRoundingMode(RoundingMode.FLOOR);
            //根据实际情况选择使用
//             gen.writeString(df.format(value));  // 返回出去是字符串
            jsonGenerator.writeNumber(df.format(bigDecimal));  // 返回出去是数字形式
        }
    }
}
