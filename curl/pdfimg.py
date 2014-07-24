from PIL import Image
from pyPdf import PdfFileWriter, PdfFileReader
from reportlab.pdfgen import canvas
from StringIO import StringIO

def placeBarcode(volumeNum):
	#for adding the barcode to the pdf cover.
	# Using ReportLab to insert image into PDF
	imgTemp = StringIO()
	imgDoc = canvas.Canvas(imgTemp)

	# Draw image on Canvas and save PDF in buffer
	imgPath = "/Users/wiki/repos/printwikipedia/curl/barcode.png"
	imgDoc.drawImage(imgPath, 175, 60, 190, 80)    ## at (399,760) with size 160x160
	imgDoc.save()
	volumeNum = str(volumeNum)
	input_cp = "/Users/wiki/repos/printwikipedia/dist/covers/volume"+volumeNum+".pdf"
	output_cp = "/Users/wiki/repos/printwikipedia/dist/covers/bar_volume"+volumeNum+".pdf"
	print volumeNum + " this is volnum"
	
	# Use PyPDF to merge the image-PDF into the template
	page = PdfFileReader(file(input_cp,"rb")).getPage(0)
	overlay = PdfFileReader(StringIO(imgTemp.getvalue())).getPage(0)
	page.mergePage(overlay)

	#Save the result
	output = PdfFileWriter()
	output.addPage(page)
	output.write(file(output_cp,"w"))
	
placeBarcode(58)