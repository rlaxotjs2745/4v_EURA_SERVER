package com.eura.web.util;

public class CONSTANT {
    public static String version ="1.0";

    public static String success ="SUCCESS";
    public static String fail ="FAIL";

    public static Integer user_idx =0;
    public static Integer admin_idx =1;

    public static Integer yes = 1;
    public static Integer no = 0;

    public static Integer default_pageblock = 8;    // 페이징 기본 수량

    public static Integer keepalive = 5;
    public static Integer mdatasec = 10;
    public static String rurl = "/mt/";
    public static String REGEXPW = "(?=.*\\d{1,50})(?=.*[~`!@#$%\\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{10,20}$";
}
