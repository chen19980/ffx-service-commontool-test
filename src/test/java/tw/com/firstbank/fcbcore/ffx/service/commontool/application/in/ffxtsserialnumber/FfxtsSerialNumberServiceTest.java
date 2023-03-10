package tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import tw.com.firstbank.fcbcore.fcbframework.core.application.exception.ValidationException;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.exception.ServiceStatusCode;

class FfxtsSerialNumberServiceTest {

	public static final String BEGIN_NO = "5";
	public static final String END_NO = "6";

	@Test
	void checkBeginNoIsSame_WillCompareBeginNoOfInputAndData_AndDoesNotThrow() {
		assertDoesNotThrow(() -> FfxtsSerialNumberService.checkBeginNoIsSame(BEGIN_NO, BEGIN_NO));
	}

	@Test
	void checkBeginNoIsSame_WillCompareBeginNoOfInputAndData_AndReturnValidationException() {
		ValidationException actual = assertThrows(ValidationException.class,
				() -> FfxtsSerialNumberService.checkBeginNoIsSame(BEGIN_NO, END_NO));
		assertThat(actual.getStatusCode()).endsWith(ServiceStatusCode.BEGIN_NO_NOT_SAME.getCode());
	}

	@Test
	void checkEndNoIsSame_WillCompareEndNoOfInputAndData_AndDoesNotThrow() {
		assertDoesNotThrow(() -> FfxtsSerialNumberService.checkEndNoIsSame(BEGIN_NO, BEGIN_NO));
	}

	@Test
	void checkEndNoIsSame_WillCompareEndNoOfInputAndDate_AndReturnValidationException() {
		ValidationException actual = assertThrows(ValidationException.class,
				() -> FfxtsSerialNumberService.checkEndNoIsSame(BEGIN_NO, END_NO));
		assertThat(actual.getStatusCode()).endsWith(ServiceStatusCode.END_NO_NOT_SAME.getCode());
	}
}
