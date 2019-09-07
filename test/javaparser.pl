#!/usr/bin/env perl

my $java = $ARGV[0];
my $ast = `java -jar ../javaparser.jar $java`;
print $ast;
