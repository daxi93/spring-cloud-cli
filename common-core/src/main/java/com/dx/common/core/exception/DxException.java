package com.dx.common.core.exception;

import com.dx.common.core.r.R;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DxException extends RuntimeException {

    private R<?> r;

    public DxException(String error) {
        r = R.fail(error);
    }

    public DxException(Integer status, String error) {
        r = R.fail(status, error);
    }

    public DxException(Integer status, Throwable cause) {
        r = R.fail(status, cause.getMessage());
    }


    @Override
    public String getMessage() {
        return r.getError();
    }
}
