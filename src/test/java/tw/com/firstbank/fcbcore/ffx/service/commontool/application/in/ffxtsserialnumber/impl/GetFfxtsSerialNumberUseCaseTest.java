package tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;
import javax.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import tw.com.firstbank.fcbcore.fcbframework.core.application.exception.BusinessException;
import tw.com.firstbank.fcbcore.fcbframework.core.application.exception.ValidationException;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.exception.ServiceStatusCode;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.FfxtsSerialNumberService;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.api.GetFfxtsSerialNumberRequestCommand;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.api.GetFfxtsSerialNumberResponseCommand;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.api.GetFfxtsSerialNumberUseCaseApi;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.mapper.FfxtsSerialNumberDto;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.out.repository.ffxtsserialnumber.api.FfxtsSerialNumberRepository;
import tw.com.firstbank.fcbcore.ffx.service.commontool.domain.ffxtsserialnumber.FfxtsSerialNumber;
import tw.com.firstbank.fcbcore.ffx.service.commontool.domain.ffxtsserialnumber.vo.Identifier;

class GetFfxtsSerialNumberUseCaseTest {

	public static final String SYSTEM_TYPE = "FIR";
	public static final String SERIAL_ID = "PaymentMessage1234";
	public static final String BEGIN_NO = "1";
	public static final String BEGIN_NO_ALTERNATE = "2";
	public static final String END_NO = "6999999";
	public static final String END_NO_ALTERNATE = "7000000";
	public static final String BEGIN_NO_CLOSE_TO_END = "6999998";
	public static final String NEW_SERIAL_NO_2 = "0000002";
	private static final int RETRY_LIMIT = 10;
	@Mock
	private FfxtsSerialNumberRepository fxSerialNumberRepository;
	private GetFfxtsSerialNumberUseCaseApi getFfxtsSerialNumberUseCaseApi;

	@Test
	void getSerialNumber_WillCheckBeginNoEqualToZero_AndThrowValidationException() {
		// Act & Assert
		// 這個方法也有 new()，而 exception 發生在 new 的期間
		ValidationException exception =
				assertThrows(ValidationException.class, this::getSerialNumber_Act_ExpectedZero);

		getSerialNumber_Assert_ValidationException(exception, ServiceStatusCode.BEGIN_NO_CANT_BE_ZERO);
		getSerialNumber_Assert_NeverSaved();
		getSerialNumber_Assert_NeverQueried();
	}

	private void getSerialNumber_Act_ExpectedZero() {
		getSerialNumber_ExpectedFactory("0", END_NO);
	}

	private void getSerialNumber_Assert_ValidationException(ValidationException exception,
			ServiceStatusCode statusCode) {
		assertThat(exception.getStatusCode()).endsWith(statusCode.getCode());
	}

	private void getSerialNumber_Assert_NeverSaved() {
		verify(fxSerialNumberRepository, times(0)).save(any());
	}

	private void getSerialNumber_Assert_NeverQueried() {
		verify(fxSerialNumberRepository, times(0)).get(any(), any());
	}

	private FfxtsSerialNumber getSerialNumber_ExpectedFactory(String beginNo, String endNo) {
		FfxtsSerialNumberDto dto = new FfxtsSerialNumberDto();
		dto.setIdentifier(new Identifier(SYSTEM_TYPE, SERIAL_ID));
		dto.setBeginNo(beginNo);
		dto.setEndNo(endNo);
		return new FfxtsSerialNumber(dto);
	}

	@Test
	void getSerialNumber_WillCheckBeginNoEqualsToEndNo_AndThrowValidationException() {
		// Arrange
		FfxtsSerialNumberDto dto = new FfxtsSerialNumberDto();
		dto.setIdentifier(new Identifier(SYSTEM_TYPE, SERIAL_ID));
		dto.setBeginNo(BEGIN_NO);
		dto.setEndNo(BEGIN_NO);

		// Act & Assert
		ValidationException exception =
				assertThrows(ValidationException.class, () -> new FfxtsSerialNumber(dto));

		getSerialNumber_Assert_ValidationException(exception,
				ServiceStatusCode.BEGIN_NO_EQUAL_TO_END_NO);
		getSerialNumber_Assert_NeverSaved();
		getSerialNumber_Assert_NeverQueried();
	}

	@Test
	void getSerialNumber_WillCheckBeginNoGreaterThanEndNo_AndThrowValidationException() {
		// Arrange
		FfxtsSerialNumberDto dto = new FfxtsSerialNumberDto();
		dto.setIdentifier(new Identifier(SYSTEM_TYPE, SERIAL_ID));
		dto.setBeginNo(END_NO);
		dto.setEndNo(BEGIN_NO);

		// Act & Assert
		ValidationException exception =
				assertThrows(ValidationException.class, () -> new FfxtsSerialNumber(dto));

		getSerialNumber_Assert_ValidationException(exception,
				ServiceStatusCode.BEGIN_NO_GREATER_THAN_END_NO);
		getSerialNumber_Assert_NeverSaved();
		getSerialNumber_Assert_NeverQueried();
	}

	@Test
	void getSerialNumber_WillCheckBeginNoIsSameAsAlreadyExistingDbRecord_AndThrowValidationException() {
		// Arrange
		FfxtsSerialNumber input = getSerialNumber_Arrange_Expected();
		FfxtsSerialNumber data = getSerialNumber_Arrange_ExpectedAlternateBeginNo();
		getSerialNumber_Arrange_RepoGetsInputButReturnsData(input, data);

		// Act & Assert
		ValidationException exception = getSerialNumber_Act_ValidationException(input);

		getSerialNumber_Assert_ValidationException(exception, ServiceStatusCode.BEGIN_NO_NOT_SAME);
		getSerialNumber_Assert_QueriedOnce(input);
		getSerialNumber_Assert_NeverSaved();
	}

	private FfxtsSerialNumber getSerialNumber_Arrange_Expected() {
		return getSerialNumber_ExpectedFactory(BEGIN_NO, END_NO);
	}

	private FfxtsSerialNumber getSerialNumber_Arrange_ExpectedAlternateBeginNo() {
		return getSerialNumber_ExpectedFactory(BEGIN_NO_ALTERNATE, END_NO);
	}

	private void getSerialNumber_Arrange_RepoGetsInputButReturnsData(FfxtsSerialNumber input,
			FfxtsSerialNumber data) {
		given(fxSerialNumberRepository.get(input.getIdentifier().getSystemType(),
				input.getIdentifier().getSerialId())).willReturn(Optional.of(data));
	}

	private ValidationException getSerialNumber_Act_ValidationException(FfxtsSerialNumber input) {
		return assertThrows(ValidationException.class, () -> getSerialNumber_Act(input));
	}

	private void getSerialNumber_Assert_QueriedOnce(FfxtsSerialNumber expected) {
		verify(fxSerialNumberRepository).get(expected.getIdentifier().getSystemType(),
				expected.getIdentifier().getSerialId());
	}

	private GetFfxtsSerialNumberResponseCommand getSerialNumber_Act(FfxtsSerialNumber input) {
		GetFfxtsSerialNumberRequestCommand requestCommand = new GetFfxtsSerialNumberRequestCommand();
		requestCommand.setSystemType(input.getIdentifier().getSystemType());
		requestCommand.setSerialId(input.getIdentifier().getSerialId());
		requestCommand.setBeginNo(input.getBeginNo());
		requestCommand.setEndNo(input.getEndNo());

		return getFfxtsSerialNumberUseCaseApi.execute(requestCommand);
	}

	@Test
	void getSerialNumber_WillCheckBeginNoLessThanZero_AndThrowValidationException() {
		// Act & Assert
		// 這個方法也有 new()，而 exception 發生在 new 的期間
		ValidationException exception =
				assertThrows(ValidationException.class, this::getSerialNumber_Act_ExpectedNegative);

		getSerialNumber_Assert_ValidationException(exception,
				ServiceStatusCode.BEGIN_NO_CANT_BE_LESS_THAN_ZERO);
		getSerialNumber_Assert_NeverSaved();
		getSerialNumber_Assert_NeverQueried();
	}

	private void getSerialNumber_Act_ExpectedNegative() {
		getSerialNumber_ExpectedFactory("-1", END_NO);
	}

	@Test
	void getSerialNumber_WillCheckEndNoIsSameWithAlreadyExistingDbRecord_AndThrowValidationException() {
		// Arrange
		FfxtsSerialNumber input = getSerialNumber_Arrange_Expected();
		FfxtsSerialNumber data = getSerialNumber_Arrange_ExpectedAlternateEndNo();
		getSerialNumber_Arrange_RepoGetsInputButReturnsData(input, data);

		// Act & Assert
		ValidationException exception = getSerialNumber_Act_ValidationException(input);

		getSerialNumber_Assert_ValidationException(exception, ServiceStatusCode.END_NO_NOT_SAME);
		getSerialNumber_Assert_QueriedOnce(input);
		getSerialNumber_Assert_NeverSaved();
	}

	private FfxtsSerialNumber getSerialNumber_Arrange_ExpectedAlternateEndNo() {
		return getSerialNumber_ExpectedFactory(BEGIN_NO, END_NO_ALTERNATE);
	}

	@Test
	void getSerialNumber_WillCheckEndNoReached_AndThrowValidationException() {
		// Arrange
		FfxtsSerialNumber input = getSerialNumber_Arrange_ExpectedCloseToEnd();

		getSerialNumber_Arrange_RepoReturnsOnQuery(input);
		getSerialNumber_Arrange_RepoReturnsOnSave(input);

		// Act & Assert
		getSerialNumber_Act(input); // 6999998
		getSerialNumber_Act(input); // 6999999
		ValidationException exception = getSerialNumber_Act_ValidationException(input); // 7000000, off
																																										// limit
		getSerialNumber_Assert_ValidationException(exception, ServiceStatusCode.END_NO_REACHED);
		getSerialNumber_Assert_SavedTwice(input);
		getSerialNumber_Assert_QueriedThrice(input);
	}

	private FfxtsSerialNumber getSerialNumber_Arrange_ExpectedCloseToEnd() {
		return getSerialNumber_ExpectedFactory(BEGIN_NO_CLOSE_TO_END, END_NO);
	}

	private void getSerialNumber_Arrange_RepoReturnsOnQuery(FfxtsSerialNumber expected) {
		given(fxSerialNumberRepository.get(expected.getIdentifier().getSystemType(),
				expected.getIdentifier().getSerialId())).willReturn(Optional.of(expected));
	}

	private void getSerialNumber_Arrange_RepoReturnsOnSave(FfxtsSerialNumber expected) {
		given(fxSerialNumberRepository.save(getSerialNumber_GetArgs(expected)))
				.willAnswer(i -> i.getArguments()[0]); // returns whatever is being saved
	}

	private void getSerialNumber_Assert_SavedTwice(FfxtsSerialNumber expected) {
		verify(fxSerialNumberRepository, times(2)).save(getSerialNumber_GetArgs(expected));
	}

	private void getSerialNumber_Assert_QueriedThrice(FfxtsSerialNumber expected) {
		verify(fxSerialNumberRepository, times(3)).get(expected.getIdentifier().getSystemType(),
				expected.getIdentifier().getSerialId());
	}

	private FfxtsSerialNumber getSerialNumber_GetArgs(FfxtsSerialNumber expected) {
		return argThat(
				e -> e.getIdentifier().getSerialId().equals(expected.getIdentifier().getSerialId())
						&& e.getIdentifier().getSystemType().equals(expected.getIdentifier().getSystemType())
						&& e.getBeginNo().equals(expected.getBeginNo())
						&& e.getEndNo().equals(expected.getEndNo()));
	}

	@Test
	void getSerialNumber_WillCheckExistAndUpdateDataBase_AndReturnLastNoPlusOne() {
		// Arrange
		FfxtsSerialNumber expected = getSerialNumber_Arrange_Expected();
		getSerialNumber_Arrange_RepoReturnsOnQuery(expected);
		getSerialNumber_Arrange_RepoReturnsOnSave(expected);

		// Act
		// 兩次, 因為想測試的是資料已存在的情況, 所以第一次僅建資料
		getSerialNumber_Act(expected);
		GetFfxtsSerialNumberResponseCommand actual = getSerialNumber_Act(expected);

		// Assert
		getSerialNumber_Assert_SavedTwice(expected);
		getSerialNumber_Assert_NewSerialNoIs2(actual);
	}

	private void getSerialNumber_Assert_NewSerialNoIs2(GetFfxtsSerialNumberResponseCommand actual) {
		assertThat(actual.getSerialNo()).isEqualTo(NEW_SERIAL_NO_2);
	}

	@Test
	void getSerialNumber_WillNeverThrowOptimisticLockExceptionInTheBranchWhenDbRecordDoesNotExist_AndReturnSerialNoAfterRetryAutomatically() {
		// Arrange
		FfxtsSerialNumber expected = getSerialNumber_Arrange_Expected();

		getSerialNumber_Arrange_QueryReturnsNothing(expected);
		getSerialNumber_Arrange_RepoWillThrowOptimisticLockExceptionThenReturns(expected);

		// Act
		GetFfxtsSerialNumberResponseCommand actual = getSerialNumber_Act(expected);

		// Assert
		getSerialNumber_Assert_QueriedTwice(expected);
		getSerialNumber_Assert_SavedTwice(expected);
		getSerialNumber_Assert_ResponseIsBeginNo(actual, expected);
	}

	private void getSerialNumber_Arrange_QueryReturnsNothing(FfxtsSerialNumber expected) {
		given(fxSerialNumberRepository.get(expected.getIdentifier().getSystemType(),
				expected.getIdentifier().getSerialId())).willReturn(Optional.empty());
	}

	private void getSerialNumber_Arrange_RepoWillThrowOptimisticLockExceptionThenReturns(
			FfxtsSerialNumber expected) {
		given(fxSerialNumberRepository.save(getSerialNumber_GetArgs(expected)))
				.willThrow(OptimisticLockException.class).willAnswer(i -> i.getArguments()[0]); // returns
																																												// whatever
																																												// is being
																																												// saved
	}

	private void getSerialNumber_Assert_QueriedTwice(FfxtsSerialNumber expected) {
		verify(fxSerialNumberRepository, times(2)).get(expected.getIdentifier().getSystemType(),
				expected.getIdentifier().getSerialId());
	}

	private void getSerialNumber_Assert_ResponseIsBeginNo(GetFfxtsSerialNumberResponseCommand actual,
			FfxtsSerialNumber expected) {
		assertThat(actual.getSerialNo()).isEqualTo(expected.getBeginNo());
	}

	@Test
	void getSerialNumber_WillNeverThrowOptimisticLockExceptionInTheBranchWhereDbRecordAlreadyExists_AndReturnSerialNoAfterRetryAutomatically() {
		// Arrange
		FfxtsSerialNumber expected = getSerialNumber_Arrange_Expected();

		getSerialNumber_Arrange_RepoReturnsOnQuery(expected);
		getSerialNumber_Arrange_RepoWillThrowOptimisticLockExceptionThenReturns(expected);

		// Act
		GetFfxtsSerialNumberResponseCommand actual = getSerialNumber_Act(expected);

		// Assert
		getSerialNumber_Assert_NewSerialNoIs2(actual);
		getSerialNumber_Assert_QueriedTwice(expected);
		getSerialNumber_Assert_SavedTwice(expected);
	}

	@Test
	void getSerialNumber_WillRetryUntilLimitExceeded_AndThrowBusinessException() {
		// Arrange
		FfxtsSerialNumber expected = getSerialNumber_Arrange_Expected();

		getSerialNumber_Arrange_RepoReturnsOnQuery(expected);
		getSerialNumber_Arrange_RepoWillThrowOptimisticLockException();

		// Act
		BusinessException actual = getSerialNumber_Act_BusinessException(expected);

		// Assert
		getSerialNumber_Assert_RetryFailed(actual);
		getSerialNumber_Assert_RetriedUntilLimit(expected);
	}

	private void getSerialNumber_Arrange_RepoWillThrowOptimisticLockException() {
		given(fxSerialNumberRepository.save(any())).willThrow(OptimisticLockException.class);
	}

	private BusinessException getSerialNumber_Act_BusinessException(FfxtsSerialNumber input) {
		return assertThrows(BusinessException.class, () -> getSerialNumber_Act(input));
	}

	private void getSerialNumber_Assert_RetryFailed(BusinessException exception) {
		assertThat(exception.getStatusCode()).endsWith(ServiceStatusCode.RETRY_FAILED.getCode());
	}

	private void getSerialNumber_Assert_RetriedUntilLimit(FfxtsSerialNumber expected) {
		verify(fxSerialNumberRepository, times(RETRY_LIMIT))
				.get(expected.getIdentifier().getSystemType(), expected.getIdentifier().getSerialId());
		verify(fxSerialNumberRepository, times(RETRY_LIMIT)).save(expected);
	}

	@Test
	void getSerialNumber_WillWriteDbIfNotExist_AndReturnLastNoSameAsBeginNo() {
		// Arrange
		FfxtsSerialNumber expected = getSerialNumber_Arrange_Expected();
		getSerialNumber_Arrange_QueryReturnsNothing(expected);
		getSerialNumber_Arrange_RepoReturnsOnSave(expected);

		// Act
		GetFfxtsSerialNumberResponseCommand actual = getSerialNumber_Act(expected);

		// Assert
		getSerialNumber_Assert_ResponseIsBeginNo(actual, expected);
		getSerialNumber_Assert_QueriedOnce(expected);
		getSerialNumber_Assert_SavedOnce(expected);
	}

	private void getSerialNumber_Assert_SavedOnce(FfxtsSerialNumber expected) {
		verify(fxSerialNumberRepository).save(getSerialNumber_GetArgs(expected));
	}

	@BeforeEach
	public void setup() {
		openMocks(this);
		FfxtsSerialNumberService fxSerialNumberService =
				new FfxtsSerialNumberService(fxSerialNumberRepository, RETRY_LIMIT);
		getFfxtsSerialNumberUseCaseApi = new GetFfxtsSerialNumberUseCaseImpl(fxSerialNumberService);
	}
}
