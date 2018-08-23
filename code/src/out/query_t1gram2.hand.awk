# example run
# awk -f query_t1gram2.hand.awk ../data/t1gram.csv

BEGIN {
    FS = "\t"; OFS = ",";
    print "Phrase,Year,MatchCount,VolumeCount"
}

$1 == "Auswanderung" {
    $1 = $1 # triggers use of OFS instead of FS
    print $0
}
