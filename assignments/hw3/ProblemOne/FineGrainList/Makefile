JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = $(wildcard *.java)

default: classes

test: classes
	java TestFineGrainList

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
