rm -f `find ./output -type f`
./scripts/compile.sh
java -Xms200m -Xmx200m -classpath ./backend/build/classes/java/main:./lib/vsdk.jar:./backend/build/runtime-libs/* entryPoints.CommandLineApp "$@" 2> e
if ls output/cleansh/*.sh >/dev/null 2>&1; then
    chmod 755 output/cleansh/*.sh
fi
