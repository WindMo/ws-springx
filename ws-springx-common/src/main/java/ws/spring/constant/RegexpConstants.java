package ws.spring.constant;

/**
 * 常用正则
 * <p>附：<a href="https://c.runoob.com/front-end/854/">菜鸟教程正则表达式在线</a>
 *
 * @author WindShadow
 * @version 2022-08-27.
 */

public interface RegexpConstants {

    /** 【UUID】正则表达式 */
    String REGEXP_UUID = "^[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}$";

    /** 【IPv4】正则表达式 */
    String REGEXP_IP_V4 = "^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$";

    /** 【MAC地址】正则表达式 */
    String REGEXP_MAC = "^([0-9a-fA-F]{2}-){5}[0-9a-fA-F]{2}$";

    /** 【Linux文件权限表达式】正则表达式，匹配如"777"或"rwx-r--r--"代表Linux文件权限的字符串 */
    String REGEXP_LINUX_MODE_EXPRESSION = "^([0-7]{3})|(([r|R|-][w|W|-][x|X|-]){3})$";

    /** 【中文、字母、数字、下划线】正则表达式 */
    String REGEXP_CHINESE_LETTER_DIGIT_UNDERLINE = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";

    /** 【电话号码】正则表达式 */
    String REGEXP_PHONE_NUMBER = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";

    /** 【中国身份证号】正则表达式 */
    String REGEXP_CHINA_ID_NUMBER = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
}