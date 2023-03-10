package tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.out.repository.ffxtsserialnumber.api;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.com.firstbank.fcbcore.fcbframework.core.spring.config.DataSourceMain;
import tw.com.firstbank.fcbcore.ffx.service.commontool.domain.ffxtsserialnumber.FfxtsSerialNumber;

/**
 * The interface of SerialNumber's JPA Repository.
 */
@Repository
@DataSourceMain
public interface FfxtsSerialNumberJpaRepository extends JpaRepository<FfxtsSerialNumber, Long> {

	Optional<FfxtsSerialNumber> findByIdentifierSystemTypeAndIdentifierSerialId(String systemType,
			String serialId);
}
