@echo off
for /f "usebackq tokens=2 delims=:" %%f in (`ipconfig ^| findstr /c:"IPv4 Address"`) do (set Ipv4Address=%%f)
if not defined Ipv4Address (
    for /f "usebackq tokens=2 delims=:" %%f in (`ipconfig ^| findstr /c:"IPv4 地址"`) do (set Ipv4Address=%%f)
    if not defined Ipv4Address (
        Ipv4Address=localhost
    )
)
set Ipv4Address=%Ipv4Address: =%
echo 当前电脑的IP地址是：%Ipv4Address%
FOR /F "USEBACKQ" %%i IN (`jps -l ^| find /i "project-hope"`) DO (SET ProjectHopePid=%%i)
if defined ProjectHopePid (
	start "" http://%Ipv4Address%:8080/html/index.html
) else (
	start "" javaw -server -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Djava.net.preferIPv6Addresses=false -Duser.timezone=GMT+08 -Xmx2g -Xms2g -Xmn1G -Xss256K -XX:+HeapDumpOnOutOfMemoryError -jar project-hope-0.0.1-SNAPSHOT.jar
	timeout /t 10 /nobreak
	start "" http://%Ipv4Address%:8080/html/index.html
)
@REM pause