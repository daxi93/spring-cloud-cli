package com.dx.common.core.util;

import java.util.UUID;

public class GuidUtil {

    public static String next() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
