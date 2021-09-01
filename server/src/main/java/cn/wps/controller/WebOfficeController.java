package cn.wps.controller;

import cn.wps.model.FileModel;
import cn.wps.model.UserModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WebOfficeController {
    //// TODO: 参数传递最好不要出现中文等特殊字符，容易导致签名不过等问题，本例子用fileid与文件名做了一个映射，实际开发可以按情况处理
    private static Map<String, String> fileNameMap = new HashMap<String, String>();

    static {
        //映射的文件地址,我这里写的是本地E盘下的地址
        fileNameMap.put("1", "E:\\zwy\\中文.doc");
        fileNameMap.put("2", "E:\\zwy\\2.xls");
        fileNameMap.put("3", "E:\\zwy\\3.ppt");
        fileNameMap.put("4", "E:\\zwy\\test.doc");
    }

    @RequestMapping(value="/v1/3rd/file/info", method = RequestMethod.GET)
    @ResponseBody
    public Object fileInfo(@RequestParam("_w_fileid") String fileid) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        JSONObject file = new JSONObject();
        JSONObject user = new JSONObject();
        try {
            FileModel fileModel = new FileModel();
            File localFile = new File(fileNameMap.get(fileid));
            fileModel.size = localFile.length();
            //// TODO: 文件的id应该唯一
            file.put("id", fileid);
            file.put("name", fileNameMap.get(fileid));
            //// TODO: 文件的版本控制
            file.put("version", fileModel.version);
            //// TODO: 必须返回文件真实大小，服务端会检查
            file.put("size", fileModel.size);
            file.put("creator", fileModel.creator);
            file.put("modifier", fileModel.modifier);
            //// TODO: 下载链接中的参数如带中文等特殊字符，参数必须进行urlencode
            file.put("download_url", FileModel.download_url + fileid);
            jsonObject.put("file", file);
            UserModel userModel = new UserModel();
            user.put("id", userModel.id);
            user.put("name", userModel.name);
            user.put("permission", userModel.permission);
            user.put("avatar_url", userModel.avatar_url);
            jsonObject.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @RequestMapping(value="/v1/3rd/file/version/{version}", method = RequestMethod.GET)
    @ResponseBody
    public Object fileVersionInfo(@PathVariable("version") Long version, @RequestParam("_w_fileid") String fileid) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        JSONObject file = new JSONObject();
        JSONObject user = new JSONObject();
        try {
            FileModel fileModel = new FileModel();
            File localFile = new File(fileNameMap.get(fileid));
            fileModel.size = localFile.length();
            //// TODO: 文件的id应该唯一
            file.put("id", fileid);
            file.put("name", fileNameMap.get(fileid));
            //// TODO: 文件的版本控制
            file.put("version", version);
            //// TODO: 必须返回文件真实大小，服务端会检查
            file.put("size", fileModel.size);
            file.put("creator", fileModel.creator);
            file.put("modifier", fileModel.modifier);
            //// TODO: 下载链接中的参数如带中文等特殊字符，参数必须进行urlencode
            file.put("download_url", fileModel.download_url + fileid);
            jsonObject.put("file", file);
            UserModel userModel = new UserModel();
            user.put("id", userModel.id);
            user.put("name", userModel.name);
            user.put("permission", "write");
            user.put("avatar_url", userModel.avatar_url);
            jsonObject.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @RequestMapping(value="/v1/3rd/user/info", method = RequestMethod.POST)
    @ResponseBody
    public Object userInfo() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject user = new JSONObject();
        UserModel userModel = new UserModel();
        try {
            user.put("id", userModel.id);
            user.put("name", userModel.name);
            user.put("permission", userModel.permission);
            user.put("avatar_url", userModel.avatar_url);
            jsonArray.put(user);
            jsonObject.put("users", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @RequestMapping(value="/v1/3rd/file/online", method = RequestMethod.POST)
    @ResponseBody
    public void online() {
    }

    @RequestMapping(value = "/v1/3rd/file/save", method = RequestMethod.POST)
    @ResponseBody
    public Object save(@RequestParam("file") MultipartFile file, @RequestParam("_w_fileid") String fileid) {
        if (!file.isEmpty()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(fileNameMap.get(fileid))));
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //// TODO: 返回保存后的文件信息 特别是文件大小与版本信息要准确
        FileModel filemodel = new FileModel();
        filemodel.name = fileNameMap.get(fileid);
        filemodel.size = file.getSize();
        filemodel.version = filemodel.version + 1;
        return filemodel;
    }

    @GetMapping("/weboffice/getFile")
    public ResponseEntity<byte[]> getFile(@RequestParam("_w_fileid") String fileid) throws Exception {
        //// TODO: 处理文件下载，返回对应的文件，如果是接第三方存储，可以没有这个接口
        File file = new File(fileNameMap.get(fileid));
        InputStream inputStream = new FileInputStream(file);
        byte[] body = new byte[inputStream.available()];
        HttpHeaders headers=new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileNameMap.get(fileid), "utf-8"));
        inputStream.read(body);
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }
}
