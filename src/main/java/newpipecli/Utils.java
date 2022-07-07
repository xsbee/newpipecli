package newpipecli;

import java.util.Collection;
import java.util.Optional;

public class Utils {
	public static<T> Optional<String> toString(CharSequence delim, Collection<T> stuff) {
		return stuff.stream()
			 .map(Object::toString)
			 .reduce((a, b) -> String.join(delim, a, b));
	}
}
