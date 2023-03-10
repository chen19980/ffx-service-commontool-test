package tw.com.firstbank.fcbcore.ffx.service.commontool.application.util;

import com.google.common.primitives.Chars;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import tw.com.firstbank.fcbcore.ffx.service.commontool.ActiveProfileResolver;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(resolver = ActiveProfileResolver.class)
class StringUtilsTest {

	static String numbers = "0123456789";
	static String symbols = "/!@$#%><? ／　，。？！";
	static String alphabets = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static String chinese = "我是中文字";
	static String japanese = "にほんごです";

	static String pool = numbers + symbols + alphabets + chinese + japanese;

	static final int testStringLength = 50;
	static final int testTimes = 100;

	private String getRandomString(String pool) {
		if (testStringLength <= 0) {
			throw new IllegalArgumentException();
		}
		List<Character> chars = Chars.asList(pool.toCharArray());
		Collections.shuffle(chars);

		StringBuilder sb = new StringBuilder(Math.min(StringUtilsTest.testStringLength, chars.size()));
		for (int i = 0; i < sb.capacity(); i++) {
			sb.append(chars.get(i));
		}

		return sb.toString();
	}

	private String getRandomString() {
		return getRandomString(pool);
	}

	@RepeatedTest(testTimes)
	void reserveNumeric_WillRemoveOthers_AndReturnNumericsOnly() {
		// Arrange
		String regex = "[^0-9]";

		String input = getRandomString() + 0; // 確保有數字
		String inputNumericsOnly = input.replaceAll(regex, "");

		// Act
		String actual = StringUtils.reserveNumeric(input);

		// Assert
		assertThat(actual).containsOnlyDigits().isEqualTo(inputNumericsOnly);
	}

	@RepeatedTest(testTimes)
	void removeSymbolAndSpace_WillRemoveOthers_AndReturnNumericsAndAlphabetsOnly() {
		// Arrange
		String regex = "[^0-9A-z]";
		String regexForAssert = regex.replace("^", "") + "*";

		String input = getRandomString() + "0aA"; // 確保有指定範圍的文字

		String inputNumericsAndAlphabetsOnly = input.replaceAll(regex, "");
		// Act
		String actual = StringUtils.removeSymbolAndSpace(input);
		// Assert
		assertThat(actual).matches(regexForAssert).isEqualTo(inputNumericsAndAlphabetsOnly);
	}

	@RepeatedTest(testTimes)
	void removeSymbol_WillRemoveOthers_AndReturnNumericsAndAlphabetsAndSpacesOnly() {
		// Arrange
		String regex = "[^0-9A-z ]";
		String regexForAssert = regex.replace("^", "") + "*";

		String input = getRandomString() + ' ';
		String inputWithoutSymbolsExpectSpace = input.replaceAll(regex, "");

		// Act
		String actual = StringUtils.removeSymbol(input);

		// Assert
		assertThat(actual).matches(regexForAssert).isEqualTo(inputWithoutSymbolsExpectSpace);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	void reserveAlphabet_WillReserveAlphabet_AndReturnAlphabet() {
		// Arrange
		String input = "Eric Chang 1969.10.18";

		String expected = "EricChang";

		// Act
		String actual = StringUtils.reserveAlphabet(input);

		// Assert
		assertThat(actual).isEqualTo(expected);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	void reserveNumeric_WillDoNothingWhenNull_AndReturnNull() {
		// Arrange
		String input = null;

		// Act
		String actual = StringUtils.reserveNumeric(input);

		// Assert
		assertThat(actual).isEqualTo(input);
	}

	@Test
	void reserveNumeric_WillDoNothingWhenEmpty_AndReturnEmpty() {
		// Arrange
		String input = "";

		// Act
		String actual = StringUtils.reserveNumeric(input);

		// Assert
		assertThat(actual).isEqualTo(input);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	void removeSymbolAndSpace_WillDoNothingWhenNull_AndReturnNull() {
		// Arrange
		String input = null;

		// Act
		String actual = StringUtils.removeSymbolAndSpace(input);

		// Assert
		assertThat(actual).isEqualTo(input);
	}

	@Test
	void reserveAlphabet_WillDoNothingWhenNull_AndReturnNull() {
		// Arrange
		String input = null;

		// Act
		String actual = StringUtils.reserveAlphabet(input);

		// Assert
		assertThat(actual).isEqualTo(input);
	}

	@Test
	void removeSymbolAndSpace_WillDoNothingWhenEmpty_AndReturnEmpty() {
		// Arrange
		String input = "";

		// Act
		String actual = StringUtils.removeSymbolAndSpace(input);

		// Assert
		assertThat(actual).isEqualTo(input);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	void removeSymbol_WillDoNothingWhenNull_AndReturnNull() {
		// Arrange
		String input = null;

		// Act
		String actual = StringUtils.removeSymbol(input);

		// Assert
		assertThat(actual).isEqualTo(input);
	}

	@Test
	void removeSymbol_WillDoNothingWhenEmpty_AndReturnEmpty() {
		// Arrange
		String input = "";

		// Act
		String actual = StringUtils.removeSymbol(input);

		// Assert
		assertThat(actual).isEqualTo(input);
	}

	@Test
	void reserveAlphabet_WillDoNothingWhenEmpty_AndReturnEmpty() {
		// Arrange
		String input = "";

		// Act
		String actual = StringUtils.reserveAlphabet(input);

		// Assert
		assertThat(actual).isEqualTo(input);
	}
}
