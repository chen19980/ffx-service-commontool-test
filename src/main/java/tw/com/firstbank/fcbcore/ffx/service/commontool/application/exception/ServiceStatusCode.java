package tw.com.firstbank.fcbcore.ffx.service.commontool.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum for status codes.
 */
@AllArgsConstructor
@Getter
public enum ServiceStatusCode {
	/**
	 * Converted Special Value of char not supported
	 */
	VALUE_NOT_SUPPORTED("0102"),
	/**
	 * Negative number input as diff
	 */
	SUBTRACTION_NOT_SUPPORTED("0111"),
	/**
	 * SerialNo and MaxNo in the input have different lengths
	 */
	SERIAL_NO_AND_MAX_NO_DIFFERENT_LENGTH("0112"),
	/**
	 * Char not supported
	 */
	CHARACTER_NOT_SUPPORTED("0101"),
	/**
	 * begin_no equals to 0
	 */
	BEGIN_NO_CANT_BE_ZERO("0001"),
	/**
	 * begin_no less than 0
	 */
	BEGIN_NO_CANT_BE_LESS_THAN_ZERO("0002"),
	/**
	 * begin_no equals to end_no
	 */
	BEGIN_NO_EQUAL_TO_END_NO("0011"),
	/**
	 * begin_no larger than end_no
	 */
	BEGIN_NO_GREATER_THAN_END_NO("0012"),
	/**
	 * No more numbers available for this SerialNumber.
	 */
	END_NO_REACHED("0021"),
	/**
	 * begin_no is not the same value as current db record.
	 */
	BEGIN_NO_NOT_SAME("0031"),
	/**
	 * end_no is not the same value as current db record.
	 */
	END_NO_NOT_SAME("0032"),
	/**
	 * Optimistic Lock retry count limit exceeded
	 */
	RETRY_FAILED("9901");

	private final String code;
}
