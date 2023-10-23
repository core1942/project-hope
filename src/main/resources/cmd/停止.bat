@echo off
cd /d %~dp0
FOR /F "USEBACKQ" %%i IN (`jps -l ^| find /i "project-hope"`) DO (SET ProjectHopePid=%%i)
if defined ProjectHopePid (
    @REM     taskkill F /pid %ProjectHopePid%
    start "" http://localhost:8080/stop
) else (
    echo Ã»ÓÐÆô¶¯
)
@REM pause