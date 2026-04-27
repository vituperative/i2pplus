#!/bin/sh
#
# Generate translated man pages from the po files.
#
# can't get po4a to do this
#
# NOTE:
# For new translations, add the file names to debian/i2p.manpages and/or debian/i2p-router.manpages.
# Don't forget to check in those files and .tx/config .
# Don't forget to git add and commit new files in platform-specific/unix/man/ and platform-specific/unix/locale-man/ .

cd `dirname $0`
MAN_BASE=../installer/resources/platform-specific/unix
LOCALE_MAN_BASE=../installer/resources/platform-specific/unix/locale-man
for i in eepget i2prouter i2prouter-nowrapper
do
	for f in $LOCALE_MAN_BASE/man_*.po
	do
		j=${f%.po}
		j=${j#${LOCALE_MAN_BASE}/man_}
		po4a-translate -f man -m $MAN_BASE/man/$i.1 -p $LOCALE_MAN_BASE/man_$j.po -l $MAN_BASE/man/$i.$j.1 -L UTF-8 -M UTF-8 -k 10 -v -d
	        if [ $? -ne 0 ]
		then
			echo "********* FAILED TRANSLATE FOR $j $i *************"
			FAIL=1
		fi
	done
done
exit $FAIL
