#!/bin/bash

if [ `whoami` != root ] ; then
	echo "Woah! Not so fast, need to be root!"
	exit 1
fi

if [ -d /usr/lib/qbar ] ; then
	echo "Wait, qbar is already installed, or at least the directory is still there (/usr/lib/qbar)"
	
	echo "Would you like to reinstall it? [Y/n]"
	read input
	
	while [[ ($input != Y && $input != n) ]] ; do
		echo "Y or n"
		read input
	done

	if [ $input == n ] ; then
		exit 0
	else
		rm -rv /usr/lib/qbar
		rm -v /usr/bin/qbar
	fi
fi

tar -xzvf qbar_package.tgz
mv -v qbar/ /usr/lib/

# ---------- Time to install the binary into the right directory ------------
cd /usr/bin

# need to sym-link to qbar, then we should be good.
ln -sv /usr/lib/qbar/qbar.sh qbar
chmod +x qbar

