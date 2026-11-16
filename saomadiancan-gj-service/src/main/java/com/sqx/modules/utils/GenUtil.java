package com.sqx.modules.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chengming
 * @since 2024/9/19
 */
public class GenUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 生成订单号
     * @return
     */
    public static String genOrderNo() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        String format = sdf.format(date);
        return format + newString;
    }
}
