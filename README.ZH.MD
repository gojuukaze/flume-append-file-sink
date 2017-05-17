flume-append-file-sink 
=========================

jar文件下载
------------
[flume-append-file-sink-1.0.jar](https://github.com/gojuukaze/flume-append-file-sink/releases)


配置参数
--------------
- sink.fileName	        default:''	文件名
- sink.appendToolDir	default:''	用来存储此sink所需要的工具的目录，不能删除

配置示例
----------------------
```shell
a1.sinks.k1.type = cn.ikaze.flume.sink.AppendFile
a1.sinks.k1.sink.fileName = /var/log/m.log
a1.sinks.k1.sink.appendToolDir = /home/gojuukaze/.append_file_tool
```

