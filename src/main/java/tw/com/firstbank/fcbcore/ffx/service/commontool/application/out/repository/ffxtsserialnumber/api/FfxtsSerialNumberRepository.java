package tw.com.firstbank.fcbcore.ffx.service.commontool.application.out.repository.ffxtsserialnumber.api;

import java.util.Optional;
import tw.com.firstbank.fcbcore.fcbframework.core.application.out.repository.AggregateRepository;
import tw.com.firstbank.fcbcore.ffx.service.commontool.domain.ffxtsserialnumber.FfxtsSerialNumber;
import tw.com.firstbank.fcbcore.ffx.service.commontool.domain.ffxtsserialnumber.vo.Identifier;

/**
 * The interface of SerialNumber's repository.
 */
public interface FfxtsSerialNumberRepository
		extends AggregateRepository<FfxtsSerialNumber, Identifier> {

	/**
	 * Get SerialNumber by primary key columns in String format.
	 *
	 * @param systemType the system type
	 * @param serialId the serial id
	 * @return the optional
	 */
	Optional<FfxtsSerialNumber> get(String systemType, String serialId);

}
