package server;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProtocolParser {
    public static HashMap<String, String> parse(String line) {
		HashMap<String, String> g = new HashMap<>();
		Arrays.stream(line.split(",")).map(s -> s.split(":")).forEach( i -> g.put(i[0].trim(), i[1].trim()) );
		return g;
	}
    
    public static List<HashMap<String, String>> getList(HashMap<String, String> map, String field) {
    	if (!map.containsKey(field + "_count")) {
    		throw new NoSuchElementException();
    	}
    	int count = Integer.parseInt(map.get(field + "_count"));
    	return IntStream.range(0, count).mapToObj((int i) -> {
    		HashMap<String, String> im = new HashMap<>();
    		String prefix = field + "_" + i;
			map.keySet().stream().filter((t) -> t.startsWith(prefix)).forEach((k) -> {
				im.put(k.substring(prefix.length()+1), map.get(k));
			});
			return im;
    	}).collect(Collectors.toList());
    }
    
}