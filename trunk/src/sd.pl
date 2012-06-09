#!/usr/bin/perl -w
use strict;
use warnings;

use Data::Dumper;
use File::Path qw(rmtree);
use Cwd qw(abs_path);
use LWP::UserAgent;


#default configuration
my $tomcat_home="/usr/local/apache-tomcat-6.0.20/";
my $hudson_host = 'tabmaster3.telenav.com';
my $hudson_port = '8080';
my $configfile="deployBS.config";
my $backup=0;
processArgs();

my $suffix_default="stage.us";
my $suffix_movie="stage-us";
my $WINDOWS_OS_TYPE = 'MSWin32';

my $TOMCAT_HOME_KEY='TOMCAT_HOME';
my $HUDSON_HOST_KEY = 'HUDSON_HOST';
my $BACKUP_KEY='BACKUP';

#@bservers is the container of bserver reference of hash. Every bserver is put in hash.
my @bservers = ();

#1. get bservers and versions from config file
open CONFIG, "<" , $configfile;

while (<CONFIG>) {
	chomp;
	s/^\s+//; s/\s+$//; #remove first and last whitespace
	next unless /^[^#].+/;
	my($bserver, $value) = split /=/;
	
	my %the_bserver = ();
	my ($suffix, $version);
	if($value =~ /(.+):(.+)/){
		($the_bserver{version},$suffix) = ($1,$2);
	}else{
		$suffix = $value;
	}
	
	if($bserver eq $TOMCAT_HOME_KEY){
		$tomcat_home = $suffix;
		next;
	}
	if($bserver eq $HUDSON_HOST_KEY){
		$hudson_host = $suffix;
		next;
	}
	if($bserver eq $BACKUP_KEY){
		$backup = $suffix;
		next;
	}
	
	if($bserver =~ /(.+):(.*):(.+)/){
		
		$the_bserver{name} = $1;
		if( $2 eq "" ){
			$the_bserver{hudson_name} = $1;
		}else{
			$the_bserver{hudson_name} = $2;
		}
		$the_bserver{ivy_name} = $3;
		
	}elsif($bserver =~ /(.+):(.+)/){
		$the_bserver{name} = $1;
		$the_bserver{hudson_name} = $2;
		$the_bserver{ivy_name} = $2;
	}else{
		$the_bserver{name} = $bserver;
		$the_bserver{hudson_name} = $bserver;
		$the_bserver{ivy_name} = $bserver;
	}
	
	if( defined $suffix ){
		$the_bserver{suffix} = $suffix;
	}else{
		if( $bserver =~ /^movie/ ){
			$the_bserver{suffix} = $suffix_movie;
		}else{
			$the_bserver{suffix} = $suffix_default;
		}
	}
	
	#only if name exist, we add the bserver into bservers
	if( exists $the_bserver{name} ){
		push @bservers, \%the_bserver;
	}
}
my $hudson_home="http://$hudson_host:$hudson_port/repository/telenav";
checkEnv();
printTips('Configuration');
print "Tomcat Home: $tomcat_home\n";
print "Hudson Server: $hudson_host:$hudson_port\n";
print "Configuration File: $configfile\n";
print "Backup: ".($backup?'true':'false')."\n";

print "[Applications]\n";
for my $bserver_ref (@bservers){
	if( defined($bserver_ref->{version}) ){
		printf "%s(hudson-name: %s, ivy-name: %s, version: %s) = %s\n", $bserver_ref->{'name'}, $bserver_ref->{'hudson_name'}, $bserver_ref->{'ivy_name'}, $bserver_ref->{version}, $bserver_ref->{'suffix'};
	}else{
		printf "%s(hudson-name: %s, ivy-name: %s) = %s\n", $bserver_ref->{'name'}, $bserver_ref->{'hudson_name'}, $bserver_ref->{'ivy_name'}, $bserver_ref->{'suffix'};
	}
}

#2. get the newest war version from hudson server
printTips("get bserver versions from $hudson_host:$hudson_port");
for my $bserver_ref (@bservers){
	unless ( defined($bserver_ref->{'version'}) ){
		$bserver_ref->{'version'} = &getVersion($bserver_ref->{'hudson_name'});
	}
}

#3. download wars from hudson server
printTips("download bserver wars from $hudson_host:$hudson_port");
for my $bserver_ref (@bservers){
	downloadwar($bserver_ref->{'ivy_name'},$bserver_ref->{'name'},$bserver_ref->{'version'},$bserver_ref->{'suffix'});
}

#4. kill tomcat
printTips("try to kill tomcat");
killtomcat();

#5. copy the newest wars to tomcat webapps directory
printTips("update bsrver war files to $tomcat_home");
for my $bserver_ref (@bservers){
	updatewar($bserver_ref->{'name'});
}

#6. startup tomcat
printTips("try to start tomcat");
starttomcat();

#++++++++++++++++++++++++++++++++++sub functions++++++++++++++++++++++++++++++++++++++++++++++++++++++++
sub processArgs {
	for(my $i=0; $i<@ARGV; $i++){
		if( $ARGV[$i] eq '-c' ){
			if( defined $ARGV[$i+1] ){
				$configfile = $ARGV[$i+1];
				last;
			}
		}
	}
}

sub checkEnv {
	die "Tomcat home[$tomcat_home] doesn't exist!" unless -e $tomcat_home;
	die "Tomcat home[$tomcat_home] is not a directory!" unless -d $tomcat_home;
	die "Tomcat home[$tomcat_home] is not a tomcat directory!" unless -e $tomcat_home."/bin/startup.sh";
	#TODO check whether the hudson server is connected
}

sub downloadwar{ 
	my ($bservername_in_ivy,$bserver,$version,$suffix) = @_;
	$|++;
	print "downloading $bserver-$version-$suffix.war ... ";
    my $url = '';
	if($bserver eq 'telenavCServer'){
		$url = "$hudson_home/dim/$version/$bserver-$version-$suffix.war";
	}else{
		$url = "$hudson_home/$bservername_in_ivy/$version/$bserver-$version-$suffix.war";
	}
	
	my $ua = LWP::UserAgent->new;
	my $response = $ua->get($url,":content_file" => $bserver.".war");
	unless($response->is_success){
		die "\nFailed when downloading $bserver-$version-$suffix.war\nwar file URL: $url\nStatus:".$response->status_line."\n";
	}
	die "fail!" unless -e $bserver.".war";
	print "success!\n";
}

sub updatewar{ 
	my $bserver = shift;
	die "\$bserver is a empty string\n" if $bserver =~ /^\s*$/;
	my $dest_war = "$tomcat_home/webapps/$bserver.war";
	my $dest_folder = "$tomcat_home/webapps/$bserver/";
	#backup war
	if($backup){
		rename $dest_war, $dest_war.".".&getDateTimeStr();
	}
	if( -e $dest_war ){
		unlink $dest_war or die "Could not delete file $dest_war: $!\n";
		print "delete $dest_war success.\n";
	}
	if( -e $dest_folder ){
		rmtree($dest_folder) or die "Could not delete directory $dest_folder: $!\n";
		print "delete $dest_folder success.\n";
	}
	rename "$bserver.war", "$dest_war";
	print "move file $bserver.war to $dest_war\n\n";
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
			} elsif ($tokens[$i] =~ /\-Dcatalina.home/) {
				my $catalina_home = (split /=/, $tokens[$i])[1];
				my $hard_link = findHardLink($catalina_home);
				if( defined $hard_link ){
					if( -l $catalina_home){
						print "find tomat: ".$catalina_home."-->".$hard_link."\n";
					}
					else{
						print "find tomat: ".$catalina_home."\n";
					}
					if($hard_link =~ /$tomcat_home/ || $hard_link."/" =~ /$tomcat_home/){
						$pid = $tokens[1];
						last;
					}
				}else{
					print "can't find hardlink for ".$catalina_home."\n";
				}
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
		if( check_dep_software('ntsd') ){
			system "ntsd -c q -p $pid";
			system "sleep 2";
			print "kill tomcat[$pid] success\n";
		}else{
			print "WARN: can't find ntsd, so I can't stop tomcat for you!!!\n";
		}
	}
	
	print "Tomcat[$tomcat_home] is stopped before.\n" if $pid == -1;
}

sub check_dep_software {
	my $software_name = shift;
	my $output=`$software_name 2>&1`;
	if($output =~ /^.$software_name./ ){
		return 0;
	}
	return 1;
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

sub getVersion{
	my $bserver = shift;
	if($bserver eq 'movie'){
		$bserver = 'tn60-movie';
	}
	my $versionRequestUrl = "http://$hudson_host:$hudson_port/hudson/view/TN60-CServer/job/".$bserver."/lastSuccessfulBuild/consoleText";
	
	my $ua = LWP::UserAgent->new;
	my $filename = 'consoleText.txt';
	my $response = $ua->get($versionRequestUrl,":content_file" => $filename);
	unless($response->is_success){
		die "Failed when get version of $bserver.\nVesion file URL: $versionRequestUrl\nStatus: ".$response->status_line."\n"; 
	}
	return parseConents4Version($bserver,"",$filename,$versionRequestUrl);
}

sub parseConents4Version(){
	print "debug";
	my ($bserver, $wget_output, $filename, $versionRequestUrl) = @_;
	my $version;
	open VERSION_FILE, $filename or die "Could not open $filename: $!\n";
	my $contents;
	grep {$contents .= $_} <VERSION_FILE>;
	close VERSION_FILE;
	unlink $filename or die "Could not delete $filename: $!\n";
	
	my @regular_exps = ("\\[echo\\] TAB\\(0.5\\): Applying tag (\\w*-?[^-]+)","(snapshot-\\d+)", "\\[echo\\] TAB\\(0.7\\): Applying tag (\\w*-?[^-]+)");
	my $isFind = 0;
	foreach my $regular_exp (@regular_exps){
		#print '$regular_exp='.$regular_exp."\n";
		$_ = $contents;
		if( /$regular_exp/s ){
			$version = $1;
			print "$bserver version: $version\n";
			$isFind = 1;
			last;
		}
	}
	unless( $isFind ){
		print $wget_output;
		#print $contents;
		die "Error: can't find version of $bserver using url $versionRequestUrl\n";
	}
	return $version;
}

sub isWgetSuccess {
	my $output = shift;
	if( $output =~ /200 OK.*`(.*)' saved/s ){
		die "Error: filename '$!' is so strange!\n" unless( $1 );
		return $1;
	}else{
		return 0;
	}
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

sub getDateTimeStr{
	my ($sec,$min,$hour,$day,$month,$yr19,@rest) =  localtime(time);#######To get the localtime of your system
	return "".($yr19+1900)."-".++$month."-".$day."^".sprintf("%02d",$hour)."-".sprintf("%02d",$min)."-".sprintf("%02d",$sec);
}

sub findHardLink {
	my $src = $_[0];
	return unless -e $src;
	return $src unless -l $src;
	
	my $dest = readlink($src);
	if( defined $dest ){
		$dest = abs_path($dest);
		#print $dest."\n";
	}else{
		return;
	}
	return findHardLink ($dest);
}
