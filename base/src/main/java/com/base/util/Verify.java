package com.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by GaoTing on 2018/3/3.
 *
 * Explain :验证
 */

public class Verify {

    private Verify(){}
    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean email(String email) {
        boolean flag;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证银行卡
     *
     * @param cardNumber
     * @return
     */
    public static boolean bankCard(String cardNumber) {
        return CheckBankCard.checkBankCard(cardNumber);
    }

    /**
     * 验证身份证
     *
     * @param idNumber
     * @return
     */
    public static boolean idCard(String idNumber) {
        return IDCard.verify(idNumber);
    }

    /**
     * 验证手机格式
     */
    public static boolean phoneNumber(String mobiles) {
        if (mobiles == null) return false;
        String telRegex = "[1][3456789]\\d{9}";
        return mobiles.matches(telRegex);
    }
}
