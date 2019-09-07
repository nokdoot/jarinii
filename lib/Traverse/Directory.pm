package Traverse::Directory;

use strict;
use warnings;

sub new{
	my ($class, $dir_name) = @_;
	$value = $dir_name;
	bless  \$dir_name , $class;
}

1;
