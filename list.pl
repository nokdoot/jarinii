#!/usr/bin/env perl

use strict;

use Archive::Zip;

my $zipFile = $ENV{'JAVA_HOME'}.'/lib/src.zip';

my $zip = Archive::Zip->new();

$zip->read($zipFile);

my @files = $zip->memberNames();  # Lists all members in archive

my $filename = $files[-2];
print ref($filename), "\n";
print "xyz.txt contains " . $zip->contents( $filename );

