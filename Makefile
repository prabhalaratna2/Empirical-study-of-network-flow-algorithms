JFLAGS=-g
JC=javac
SOURCEDIR=./
SOURCES=$(shell find $(SOURCEDIR) -name '*.java')

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java


default: classes

classes: $(SOURCES:.java=.class)

.PHONY: clean
clean:
	rm -f *.class ||:	
