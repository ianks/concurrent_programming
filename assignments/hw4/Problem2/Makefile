JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = $(wildcard *.java)

default: classes

test: classes
	java ConcurrentQueueTest
	$(MAKE) --no-print-directory clean

classes: $(CLASSES:.java=.class)

wake:
	ruby -e "1000.times { system 'make test' }"

clean:
	$(RM) *.class
