#!/bin/bash

if [ -e /proc/$$/fd/255 ]; then
    scriptpath=`readlink /proc/$$/fd/255 2>/dev/null`
fi

if [ ! -n "$scriptpath" ]; then
    scriptpath="$0"
fi

cd `dirname "$scriptpath"`

java -Xmx1024m -Xms512m -XX:MaxPermSize=512m -XX:PermSize=256m -Dfile.encoding=UTF8 -jar lib/patching-tool.jar $*