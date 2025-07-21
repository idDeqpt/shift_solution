del /q bin
mkdir bin

javac -d bin ./src/*
jar -cmf manifest.mf util.jar -C bin .

pause
