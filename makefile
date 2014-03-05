-include config.mk

MAIN    := plant_project.Main

SOURCES := $(shell find src -name '*.java')
OBJECTS := $(subst .java,.class,$(SOURCES))
OBJECTS := $(subst src/,obj/,$(OBJECTS))

CLASSPATH := obj:src:lib/*

all: $(OBJECTS)

run: $(OBJECTS)
	java -cp $(CLASSPATH) -ea -Xms64m -Xmx512m $(MAIN)

$(OBJECTS): $(SOURCES) makefile | obj
	javac -cp $(CLASSPATH) -d obj $(SOURCES)

clean:
	rm -rf obj

obj:
	mkdir -p obj
