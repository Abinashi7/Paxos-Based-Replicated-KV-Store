JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	KeyValueCode.java \
	KeyValueStore.java \
	KeyValueReturnWrapper.java \
	KeyValueCoordinator.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
