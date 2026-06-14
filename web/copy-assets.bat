@echo off
REM 从旧项目复制静态资源到前端 public 目录
set LEGACY_DIR=..\legacy
set PUBLIC_DIR=public

echo Copying static assets from legacy project...
for %%d in (img images css js upload fonts ffont newfont alifont erweicode) do (
    if exist "%LEGACY_DIR%\%%d" (
        xcopy /E /I /Y "%LEGACY_DIR%\%%d" "%PUBLIC_DIR%\%%d" > nul
        echo   %%d - done
    )
)
echo Done. Assets copied to web/public/
