
package com.yuenidong.util;

import java.util.Random;

/**
 * 随机数(随机产生length个大于等于0并且小于10的整数)
 * Created by 石岩 on 14-12-31.
 */
@SuppressWarnings("UnusedDeclaration")
public class RandomDataUtil {
    public static String with(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(random.nextInt(10));
        }

        return stringBuffer.toString();
    }
}
