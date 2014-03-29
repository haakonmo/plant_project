VIDEOS=normal brush cacti trees

default: push

all: paper.pdf flv

flv: ${VIDEOS:%=video/%.flv}

push: all
	rsync -uav ./{index.htm,images,video,paper.pdf} \
		pileus.org:/home/pileus/htdocs/andy/alife/   

%.bbl: %.bib
	pdflatex $*
	bibtex   $*
	pdflatex $*

%.pdf: %.tex %.bbl
	pdflatex $*

%.flv: %.mp4
	ffmpeg -i $< -vcodec libx264 -y $@

clean:
	rm -f *.log *.aux *.bbl *.blg *.out *.pdf
