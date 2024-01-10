import os
import time
import math
import wikipediaapi
os.system('cls' if os.name == 'nt' else 'clear')





# INPUT ------------------------------------------------------------------------------------------------

# (change this to the directory you want the database to be saved in)
dir = "C:/Users/capta/Downloads/"

# (change this to the directory of the single Wikipedia dump file, should be a .xml)
filename = "C:/Users/capta/Downloads/wikidata.xml"


# ------------------------------------------------------------------------------------------------------





# create folders in directory for sorting
def addFolder(directory,folderName):
    path = os.path.join(directory,folderName) 
    os.mkdir(path) 
    return path
    
def addFolders(dr):
    for i in range(26):
        addFolder(dr,str(chr(97+i)).capitalize())
    for i in range(9):
        addFolder(dr,str(i+1))
    addFolder(dr,"other")

def addFoldersLayered(dr):
    tempdir = ""
    for i in range(26):
        tempdir = dr + "/" + str(chr(97+i)).capitalize()
        addFolders(tempdir)
    for i in range(9):
        tempdir = dr + "/" + str(i+1)
        addFolders(tempdir)

dir = addFolder(dir,"Wikirace Database")
saveFolderRedirects = addFolder(dir,"redirects")
addFolder(dir,"error")
addFolders(dir)
addFoldersLayered(dir)

addFolder(saveFolderRedirects,"error")
addFolders(saveFolderRedirects)
addFoldersLayered(saveFolderRedirects)

saveFolderArticles = dir

# benchmark test
t0 = time.time()
def speedTest(t_):
    stri = ""
    mm = 0
    tt = time.time()-t_
    if tt >= 10:
        tt = round(tt)
    if tt >= 60:
        mm = math.floor(tt / 60)
        tt = tt % 60
    if mm >= 1:
        stri = str(mm)+ " minutes elapsed"
    else:
        stri = str(tt) + " seconds elapsed"
    return(stri)


# loop var setup
line = ""
line2 = ""
index = 0
index2 = 0
title = ""   
title2 = ""
pageFile = 0
charSort = ""
word = ""
saveName = ""
listOfLinks = []
illegal_types = ["File:","Category:","Project:","Special:","s:","Wikipedia:","WP:","wikt:","Template:","User:","File:","Image:","User talk:","Help:",":en:","talk:",":Category:","c:","Wiktionary:"]
test = 0
illegal_characters = ['?','*',':','|','>','<','/','\\']
illegal_replacements = ['&quest;','&ast;','&colon;','&verbar;','&gt;','&lt;','&sol;','&bsol;']
category = False
saveFolder = saveFolderArticles
redirect = False
page_py = ""
url = ""

wiki_wiki = wikipediaapi.Wikipedia('WikiraceParse', 'en')

# loop
encd_type = "utf_8"
with open(filename, "r", encoding=encd_type) as file: 
    file.seek(0)
    line = file.readline() 
    while line: 

        # process line 
        line = file.readline()
        
        # find title (new file)
        index = line.find("<title>")
        if index != -1:
            index2 = line.index("</title>")
            title = line[index+7:index2]

            # check if title is in banned category
            category = False
            for i in illegal_types:
                if i in title:
                    category = True
                    break
            if category:
                continue

            # check if title is redirect
            file.readline()
            file.readline()
            line2 = file.readline()

            if ("<redirect" in line2):
                saveFolder = saveFolderRedirects
                redirect = True
            else:
                saveFolder = saveFolderArticles
                redirect = False

            # complete previous file
            if pageFile.closed == False:
                listOfLinks = set(listOfLinks)
                listOfLinks.discard("")
                for wrd in listOfLinks:
                    pageFile.write(wrd + "\n")
                listOfLinks = []
                pageFile.close()
            
            # find new page title
            charSort = title[0:1]
            if len(title) >= 2:
                charSort2 = title[1:2]
            else:
                charsort2 = ''

            # open new file, named after page
            if (charSort.isalpha() or charSort.isnumeric()) and (ord(charSort) <= 122):
                if (charSort2.isalpha() or charSort2.isnumeric()) and (ord(charSort2) <= 122) and (charSort2 != ''):
                    saveName = saveFolder+charSort.upper()+"/"+charSort2.lower()+"/"+title+".txt"
                else:
                    saveName = saveFolder+charSort.upper()+"/"+"other"+"/"+title+".txt"
            else:
                # special characters
                saveName = saveFolder+"other"+"/"+title+".txt"

            if os.path.exists(saveName):
                os.remove(saveName)

            try:
                pageFile = open(saveName, "w", encoding=encd_type)
            except:
                # replace illegal characters in page title
                for ele in range(0, len(title)):
                    char = title[ele]
                    if char in illegal_characters:
                        ele = 0
                        for ill in range(len(illegal_characters)):
                            title = title.replace(illegal_characters[ill],illegal_replacements[ill])
                        
                try: # Try to save the file again (with illegal characters removed)
                    # parse 
                    charSort = title[0:1]
                    if len(title) >= 2:
                        charSort2 = title[1:2]
                    else:
                        charsort2 = ''

                    # open new file, named after page
                    if (charSort.isalpha() or charSort.isnumeric()) and (ord(charSort) <= 122):
                        if (charSort2.isalpha() or charSort2.isnumeric()) and (ord(charSort2) <= 122) and (charSort2 != ''):
                            saveName = saveFolder+charSort.upper()+"/"+charSort2.lower()+"/"+title+".txt"
                        else:
                            saveName = saveFolder+charSort.upper()+"/"+"other"+"/"+title+".txt"
                    else:
                        # special characters
                        saveName = saveFolder+"other"+"/"+title+".txt"

                    if os.path.exists(saveName):
                        os.remove(saveName)

                    pageFile = open(saveName, "w", encoding=encd_type)
                except:
                    print('Error: file name "' + str(saveName)+'" is illegal')
                    pageFile = open(saveFolder+"error/"+str(time.time()-t0), "w", encoding=encd_type)
                    pageFile.write("ERROR NAME: "+saveName)

            # write redirect content
            if redirect:
                title2 = line2[line2.find('<redirect title="')+17:line2.find('" />')]
                # replace illegal characters in file content (title)
                for ele in range(0, len(title2)):
                    char = title2[ele]
                    if char in illegal_characters:
                        ele = 0
                        for ill in range(len(illegal_characters)):
                            title2 = title2.replace(illegal_characters[ill],illegal_replacements[ill])
                pageFile.write(title2)
                pageFile.close()


            # debug info
            test += 1
            if test % 10000 == 0:
                print(str(test)+" pages parsed ("+speedTest(t0)+")")


        # find link and add to list(file)
        index = line.find("[[")            
        if index != -1:
            for i in range(0,len(line)):
                word = ""
                if line[i:i+2] == "[[":
                    if line[i+2:i+7] == "File:" or line[i+2:i+8]=="Image:" or line[i+2:i+11] == "Category:" \
                        or line[i+2:i+10] == "Project:" or line[i+2:i+10] == "Special:" or line[i+2:i+4]=="s:"\
                        or line[i+2:i+12] == "Wikipedia:" or line[i+2:i+5] == "WP:" or line[i+2:i+7]=="wikt:"\
                        or line[i+2:i+11]=="Template:" or line[i+2:i+7]=="User:" or line[i+2:i+12]=="User talk:"\
                        or line[i+2:i+7]=="Help:" or line[i+2:i+6]==":en:" or line[i+2:i+7]=="talk:"\
                        or line[i+2:i+12]==":Category:" or line[i+2:i+4]=="c:" or line[i+2:i+13] == "Wiktionary:":
                       continue
                    index2 = line.find("]]",i+2)
                    index3 = line.find("|",i+2)
                    if index3 < index2 and index3 != -1:
                        word = line[i+2:index3]
                    else:
                        word = line[i+2:index2]
                    if word.find("#") != -1:
                        word = word[0:word.find('#')]

                    word = word.replace("&amp;","&")
                    word = word.replace("&quot;",'"')

                    listOfLinks.append(word)        

# END
if pageFile.closed == False:
    pageFile.close()
print()
print('PARSE COMPLETE!')
print(str(test)+" files parsed in total.")
print()
