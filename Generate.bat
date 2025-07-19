del /q bin
mkdir bin

javac -Xlint:unchecked -d bin ./src/*
jar -cmf manifest.mf util.jar -C bin .

pause