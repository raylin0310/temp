import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lin
 */
public class Main {

	public static void main(String[] args) {
		System.out.println("bac".equals(answer(new String[]{"ba", "ab", "cb"})));
		System.out.println("yzx".equals(answer(new String[]{"y", "z", "xy"})));
		System.out.println("xzy".equals(answer(new String[]{"z", "yx", "yz"})));

		System.out.println("".equals(answer(new String[]{"yz", "yyy", "yyz"})));
		// d？
		System.out.println("abc".equals(answer(new String[]{"a", "ba", "bc", "cd"})));
	}

	public static String answer(String[] words) {

		Map<Character, LinkedHashSet<Character>> map = getOrderValueMap(words);

		Set<Character> values = map.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());

		//
		Set<Character> smallestKeySet = map.keySet()
				.stream()
				.filter(e -> !values.contains(e))
				.collect(Collectors.toCollection(LinkedHashSet::new));

		if (smallestKeySet.size() == 0) {
			// {"yz", "yyy", "yyz"} z<y  y<z  循环
			return "";
		}

		int[] pass = new int[26];
		Set<Character> alphabet = new LinkedHashSet<>();

		smallestKeySet.forEach(e -> dfs(e, alphabet, pass, map));

		StringBuilder res = new StringBuilder();
		alphabet.forEach(res::append);
		return res.reverse().toString();
	}


	private static void dfs(Character ch, Set<Character> alphabet, int[] pass, Map<Character, LinkedHashSet<Character>> map) {
		if (pass[ch - 'a'] == 1) {
			return;
		}
		// mark
		pass[ch - 'a'] = 1;
		if (map.containsKey(ch)) {
			for (Character v : map.get(ch)) {
				dfs(v, alphabet, pass, map);
			}
		}
		alphabet.add(ch);
	}

	private static Map<Character, LinkedHashSet<Character>> getOrderValueMap(String[] words) {
		Map<Character, LinkedHashSet<Character>> result = new LinkedHashMap<>();
		for (int i = 0; i < words.length - 1; i++) {
			String w1 = words[i];
			String w2 = words[i + 1];
			int min = Math.min(w1.length(), w2.length());
			for (int j = 0; j < min; j++) {
				if (w1.charAt(j) == w2.charAt(j)) {
					continue;
				}
				// 大小关系
				LinkedHashSet<Character> values = result.computeIfAbsent(w1.charAt(j), (k) -> new LinkedHashSet<>());
				values.add(w2.charAt(j));
				break;
			}
		}
		return result;
	}

}
