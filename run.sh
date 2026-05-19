rm -f `find ./output -type f`
java -Xms200m -Xmx200m -classpath ./classes:./lib/vsdk.jar co.cubestudio.DebianAnalyzer $@ 2> e
chmod 755 output/cleansh/*.sh
#dot -Tdia output/general.dot -o output/general.dia

#-Xms1000m -Xmx1000m -Xss2000m
