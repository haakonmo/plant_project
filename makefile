-include config.mk

MAIN    := plant_project.Main

SOURCES := $(shell find src -name '*.java')
OBJECTS := $(subst .java,.class,$(SOURCES))
OBJECTS := $(subst src/,obj/,$(OBJECTS))

CLASSPATH := obj:src:lib/*

all: $(OBJECTS)

dist: jar
	cd jar; \
	jar cef $(MAIN) ../plant_project.jar *

run: $(OBJECTS)
	java -cp $(CLASSPATH) -ea -Xms64m -Xmx512m $(MAIN)

$(OBJECTS): $(SOURCES) makefile | obj
	javac -cp $(CLASSPATH) -Xlint:unchecked -d obj $(SOURCES)

clean:
	rm -rf obj

jar: $(OBJECTS)
	mkdir -p jar
	cp -a obj/plant_project jar
	cp lib/*.jar jar

obj:
	mkdir -p obj
