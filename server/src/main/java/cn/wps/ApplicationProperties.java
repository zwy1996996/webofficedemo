package cn.wps;

import cn.wps.model.FileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties implements CommandLineRunner {

    public static String appid = "";
    public static String appSecret = "";
    public static String domain = "";

    @Autowired
    public ApplicationProperties(
            @Value("${appid}") String appid,
            @Value("${appsecret}") String appSecret,
            @Value("${download_host}") String download_host,
            @Value("${domain}") String domain) {
        ApplicationProperties.appid = appid;
        ApplicationProperties.appSecret = appSecret;
        ApplicationProperties.domain = domain;
        //// TODO: 下载链接按实际情况增改参数
        if (download_host != null)
            FileModel.download_url = download_host + "/weboffice/getFile?_w_fileid=";
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
