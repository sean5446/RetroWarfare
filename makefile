#
# Makefile
#

CC = javac

all: rwserver rwclient

rwserver: ./src/RWServer.java
	$(CC) -sourcepath ./src -d ./bin ./src/RWServer.java

rwclient: ./src/RWClient.java
	$(CC) -sourcepath ./src -classpath ./bin/jcurses/lib/jcurses.jar -d ./bin ./src/RWClient.java

rrwserver:
	java -Djava.net.preferIPv4Stack=true -classpath ./bin/jcurses/lib/jcurses.jar:./bin RWServer

rrwclient:
	java -Djava.net.preferIPv4Stack=true -classpath ./bin/jcurses/lib/jcurses.jar:./bin RWClient
