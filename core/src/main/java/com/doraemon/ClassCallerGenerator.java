package com.doraemon;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/6 23:14
 **/
public class ClassCallerGenerator {


    public static void main(String[] args) {
        // String str = "org/springframework/asm/ClassReader";
        // String str = "javassist/ClassClassPath";
        // String str = "org/hibernate/boot/archive/scan/spi/ClassFileArchiveEntryHandler";
        // String str = "com/perfma/doraemon/GG";
        // String str = "javassist/bytecode/ClassFile";
        String str = "com/alibaba/fastjson/asm/ClassReader";
        int[] ret = string2intArray(str);
        for (int i = 0; i < ret.length; ++i) {
            System.out.print(ret[i] + ",");
        }
        // 15,18,7,53,19,16,18,9,14,7,6,18,1,13,5,23,15,18,11,53,1,19,13,53,29,12,1,19,19,44,5,1,4,5,18
    }

    static char alphabet[] = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z', '/', '\0'
    };

    /**
     * 数字数组中的值，为字母表下标值+1，如 a 对应的下标为 0 ，则实际值为 1
     *
     * @param str 被转换的字符串
     * @return
     */
    static int[] string2intArray(String str) {
        int[] ret = new int[str.length()];
        for (int i = 0; i < str.length(); ++i) {
            for (int j = 0; j < alphabet.length; ++j) {
                if (str.charAt(i) == alphabet[j]) {
                    ret[i] = j + 1;
                    break;
                }
            }
        }

        return ret;
    }
}
