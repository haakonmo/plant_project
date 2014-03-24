-include config.mk

MAIN    := plant_project.Main
JARFILE := doc/plant_project.jar

SOURCES := $(shell find src -name '*.java')
OBJECTS := $(subst .java,.class,$(SOURCES))
OBJECTS := $(subst src/,obj/,$(OBJECTS))

CLASSPATH := obj:src:lib/*

all: $(OBJECTS) $(JARFILE)

run-java: $(OBJECTS)
	java -cp $(CLASSPATH) -ea -Xms64m -Xmx512m $(MAIN)

run-jar: $(JARFILE)
	java -ea -Xms64m -Xmx512m -jar $(JARFILE)

$(OBJECTS): $(SOURCES) makefile | obj
	javac -cp $(CLASSPATH) -Xlint:unchecked -d obj $(SOURCES)

$(JARFILE): $(OBJECTS)
	mkdir -p jar
	cp -a obj/plant_project jar
	cd jar; \
	jar cef $(MAIN) ../$(JARFILE) *

clean:
	rm -rf obj jar $(JARFILE)

obj:
	mkdir -p obj
