package tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.out.repository.ffxtsserialnumber.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.out.repository.ffxtsserialnumber.api.FfxtsSerialNumberJpaRepository;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.out.repository.ffxtsserialnumber.api.FfxtsSerialNumberRepository;
import tw.com.firstbank.fcbcore.ffx.service.commontool.domain.ffxtsserialnumber.FfxtsSerialNumber;

/**
 * The type of SerialNumber's repository's implementation.
 */
@Repository
@RequiredArgsConstructor
public class FfxtsSerialNumberRepositoryImpl implements FfxtsSerialNumberRepository {

	private final FfxtsSerialNumberJpaRepository repo;

	/**
	 * Selects a record by SYSTEM_TYPE and SERIAL_ID.
	 *
	 * @param systemType SYSTEM_TYPE
	 * @param serialId SERIAL_ID
	 * @return the optional
	 */
	@Override
	public Optional<FfxtsSerialNumber> get(String systemType, String serialId) {
		return repo.findByIdentifierSystemTypeAndIdentifierSerialId(systemType, serialId);
	}

	/**
	 * Saves the entity into DB and flushes DB buffer.
	 *
	 * @param ffxtsSerialNumber The entity
	 * @return The saved entity after flushing.
	 */
	@Override
	public FfxtsSerialNumber saveAndFlush(FfxtsSerialNumber ffxtsSerialNumber) {
		FfxtsSerialNumber saved = save(ffxtsSerialNumber);
		repo.flush();
		return saved;
	}

	/**
	 * Save a SerialNumber.
	 *
	 * @param ffxtsSerialNumber the serial number
	 * @return the serial number
	 */
	@Override
	public FfxtsSerialNumber save(FfxtsSerialNumber ffxtsSerialNumber) {
		return repo.save(ffxtsSerialNumber);
	}
}
