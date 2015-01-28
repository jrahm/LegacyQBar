#!/bin/sh
interpret=0;

is_interpreted() {
	for i in $@ ; do
		if [ "$i" == "-i" ] ; then
			interpret=1;
			break
		fi
	done;
}

is_interpreted;

if [ $interpret ] ; then
	qb_dir=/home/`whoami`/
	if [ ! -e $qb_dir ] ; then
		history="";
	else
		qb_dir=$qb_dir/.qbar
		if [ ! -e $qb_dir ] ; then
			mkdir $qb_dir
		fi

		qb_hist_file=$qb_dir/qb_history
		if [ ! -e $qb_hist_file ] ; then
			touch $qb_hist_file
		fi

		history="-H $qb_hist_file"
	fi

	rlwrap $history -c -f /usr/lib/qbar/aux/completions.txt java -jar /usr/lib/qbar/lib/QBarInterpreter.jar -p /usr/lib/qbar $@
else
	java -jar /usr/lib/qbar/lib/QBarInterpreter.jar -p /usr/lib/qbar $@
fi
