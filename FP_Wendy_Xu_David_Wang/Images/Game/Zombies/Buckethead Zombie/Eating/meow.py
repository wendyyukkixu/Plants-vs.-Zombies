from os import listdir
from os import rename
from os.path import isfile, join
mypath = "E:\Comp Sci 12\Final\Images\Game\Zombies\Buckethead Zombe\Eating"
onlyfiles = [ f for f in listdir(mypath) ]#if isfile(join(mypath,f)) ]

count = 1

for f in onlyfiles:

    if f[-4:] == ".png":

        rename(f, "eatingbuckethead%d.png" %count)
        count += 1
