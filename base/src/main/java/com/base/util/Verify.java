package com.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by GaoTing on 2018/3/3.
 *
 * @TODO :验证
 */

public class Verify {
    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean Email(String email) {
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
    public static boolean BankCard(String cardNumber) {
        return CheckBankCard.checkBankCard(cardNumber);
    }

    /**
     * 验证身份证
     *
     * @param IDNumber
     * @return
     */
    public static boolean IDCard(String IDNumber) {
        return IDCard.Verify(IDNumber);
    }

    /**
     * 验证手机格式
     */
    public static boolean PhoneNumber(String mobiles) {
        if (mobiles == null) return false;
        String telRegex = "[1][3456789]\\d{9}";
        return mobiles.matches(telRegex);
    }
}
