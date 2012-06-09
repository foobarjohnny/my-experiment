#!/usr/bin/perl
use strict;
use warnings;

use Data::Dumper;
use File::Path qw(rmtree);

#default configuration
my $tomcat_home="/home/tnuser/apache-tomcat-6.0.18/";
my $WINDOWS_OS_TYPE = 'MSWin32';
&checkEnv();

print "Tomcat Home: $tomcat_home\n";

#4. kill tomcat
&printTips("try to kill tomcat");
killtomcat();

#6. startup tomcat
&printTips("try to start tomcat");
starttomcat();


sub checkEnv(){
	die "Tomcat home[$tomcat_home] doesn't exist!" unless -e $tomcat_home;
	die "Tomcat home[$tomcat_home] is not a directory!" unless -d $tomcat_home;
	#TODO check whether the hudson server is connected
}

sub killtomcat{
	my $os = $^O;
	print "your operating system type is $os\n";
	if( $os =~ /$WINDOWS_OS_TYPE/){
		#die "Could not stop tomcat on windows\n";
		&killtomcat_windows();
	}else{
		&killtomcat_linux();
	}
}

sub killtomcat_linux{
	chomp(my @processes = `ps -ef | grep tomcat`);
	my $pid = -1;

	foreach(@processes){
		#print "$_\n";
		my @tokens = split;
		for(my $i=8; $i<@tokens; $i++){
			if( $tokens[$i] =~ /$tomcat_home/ ){
				$pid = $tokens[1];
				last;
			}
		}
		last if $pid != -1;
	}

	if( $pid != -1 ){
		print "stop tomcat[$pid]...\n";
		system "kill -9 $pid";
		system "sleep 2";
		print "stop success!\n";
	}else{
		print "Tomcat[$tomcat_home] is stopped before.\n";
	}
}

sub killtomcat_windows{
	chomp(my @output = `tasklist -V | grep java`);
	my $index =0;
	my $pid = -1;
	foreach(@output){
		#print $index++.":  ".$_;
		next unless /Tomcat/;
		my @tokens = split;
		#print "\@tokens = @tokens\n";
		$pid = $tokens[1];
		system "ntsd -c q -p $pid";
		system "sleep 2";
		print "kill tomcat[$pid] success\n";
	}
	
	print "Tomcat[$tomcat_home] is stopped before.\n" if $pid == -1;
}

sub starttomcat{
	my $os = $^O;
	print "your operating system type is $os\n";
	my $startup_script;
	if( $os =~ /$WINDOWS_OS_TYPE/ ){
		$startup_script = "$tomcat_home/bin/startup.bat";
	}else{
		$startup_script = "$tomcat_home/bin/startup.sh";
	}
	system "$startup_script";
	#TODO check whether the tomcat process exists.
	print "start success.\n";
}

sub printTips{
	print "\n";
	my $total_len = 80;
	my $other_ch = '+';
	my $msg = shift;
	my $len = length $msg;
	
	my $other_len = $total_len  - $len;
	my $first_half = int($other_len/2);
	my $second_half = $other_len-$first_half;
	for(my $i=0; $i<$first_half; $i++){
		print $other_ch;
	}
	print $msg;
	for(my $i=0; $i<$second_half; $i++){
		print $other_ch;
	}
	print "\n";
}
