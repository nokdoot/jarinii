#!/usr/bin/env perl
use strict;
use warnings;
use Data::Dumper;

use FindBin;
use lib $FindBin::Bin . '/lib';

use Traverse::Directory;

my $td = Traverse::Directory->new("helloModule@!");

print $$td;
