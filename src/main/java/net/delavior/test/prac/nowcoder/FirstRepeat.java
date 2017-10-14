package net.delavior.test.prac.nowcoder;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class FirstRepeat {
	public char findFirstRepeat(String A, int n) {
		if (StringUtils.isEmpty(A) || n == 0) {
			return '\0';
		}
		boolean[] hasPresentArr = new boolean[256];
		for (int i = 0; i < A.length(); i++) {
			if (hasPresentArr[A.charAt(i)]) {
				return A.charAt(i);
			} else {
				hasPresentArr[A.charAt(i)] = true;
			}
		}
		return '\0';
	}

	public char findFirstRepeat2(String A, int n) {
		Map<Character, Integer> charMap = new HashMap<>();
		for (int i = 0; i < A.length(); i++) {
			char c = A.charAt(i);
			if (charMap.containsKey(c)) {
				return c;
			} else {
				charMap.put(c, i);
			}
		}
		return '\0';
	}

	public static void main(String[] args) {

	}
}
