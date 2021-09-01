
本demo基于springboot框架，只演示简单流程,功能并不完善，帮助对接入weboffice服务构建简单的技术概念，方便java接入调试，细节请参考开发文档或go-demo

注意：正式项目中，尽量不要直接用文件名作为重要参数，因为字符编码等问题，容易导致签名不过等问题，建议用fileid等做为唯一标识。（可以参考go-demo）

# webofficedemo运行说明(当前版本1.0.9)
**1.设置配置文件application.properties**
>	server.port:     java-demo服务端口
>
>	domain:   金山文档在线编辑域名(不需要修改)
>
>	appid:    开发信息中的APPID
>
>	appkey:   开发信息中的APPKEY
>
>	download_host: 文件外网下载服务器.拼接下载地址用.(demo中文件下载地址为：download_host+"/weboffice/getFile?_w_fname=", 需要外网可访问)
>

**2.终端运行demo命令:**
>	cd webofficedemo
>
>	gradle clean build 构建出可运行jar包
>   把测试文件1.doc、2.xls、3.ppt放在与WebofficeDemo.jar包同级目录下，保证访问/weboffice/getFile?_w_fname=1.doc能下载到
>   java -jar (WebofficeDemo.jar的路径)运行demo
>
 

**3.打开浏览器输入地址:**
>	demoIP:端口/weboffice/index.html
>
>如 http://www.xxxx.com:19999/weboffice/index.html


