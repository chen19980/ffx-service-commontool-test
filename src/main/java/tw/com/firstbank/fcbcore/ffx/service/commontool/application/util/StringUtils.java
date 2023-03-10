package tw.com.firstbank.fcbcore.ffx.service.commontool.application.util;

import lombok.experimental.UtilityClass;

import java.util.function.Predicate;

/**
 * The utility for sanitizing strings, mainly for payment messages.
 */
@UtilityClass
public class StringUtils {
	private final String BLANK = "";

	@SafeVarargs
	private static String reserveConditional(String s, Predicate<Character>... conditions) {
		if (s == null || s.isEmpty()) {
			return s;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			for (Predicate<Character> p : conditions) {
				// if any of the condition matches, the character is reserved.
				if (p.test(c)) {
					sb.append(c);
					break;
				}
			}
		}

		return sb.toString();
	}

	/**
	 * Sanitize input so that the output only contains numbers. 將字串除非數字(0~9)字元後,回傳值
	 *
	 * @param s input
	 *
	 * @return input stripped off non-numeric characters
	 */
	public static String reserveNumeric(String s) {
		return reserveConditional(s, (Character c) -> '0' <= c && c <= '9');
	}

	/**
	 * Sanitize input so that the output only contains alphabets or numbers.
	 * 將字串去除特殊符號及空白,保留英文字母(a~z,A~Z)、數字（0~9）,回傳值
	 *
	 * @param s input
	 *
	 * @return input stripped off non-alphabetic, non-numeric characters
	 */
	public static String removeSymbolAndSpace(String s) {
		return reserveConditional(s, (Character c) -> '0' <= c && c <= '9',
				(Character c) -> 'a' <= c && c <= 'z', (Character c) -> 'A' <= c && c <= 'Z');
	}

	/**
	 * Sanitize input so that the output only contains alphabets, numbers or spaces.
	 * 將字串除特殊符號,保留英文字母(a~z,A~Z)及空白、數字（0~9）,回傳值
	 *
	 * @param s input
	 *
	 * @return input with only alphabets, numbers or spaces
	 */
	public static String removeSymbol(String s) {
		return reserveConditional(s, (Character c) -> '0' <= c && c <= '9',
				(Character c) -> 'a' <= c && c <= 'z', (Character c) -> 'A' <= c && c <= 'Z',
				(Character c) -> c == ' ');
	}

	/**
	 * 將字串處理,只保留 a~z,A~Z字元
	 *
	 * @param s 原始字串
	 * @return 處理後字串
	 */
	public static String reserveAlphabet(String s) {
		return reserveConditional(s, (Character c) -> 'a' <= c && c <= 'z',
				(Character c) -> 'A' <= c && c <= 'Z');
	}

	/**
	 * 產生同一字元,指定長度的字串
	 *
	 * @param sign 指定字元
	 * @param len 指定長度
	 * @return 產生字串
	 */
	public static String fillSign(String sign, Integer len) {
		StringBuilder rs = new StringBuilder();
		for (int i = 0; i < len; i++) {
			rs.append(sign);
		}
		return rs.toString();
	}

	/**
	 * 取部份字串
	 *
	 * @param s 原始字串
	 * @param b 起始位置
	 *
	 * @return 擷取字串
	 */
	public static String subStr(String s, int b) {
		int e = s.length();
		return subStr(s, b, e);
	}

	/**
	 * 取部份字串
	 *
	 * @param s 原始字串
	 * @param b 起始位置
	 * @param e 結束位置
	 *
	 * @return 擷取字串
	 */
	public static String subStr(String s, int b, int e) {
		if (s == null || s.isBlank()) {
			return BLANK;
		}
		return (b > s.length() ? "" : s.substring(b, Math.min(s.length(), e)));
	}

	/**
	 * 判斷字串是否為null,空字串
	 *
	 * @param s 字串
	 * @return true/false
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.isBlank() || s.isEmpty();
	}

	/**
	 * 取字串長度
	 *
	 * @param s 字串
	 * @return 字串長度
	 */
	public static Integer length(String s) {
		return isEmpty(s) ? 0 : s.length();
	}
}
