package tw.com.firstbank.fcbcore.ffx.service.commontool.application.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import tw.com.firstbank.fcbcore.fcbframework.core.application.exception.BusinessException;

class SerialNoUtilsTest {


	public static final String SERIAL_NO = "ABC01";
	public static final String MAX_NO = "ZZ001";

	@Test
	void add_WillAddBy0_AndReturnOriginalString() {
		String actual = SerialNoUtils.add(SERIAL_NO, 0, MAX_NO);
		assertThat(actual).isEqualTo(SERIAL_NO);
	}

	@ParameterizedTest
	@CsvSource({"99998,99999,99999", "00001,00002,ZZ001", "99999,A0000,ZZ001", "A9999,AA000,ZZ001",
			"AZ999,B0000,ZZ001", "AZ999,B0000,ZZ001", "B9999,BA000,ZZ001", "A0000,A0001,ZZZZZ",
			"1,00002,ZZ001"})
	void add_WillAddBy1_AndReturnCorrectString(String input, String expected, String maxNo) {
		String actual = SerialNoUtils.add(input, 1, maxNo);
		assertThat(actual).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource({"1923,1", "12,Z"})
	void add_WillCheckLengths_AndThrowBusinessExceptionIfNotSame(String serialNo, String maxNo) {
		assertThrows(BusinessException.class, () -> SerialNoUtils.add(serialNo, 1, maxNo));
	}

	@ParameterizedTest
	@CsvSource({"/c,1", "ZZ001,-1"})
	void add_WillLimitUsableChars_AndThrowBusinessException(String input, int diff) {
		assertThrows(BusinessException.class, () -> SerialNoUtils.add(input, diff, input));
	}

	@ParameterizedTest
	@CsvSource({"0,1,-1", "1,0,1", "1,1,0", "00001,1,0", "  00232,232                    ,0",
			"99999,A0000,-1", "B0000,AZ999,1"})
	void compare_WillCompareByActualLengthFirst_AndReturnCompareResult(String a, String b,
			int expected) {
		int actual = SerialNoUtils.compare(a, b);

		assertThat(actual).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource({"/c,%g"})
	void compare_WillLimitUsableChars_AndThrowBusinessException(String input1, String input2) {
		assertThrows(BusinessException.class, () -> SerialNoUtils.compare(input1, input2));
	}
}
