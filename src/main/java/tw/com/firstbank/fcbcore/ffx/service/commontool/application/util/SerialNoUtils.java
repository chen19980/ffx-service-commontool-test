package tw.com.firstbank.fcbcore.ffx.service.commontool.application.util;

import java.util.Arrays;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import tw.com.firstbank.fcbcore.fcbframework.core.application.exception.BusinessException;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.exception.ServiceStatusCode;

/**
 * The utility for calculating SerialNumbers.
 */
@UtilityClass
public class SerialNoUtils {

	static final char MIN_CHAR = 'A';
	static final char MAX_CHAR = 'Z';
	static final int MIN_CHAR_VALUE = charToValue(MIN_CHAR);
	static final int MAX_CHAR_VALUE = charToValue(MAX_CHAR);

	/**
	 * Adds <i>diff</i> to <i>serialNo</i>.<br>
	 *
	 * @param serialNo SerialNumber to manipulate
	 * @param diff the number to add.
	 * @param maxNo the max number for this SerialNumber.<br>
	 *        Notice this <b><u>won't</u></b> throw error when <i>maxNo</i> is reached. <br>
	 *        It's merely for calculation.
	 * @return Manipulated SerialNumber
	 */
	@SneakyThrows
	public static String add(String serialNo, int diff, String maxNo) {
		if (diff == 0) {
			return serialNo;
		}
		serialNo = sanitizeInput(serialNo, diff, maxNo);

		int[] values = addPerDigit(serialNo, diff, maxNo);

		return digitsToString(values);
	}

	private static String digitsToString(int[] values) {
		// 轉換成 String 回傳
		StringBuilder sb = new StringBuilder();
		Arrays.stream(values).forEachOrdered(i -> sb.append(valueToChar(i)));
		return sb.toString();
	}

	private static int[] addPerDigit(String serialNo, int diff, String maxNo) {
		// 將接收到的 serialNo 一位一位計算，以便進位
		int[] values = serialNo.chars().map(charInt -> charToValue((char) charInt)).toArray();
		int lastIndexOfAlphabet = getLastIndexOfAlphabet(maxNo);
		values[values.length - 1] += diff;
		for (int i = values.length - 1; i > 0; i--) {
			int thisMaxValue = getMaxValue(values, i, lastIndexOfAlphabet);
			while (values[i] > thisMaxValue) {
				values[i] -= thisMaxValue + 1;
				values[i - 1]++;
			}
		}
		return values;
	}

	private static String sanitizeInput(String serialNo, int diff, String maxNo) {
		if (diff < 0) {
			throw new BusinessException(ServiceStatusCode.SUBTRACTION_NOT_SUPPORTED.getCode(),
					"SerialNoUtils.add 尚不支援減法行為");
		}
		// 當目前序號與最大序號長度不同時：
		// 1. 目前序號都只有數字時，自動補零
		// 2. 目前序號有字母時，不補零
		if (serialNo.length() != maxNo.length()) {
			if (serialNo.length() < maxNo.length() && isAllDigits(serialNo)) {
				serialNo = StringUtils.leftPad(serialNo, maxNo.length(), '0');
			} else {
				throw new BusinessException(
						ServiceStatusCode.SERIAL_NO_AND_MAX_NO_DIFFERENT_LENGTH.getCode(),
						"SerialNo 和 MaxNo 長度不同！");
			}
		}
		return serialNo;
	}

	private static boolean isAllDigits(String serialNo) {
		return serialNo.chars().allMatch(i -> '0' <= i && i <= '9');
	}

	private static int getLastIndexOfAlphabet(String s) {
		for (int i = 0; i < s.length(); i++) {
			if ('0' <= s.charAt(i) && s.charAt(i) <= '9') {
				return i - 1;
			}
		}
		return s.length() - 1;
	}

	private static int getMaxValue(int[] values, int thisIndex, int lastIndexOfAlphabet) {
		if (thisIndex > lastIndexOfAlphabet) {
			return 9;
		}

		// 需求參考 AP-118，取號算法較複雜
		// 一般數字時：按照正常進位即可
		// 包含字母時：字母允許 0 ~ Z 直到真的等於最大值為止。

		// 假如最大值為 BA
		// 則允許 AA, AB, AC ... AZ, B0, B1, B2, B3, ..., B9, BA
		// 但如果現在值仍無字母如 90 時，所有位數仍為十進位
		// 直到上一位數是字母時才允許這裡也變成字母
		return values[thisIndex - 1] >= MIN_CHAR_VALUE ? MAX_CHAR_VALUE : 9;
	}

	@SneakyThrows
	private static char valueToChar(int i) {
		if (0 <= i && i <= 9) {
			return (char) ('0' + i);
		} else if (MIN_CHAR_VALUE <= i && i <= MAX_CHAR_VALUE) {
			return (char) (MIN_CHAR + i - MIN_CHAR_VALUE);
		} else {
			throw new BusinessException(ServiceStatusCode.VALUE_NOT_SUPPORTED.getCode(),
					"SerialNoUtils.intToChar 尚不支援指定數字: " + i);
		}
	}

	/**
	 * Compares 2 SerialNumbers with special logic.
	 *
	 * @param a SerialNumber A
	 * @param b SerialNumber B
	 * @return An integer less than 0: if <i>a</i> < <i>b</i><br>
	 *         An integer equals 0: if <i>a</i> == <i>b</i><br>
	 *         An integer larger than 0: if <i>a</i> > <i>b</i>
	 */
	public static int compare(String a, String b) {
		a = trim(a);
		b = trim(b);
		if (a.length() != b.length()) {
			return Integer.compare(a.length(), b.length());
		}

		for (int i = 0; i < a.length(); i++) {
			if (a.charAt(i) != b.charAt(i)) {
				return compare(a.charAt(i), b.charAt(i));
			}
		}

		return 0;
	}

	private static String trim(String serialNo) {
		return serialNo.trim().replaceFirst("^0+", "");
	}

	private static int compare(char a, char b) {
		return Integer.compare(charToValue(a), charToValue(b));
	}

	@SneakyThrows
	private static int charToValue(char c) {
		if ('0' <= c && c <= '9') {
			return c - '0';
		} else if (MIN_CHAR <= c && c <= MAX_CHAR) {
			return c - MIN_CHAR + 10;
		} else {
			throw new BusinessException(ServiceStatusCode.CHARACTER_NOT_SUPPORTED.getCode(),
					"SerialNoUtils.charToInt 尚不支援指定字元: " + c);
		}
	}
}
