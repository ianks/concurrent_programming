JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = $(wildcard *.java)

default: classes

test: classes
	java SavingsAccountTest
	java SavingsAccountPreferredTest
	$(MAKE) --no-print-directory clean

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
