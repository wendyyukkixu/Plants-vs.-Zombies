from os import listdir
from os import rename
from os.path import isfile, join
mypath = "E:\Comp Sci 12\Final\Images\Game\Zombies\Imp Zombie\Eating"
onlyfiles = [ f for f in listdir(mypath) ]#if isfile(join(mypath,f)) ]

count = 1

for f in onlyfiles:

    if f[-4:] == ".png":

        rename(f, "eatingimp%d.png" %count)
        count += 1
