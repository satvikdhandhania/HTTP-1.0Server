JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	GetMime.java \
	Simple.java \
	Client.java \
	HandleRequest.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
