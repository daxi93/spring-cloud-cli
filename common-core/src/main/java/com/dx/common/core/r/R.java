package com.dx.common.core.r;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@ApiModel("返回结果")
@Data
public class R<T> {

    private R() {
        this.timestamp = new Date();
        this.status = 200;
    }

    @ApiModelProperty(value = "时间戳", notes = "时间戳")
    private Date timestamp;

    @ApiModelProperty(value = "状态码", notes = "状态码:200成功;其他为失败")
    private Integer status;

    @ApiModelProperty(value = "错误信息", notes = "错误信息;错误时返回")
    private String error;

    @ApiModelProperty(value = "消息", notes = "消息说明，一般正确时返回")
    private String message;

    @ApiModelProperty(value = "数据", notes = "数据载体")
    private T data;


    public static <T> R<T> success() {
        return success("成功");
    }

    public static <T> R<T> success(String msg) {
        return success(msg, null);
    }

    public static <T> R<T> success(T data) {
        return success("成功", data);
    }

    public static <T> R<T> success(String message, T data) {
        R<T> r = new R<>();
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static <T> R<T> fail(String error) {
        return fail(500, error);
    }

    public static <T> R<T> fail(Integer status, String error) {
        R<T> r = new R<>();
        r.setError(error);
        r.setStatus(status);
        return r;
    }

    public static class Builder {
        R<Map<String, Object>> rb = new R<>();

        public R<Map<String, Object>> build() {
            return rb;
        }


        public Builder status(Integer status) {
            rb.setStatus(status);
            return this;
        }

        public Builder error(String error) {
            rb.setError(error);
            return this;
        }

        public Builder data(String key, Object v) {
            rb.getData().put(key, v);
            return this;
        }
    }
}
