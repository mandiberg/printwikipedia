printwikipedia
==============

This code parses the Wikipedia database and creates print ready PDFs. 

The project is partially complete at present- it reads the database, and can output part of it to PDFs, but it crashes after 20% of the way through. 

See design-documents folder for an example of sample output, and the design mockup for the layout. 

Next Steps:

1. The core script needs stability and design improvements. The design is close, but not totally there, but more importantly, the script crashes about 20% through the database around "D" or "E." It needs better stability and error handling, and it needs to be able to be restarted when it encounters an error or skip the problem entries and keep going.

2. Covers: Each volume needs a print ready cover to be generated that has the correct start/end words (e.g. "Fredericksburg - Free Market"), and it needs to have the correct dimensions. This is mostly determined by the spine width, which will be kept consistent on all volumes, except the last one, which will be smaller. Could be done concurrently with the creation of the pdfs, or volume number, start/end words could be stored elsewhere, and the covers could be build by a separate (non java?!?) program afterwards

3. Uploading to CreateSpace: Is there an API to upload to CreateSpace? It doesn't seem like any of the major print on demand services have APIs, this will have to be done manually. Can it be done via mTurk? Or will it have to be done by myself or an assistant?
