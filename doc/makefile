all: paper.pdf

%.bbl: %.bib
	pdflatex $*
	bibtex   $*
	pdflatex $*

%.pdf: %.tex %.bbl
	pdflatex $*

clean:
	rm -f *.log *.aux *.bbl *.blg *.out *.pdf
