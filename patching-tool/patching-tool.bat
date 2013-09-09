@echo off

pushd "%~dp0"

java -Xmx1024m -Xms512m -XX:MaxPermSize=512m -XX:PermSize=256m -Dfile.encoding=UTF8 -jar lib\patching-tool.jar %*

popd

echo on