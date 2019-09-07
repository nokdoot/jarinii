use strict;
use warnings;
use 5.010;
 
use JSON::MaybeXS qw(encode_json decode_json);
 
my $student_json = <>; 
 
my $student = decode_json $student_json;
 
use Data::Dumper;
print Dumper $student;
