package cn.wps.controller;

import cn.wps.ApplicationProperties;
import cn.wps.model.UrlModel;
import cn.wps.utils.FileUtil;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64String;

@RestController
public class OauthController  {

    //后缀名错误
    private static final String SUFFIX_FALSE = "0";

    @RequestMapping(value="/weboffice/url", method = RequestMethod.GET)
    //跨域
    @CrossOrigin
    @ResponseBody
    public Object getapp_Token(@RequestParam("_w_fileName") String fileName,@RequestParam("_w_downUrl") String downUrl,@RequestParam("_w_fileId") String fileId) throws UnsupportedEncodingException {
        UrlModel urlModel = new UrlModel();
        //获取后缀名
        String fileSuffix = FileUtil.getFileTypeByFileName(fileName);
        //后缀名校验
        if (SUFFIX_FALSE.equals(fileSuffix)){
            urlModel.success = "0";
            urlModel.msg = "检查后缀名是否存在";
            return urlModel;
        }
        //文件类型是一定需要的FileUtil.getFileTypeCode(fileSuffix)
        String url = ApplicationProperties.domain + "/office/" + FileUtil.getFileTypeCode(fileSuffix) + "/xxxx?" ;
        //// TODO: 注意：签名前，参数不要urlencode,要签名以后统一处理url编码，防止签名不过，带中文等字符容易导致签名不过，要注意签名与编成的顺序，最好不要带中文等特殊字符
        Map paramMap= new HashMap<String, String>();
        paramMap.put("_w_appid", ApplicationProperties.appid);
        paramMap.put("_w_downUrl",downUrl);
        paramMap.put("_w_fileName",fileName);
        paramMap.put("_w_fileId",fileId);

        String signature = getSignature(paramMap, ApplicationProperties.appSecret);
        url += getUrlParam(paramMap) + "&_w_signature=" + signature;

        //url就是在线文档的地址,可以直接打开这个url查看
        urlModel.wpsUrl = url;
        urlModel.token = "xxxx";
        urlModel.success = "1";
        urlModel.msg = "成功";
        return  urlModel;
    }

    private static String getUrlParam(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (builder.length() > 0) {
                builder.append('&');
            }
            builder.append(URLEncoder.encode(entry.getKey(), "utf-8")).append('=').append(URLEncoder.encode(entry.getValue(), "utf-8"));
        }
        return  builder.toString();
    }

    private static String getSignature(Map<String, String> params, String appSecret) {
        List<String> keys=new ArrayList();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            keys.add(entry.getKey());
        }

        // 将所有参数按key的升序排序
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        // 构造签名的源字符串
        StringBuilder contents=new StringBuilder("");
        for (String key : keys) {
            if (key=="_w_signature"){
                continue;
            }
            contents.append(key+"=").append(params.get(key));
            System.out.println("key:"+key+",value:"+params.get(key));
        }
        contents.append("_w_secretkey=").append(appSecret);

        // 进行hmac sha1 签名
        byte[] bytes= hmacSha1(appSecret.getBytes(),contents.toString().getBytes());
        //字符串经过Base64编码
        String sign= encodeBase64String(bytes);
        try {
            sign = URLEncoder.encode( sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(sign);
        return sign;
    }

    public static byte[] hmacSha1(byte[] key, byte[] data) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance(signingKey.getAlgorithm());
            mac.init(signingKey);
            return mac.doFinal(data);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
}
