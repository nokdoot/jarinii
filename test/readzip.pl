#!/usr/bin/env perl


use Archive::Zip qw( :ERROR_CODES :CONSTANTS );
my $zip = Archive::Zip->new('/usr/local/jdk-9.0.4/lib/src.zip');

my @memberNames = $zip->memberNames();


sub module{
	my $module_regex = qr{(^(\w*.)\w*\/)};
	return $1;
}


my $module;
my $i = 0;
foreach(@memberNames){
	my $module_inloop = module($_);	
	my $class

	########### 흠... src의 모든 클래스가 사용되는게 아니었음
	


	$i++;

}
