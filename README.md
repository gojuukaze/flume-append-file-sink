flume-append-file-sink 
=========================

[中文README.MD](https://github.com/gojuukaze/flume-append-file-sink/blob/master/README.ZH.MD)

Download Jar
------------
[flume-append-file-sink-1.0.jar](https://github.com/gojuukaze/flume-append-file-sink/releases)


Configuration：
--------------

| Property Name       | Default | Description                                     |
| --------------------|:-------:| -----------------------------------------------:|
| sink.fileName       |    -    | file name                                       |
| sink.appendToolDir  |    -    | the path which the append tool will be saved to |

Configuration Example：
----------------------
```shell
a1.sinks.k1.type = cn.ikaze.flume.sink.AppendFile
a1.sinks.k1.sink.fileName = /var/log/access.log
a1.sinks.k1.sink.appendToolDir = /home/gojuukaze/.append_file_tool
```


