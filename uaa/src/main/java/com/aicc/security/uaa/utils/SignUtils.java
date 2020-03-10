package com.aicc.security.uaa.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.*;

public class SignUtils {

    //TODO 增加所有客户端，作为集合
    private static final String secretKey = "123456";
    private static final String appId = "client_2";

    public static void main(String[] args) {
        HashMap<String, String> signMap = new HashMap<>();
        String curtime = String.valueOf(new Date().getTime() / 1000);
        System.out.println(curtime);
        signMap.put("appId", appId);
        signMap.put("timestamp", curtime);
        System.out.println("签名:" + getSign(signMap, secretKey));
    }


    /**
     * 验证签名
     *
     * @param request
     * @return
     */
    public static Boolean checkSign(HttpServletRequest request) {
        Boolean flag = false;
        String requestAppId = request.getParameter("appId");//appid
        if (!appId.equals(requestAppId)) {
            throw new RuntimeException("appId is invalid");
        }
        String sign = request.getParameter("sign");//签名
        String timestamp = request.getParameter("timestamp");//时间戳
        //check时间戳的值是否在当前时间戳前后一小时以内
        String currTimestamp = String.valueOf(new Date().getTime() / 1000); // 当前时间的时间戳
        int currTimestampNum = Integer.parseInt(currTimestamp);
        int verifyTimestampNum = Integer.parseInt(timestamp); // 时间戳的数值
        // 在10分钟范围之外，访问已过期
        if (Math.abs(verifyTimestampNum - currTimestampNum) > 600) {
            throw new RuntimeException("Signature expired");
        }
        //检查sign是否过期
        Enumeration<?> pNames = request.getParameterNames();
        Map<String, String> params = new HashMap<>();
        while (pNames.hasMoreElements()) {
            String pName = (String) pNames.nextElement();
            if ("sign".equals(pName)) continue;
            String pValue = request.getParameter(pName);
            params.put(pName, pValue);
        }
        if (sign.equals(getSign(params, secretKey))) {
            flag = true;
        }
        return flag;
    }


    private static byte[] getMD5Digest(String data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data.getBytes("UTF-8"));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse);
        }
        return bytes;
    }


    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
            //sign.append(hex.toLowerCase());
        }
        return sign.toString();
    }


    /**
     * 生成签名
     *
     * @param params 参数集合不含secretkey
     * @param secret 验证接口的secretkey
     * @return
     */
    public static String getSign(Map<String, String> params, String secret) {
        String sign = "";
        StringBuilder sb = new StringBuilder();
        //step1：先对请求参数排序
        Set<String> keyset = params.keySet();
        TreeSet<String> sortSet = new TreeSet<String>();
        sortSet.addAll(keyset);
        Iterator<String> it = sortSet.iterator();
        //step2：把参数的key value链接起来 secretkey放在最后面，得到要加密的字符串
        while (it.hasNext()) {
            String key = it.next();
            String value = params.get(key);
            sb.append(key).append(value);
        }
        sb.append(secret);
        byte[] md5Digest;
        try {
            //得到Md5加密得到sign
            md5Digest = getMD5Digest(sb.toString());
            sign = byte2hex(md5Digest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sign;
    }


}
