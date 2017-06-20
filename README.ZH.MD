flume-append-file-sink 
=========================

追加内容到文件末尾

依赖
------------
- Flume-NG >= 1.7
- Linux
- MacOS

构建
------------
```bash
$ mvn clean package
```

jar文件下载
------------
[flume-append-file-sink-1.0.jar](https://github.com/gojuukaze/flume-append-file-sink/releases)


配置参数
--------------

| Property Name       | Default | Description                                     |
| --------------------|:-------:| ------------------------------------------------|
| **sink.fileName**       |    -    | 文件名                                       |
| **sink.appendToolDir**  |    -    | 用来存储此sink所需要的工具的目录，不能删除 |
| **sink.batchSize**      |   100   | 一次提交多少消息   |


配置示例
----------------------
```shell
a1.sinks.k1.type = cn.ikaze.flume.sink.AppendFile
a1.sinks.k1.sink.fileName = /var/log/m.log
a1.sinks.k1.sink.appendToolDir = /home/gojuukaze/.append_file_tool
```

常见问题：
----------------------
- 字符显示乱码:  
  尝试更改系统的字符集,  
  栗子:  
  ```bash
  centos 7: 
  $ echo 'LANG="zh_CN.UTF-8"' >>/etc/locale.conf
  
  ```
  
