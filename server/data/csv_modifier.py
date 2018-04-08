import csv
import sys

if len(sys.argv) < 2:
    print("missing arg")
    exit(1)

with open(sys.argv[1]) as csvfile:
    with open("modified_" + sys.argv[1], "wb") as csvwriter:
        reader  = csv.reader(csvfile, delimiter=',', quotechar='"')
        writer  = csv.writer(csvwriter, delimiter=',', quotechar='"')
        index = 0
        for row in reader:
            row["id"] = index
            writer.writerow(row)

