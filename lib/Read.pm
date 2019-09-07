package Read;

use strict;
use warnings;

sub bnf
{
	local $/ = undef;
	open(my $regexfile, "<", "./java.bnf") or die "can't open ./java.bnf\n";
	<$regexfile>;
}

1;
