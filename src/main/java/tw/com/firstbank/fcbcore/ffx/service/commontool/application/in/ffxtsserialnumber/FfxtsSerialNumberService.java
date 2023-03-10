package tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tw.com.firstbank.fcbcore.fcbframework.core.application.exception.BusinessException;
import tw.com.firstbank.fcbcore.fcbframework.core.application.exception.ValidationException;
import tw.com.firstbank.fcbcore.fcbframework.core.application.util.ApLogHelper;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.exception.ServiceStatusCode;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.mapper.FfxtsSerialNumberDto;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.out.repository.ffxtsserialnumber.api.FfxtsSerialNumberRepository;
import tw.com.firstbank.fcbcore.ffx.service.commontool.domain.ffxtsserialnumber.FfxtsSerialNumber;
import tw.com.firstbank.fcbcore.ffx.service.commontool.domain.ffxtsserialnumber.vo.Identifier;


/**
 * The domain service of FfxtsSerialNumber.
 */
@Service
@AllArgsConstructor
public class FfxtsSerialNumberService {

	private static final Logger log = LoggerFactory.getLogger(FfxtsSerialNumberService.class);
	private final FfxtsSerialNumberRepository fxSerialNumberRepository;
	@Value("${serial-number.optimistic-lock-retry-limit}")
	int retryLimit;

	/**
	 * Tries to get a new SerialNumber for input request.<br>
	 * Includes an optimistic lock retry implementation.
	 *
	 * @param systemType system_type
	 * @param serialId serial_id
	 * @param beginNo begin_no
	 * @param endNo end_no
	 * @return new SerialNumber
	 */
	public String tryGetSerialNumber(String systemType, String serialId, String beginNo,
			String endNo) {
		for (int i = 0; i < retryLimit; i++) {
			try {
				return getSerialNumber(systemType, serialId, beginNo, endNo);
			} catch (OptimisticLockException ole) {
				ApLogHelper.warn(log,
						">>>> Optimistic lock version check failed, auto retrying... Attempt {}", i);
			}
		}

		throw new BusinessException(ServiceStatusCode.RETRY_FAILED.getCode(), "樂觀鎖自動重試已達上限，取號失敗！");
	}

	private String getSerialNumber(String systemType, String serialId, String beginNo, String endNo) {

		FfxtsSerialNumber input = new FfxtsSerialNumber();
		input.setIdentifier(new Identifier(systemType, serialId));
		input.setBeginNo(beginNo);
		input.setEndNo(endNo);
		input.setFfxtsSerialNumberId(1l);

		return fxSerialNumberRepository.save(input).getLastNo().toString();
	}

	/**
	 * Checks if beginNo from input is same as the record.
	 *
	 * @param dataBeginNo data begin_no
	 * @param inputBeginNo input begin_no
	 */
	public static void checkBeginNoIsSame(String dataBeginNo, String inputBeginNo) {
		if (!Objects.equals(dataBeginNo, inputBeginNo)) {
			throw new ValidationException(ServiceStatusCode.BEGIN_NO_NOT_SAME.getCode(),
					"雖然有找到此 id 的序號，但所輸入 BeginNo 與既有資料不同！");
		}
	}

	/**
	 * Checks if endNo from input is same as the record.
	 *
	 * @param dataEndNo data end_no
	 * @param inputEndNo input end_no
	 */
	public static void checkEndNoIsSame(String dataEndNo, String inputEndNo) {
		if (!Objects.equals(dataEndNo, inputEndNo)) {
			throw new ValidationException(ServiceStatusCode.END_NO_NOT_SAME.getCode(),
					"雖然有找到此 id 的序號，但所輸入 EndNo 與既有資料不同！");
		}
	}
}
