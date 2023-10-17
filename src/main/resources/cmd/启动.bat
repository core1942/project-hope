@echo off
FOR /F "USEBACKQ" %%i IN (`jps -l ^| find /i "project-hope"`) DO (SET ProjectHopePid=%%i)
if defined ProjectHopePid (
	start "" http://localhost:8080/html/index.html
) else (
	start "" javaw -server -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Djava.net.preferIPv6Addresses=false -Duser.timezone=GMT+08 -Xmx2g -Xms2g -Xmn1G -Xss256K -XX:+HeapDumpOnOutOfMemoryError -jar project-hope-0.0.1-SNAPSHOT.jar
	timeout /t 10 /nobreak
	for /f "usebackq tokens=2 delims=:" %%f in (`ipconfig ^| findstr /c:"IPv4 Address"`) do (set ipv4_address=%%f)
	start "" http://%ipv4_address%/html/index.html
)
@REM pause