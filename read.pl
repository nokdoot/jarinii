#!/usr/bin/env perl

use warnings;
use strict;

use Data::Dumper;
use Term::ANSIColor;

my $javafilename = "./test.java";
my $javastruct;

my $packregex = qr/(package)(\s*)((\w*)(.))*(\s*);/; 
sub packfunc{
	my ($line, $struct) = @_;
	trimboth($line);
	chop $line;
	$struct->{"package"} = $line;
}

my $importregex = qr/(import)(\s*)((\w*)(.))*(\s*);/; 
sub importfunc{
	my ($line, $struct) = @_;
	trimboth($line);
	chop $line;
	push @{$struct->{"imports"}}, $line;
}

my $commentregex = qr!(?<slash_aster>\/\*.*)|(?<doubleslash>\/\/)!;
my $commentend = qr!.*\*/!;
sub commentfunc{
	my ($line, $matchedhash, $file) = @_;
	if(exists $$matchedhash{"slash_aster"}){
		printline ("comment start with : \n\t$line\n", "bright_red");
		while(<$file>){
			if($_ =~ $commentend){
				printline ("comment end with : \n\t$_\n", "bright_red");	
				last;
			}
		}
	}elsif(exists $$matchedhash{double}){
		#skip
	}
}

my $classregex = qr/
					(?<modifier>public)(\s+)(class)(\s+)(?<classname>\w+)
					((\s+)(extends(\s+)(\w+)))?
					((\s+)(implements(\s+)(\w+)(,\s*\w+)*))?
				/x;

sub getJavaClass{
	my ($lines, $file) = @_;
	
	my $blockregex = qr/(
					\{
						(
							(?:
								[^\{\}]*+
								|
								(?1)
							)*
						)
					\}
				)/xm;
					
	while(<$file>){
		next if $_ =~ s/^\s*$//;
		$lines .= $_;
		last if $lines =~ /^\s*($classregex\s*$blockregex)/; 
	}

	return $lines;
}

sub classfunc{
	my ($line, $matchedhash, $file, $struct) = @_;
	
	my $javaclass = getJavaClass($line, $file);


	my $classstruct = $struct->{classes}->{$$matchedhash{classname}} = {};

	(my $javaclassbody = $javaclass) =~ s/$classregex//;
	$classstruct->{body} = "\$javaclassbody"; ## 변수, 메서드 추가되면 사라질 예정

	$classstruct->{modifier} = $$matchedhash{modifier};

	open (my $fileofclass, "<", \$javaclassbody);
	read_line($fileofclass, $struct->{classes}->{$$matchedhash{classname}});
}

my $methodregex = qr//;
sub methodfunc{
	my ($line, $matchedhash, $struct) = @_;
	#my $javamethod = getJavaMethod($line, 
}


my $jstrctable = {
	argslist => {
		line => undef,
		struct => undef,
		matchedhash => undef,
		file => undef,
		color => "bright_magenta"
	},
	$packregex => { func => \&packfunc, args => [ qw[line struct] ] },
	$importregex => { func => \&importfunc, args => [ qw[line struct] ] },
	$classregex => { func => \&classfunc, args => [ qw[line matchedhash file struct] ] },
	$commentregex => { func => \&commentfunc, args => [ qw[line matchedhash file] ] },
	#$methodregex => {func => \&methodfunc, args => [ qw[line mathcedhash struct] ] },
	DEFAULT => { func => \&printline, args=> [ qw[line color] ] }
};

sub printline{
	my ($line, $color) = @_;
	print color($color), $line, color("reset");
}


sub trimboth{ 
	$_[0] =~ s/^\s+/^/;
	$_[0] =~ s/\s+$/\$/;
}

sub syntaxType{
	my ($line, $file, $struct) = @_;
	$jstrctable->{argslist}->{line} = $line;
	for my $regex(keys %$jstrctable){
		if($line =~ $regex){
			my $args = $jstrctable->{$regex}{args};
			$jstrctable->{argslist}->{matchedhash} = \%+ if grep{$_ eq 'matchedhash'} @$args;
			$jstrctable->{argslist}->{file} = $file if grep{$_ eq 'file'} @$args;
			$jstrctable->{argslist}->{struct} = $struct if grep{$_ eq 'struct'} @$args;

			$jstrctable->{$regex}{func}(@{$jstrctable->{argslist}}{ @{ $args } });
			return;
		}
	}
	#$jstrctable->{DEFAULT}{func}($line, "bright_magenta");
}

sub read_line{
	my ($file, $struct) = @_;

	while(<$file>){
		syntaxType ($_, $file, $struct);
	}

}

open (my $javafile, $javafilename) or return;
$javastruct->{filename} = $javafilename;
read_line $javafile, $javastruct;

print "\n";
print Dumper($javastruct);
