package tw.com.firstbank.fcbcore.ffx.service.commontool.domain.ffxtsserialnumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tw.com.firstbank.fcbcore.fcbframework.core.application.exception.ValidationException;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.exception.ServiceStatusCode;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.mapper.FfxtsSerialNumberDto;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.util.SerialNoUtils;
import tw.com.firstbank.fcbcore.ffx.service.commontool.domain.ffxtsserialnumber.vo.Identifier;

class CreateFfxtsSerialNumberEntityTest {

	private static final String SYSTEM_TYPE = "IR";
	private static final String SERIAL_ID = "PaymentMessage2077";
	private static final String BEGIN_NO = "1";
	private static final String END_NO = "6999999";

	/**
	 * Tests that will throw validation
	 */
	@Test
	void createFfxtsSerialNumberWithBeginNoAsZero_WillThrowValidationException() {
		// given test data

		FfxtsSerialNumberDto dto = new FfxtsSerialNumberDto();
		dto.setIdentifier(new Identifier(SYSTEM_TYPE, SERIAL_ID));
		dto.setBeginNo("0");
		dto.setEndNo(END_NO);

		// act
		ValidationException actual =
				assertThrows(ValidationException.class, () -> new FfxtsSerialNumber(dto));

		assertThat(actual.getStatusCode()).endsWith(ServiceStatusCode.BEGIN_NO_CANT_BE_ZERO.getCode());
	}

	@Test
	void createFfxtsSerialNumberWithBeginNoGreaterThanEndNo_WillThrowValidationException() {
		// given test data

		FfxtsSerialNumberDto dto = new FfxtsSerialNumberDto();
		dto.setIdentifier(new Identifier(SYSTEM_TYPE, SERIAL_ID));
		dto.setBeginNo(SerialNoUtils.add(END_NO, 1, END_NO));
		dto.setEndNo(END_NO);

		// act
		ValidationException actual =
				assertThrows(ValidationException.class, () -> new FfxtsSerialNumber(dto));

		assertThat(actual.getStatusCode())
				.endsWith(ServiceStatusCode.BEGIN_NO_GREATER_THAN_END_NO.getCode());
	}

	@Test
	void createFfxtsSerialNumberWithBeginNoLessThanZero_WillThrowValidationException() {
		// given test data

		FfxtsSerialNumberDto dto = new FfxtsSerialNumberDto();
		dto.setIdentifier(new Identifier(SYSTEM_TYPE, SERIAL_ID));
		dto.setBeginNo("-1");
		dto.setEndNo(END_NO);

		// act
		ValidationException actual =
				assertThrows(ValidationException.class, () -> new FfxtsSerialNumber(dto));

		assertThat(actual.getStatusCode())
				.endsWith(ServiceStatusCode.BEGIN_NO_CANT_BE_LESS_THAN_ZERO.getCode());
	}

	@Test
	void createFfxtsSerialNumberWithBeginNoSameAsEndNo_WillThrowValidationException() {
		// given test data

		FfxtsSerialNumberDto dto = new FfxtsSerialNumberDto();
		dto.setIdentifier(new Identifier(SYSTEM_TYPE, SERIAL_ID));
		dto.setBeginNo(BEGIN_NO);
		dto.setEndNo(BEGIN_NO);

		// act
		ValidationException actual =
				assertThrows(ValidationException.class, () -> new FfxtsSerialNumber(dto));

		assertThat(actual.getStatusCode())
				.endsWith(ServiceStatusCode.BEGIN_NO_EQUAL_TO_END_NO.getCode());
	}

	/**
	 * Create entity test
	 */
	@Test
	void createFfxtsSerialNumberWithCorrectData_WillSuccess() {

		// given test data

		FfxtsSerialNumberDto dto = new FfxtsSerialNumberDto();
		dto.setIdentifier(new Identifier(SYSTEM_TYPE, SERIAL_ID));
		dto.setBeginNo(BEGIN_NO);
		dto.setEndNo(END_NO);

		// act
		FfxtsSerialNumber actual = assertDoesNotThrow(() -> new FfxtsSerialNumber(dto));

		// then validate
		Assertions.assertEquals(actual.getIdentifier().getSystemType(),
				dto.getIdentifier().getSystemType());
		Assertions.assertEquals(actual.getIdentifier().getSerialId(),
				dto.getIdentifier().getSerialId());
		assertEquals(actual.getBeginNo(), dto.getBeginNo());
		assertEquals(actual.getEndNo(), dto.getEndNo());
		assertEquals(actual.getAggregateId(), SYSTEM_TYPE + "$" + SERIAL_ID);
	}
}
