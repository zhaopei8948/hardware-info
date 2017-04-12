用java 语言 + springboot + springmvc + (maven/gradle) 搭成的框架

1.用maven启动命令是
	mvn spring-boot:run

2.用gradle启动的命令是
	gradle bootRun

3.项目用的jar包是sigar.jar 用下面命令安装把jar包安装到本地
```
mvn install:install-file -DgroupId=org.hyperic -DartifactId=sigar -Dversion=1.6.4 -Dfile=Downloads/hyperic-sigar-1.6.4/sigar-bin/lib/sigar.jar -Dpackaging=jar -DgeneratePom=true
```
4.此项目主要用java获取本地硬件信息

5.启动时需要修改下面路径为自己的共享库文件路径
```
<jvmArguments>
-Djava.library.path=/Users/zhaopei/Downloads/hyperic-sigar-1.6.4/sigar-bin/lib
</jvmArguments>
```

6.启动后输入下面地址可查询内存相关信息,全部是JSON格式
```
http://localhost:18948/memInfo
```

7.打成jar包后，可以用下面命令启动
```
java -Djava.library.path=C:\zhaopei\hardware-info -Dfile.encoding=UTF-8 -jar hardware-info-1.0.jar
```
