package tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.out.repository.ffxtsserialnumber.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import tw.com.firstbank.fcbcore.ffx.service.commontool.ActiveProfileResolver;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.mapper.FfxtsSerialNumberDto;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.out.repository.ffxtsserialnumber.api.FfxtsSerialNumberRepository;
import tw.com.firstbank.fcbcore.ffx.service.commontool.domain.ffxtsserialnumber.FfxtsSerialNumber;
import tw.com.firstbank.fcbcore.ffx.service.commontool.domain.ffxtsserialnumber.vo.Identifier;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration({RabbitAutoConfiguration.class, FeignAutoConfiguration.class})
@ActiveProfiles(resolver = ActiveProfileResolver.class)
class FfxtsSerialNumberRepositoryImplTest {

	private static final String SYSTEM_TYPE = "FIR";
	private static final String SERIAL_ID = "PaymentMessage2077";
	private static final String BEGIN_NO = "1";
	private static final String END_NO = "6999999";
	@Autowired
	private FfxtsSerialNumberRepository fxSerialNumberRepository;

	@Test
	void saveFfxtsSerialNumberFromEntity_WillSaveFfxtsSerialNumberInH2() {
		// given test data
		FfxtsSerialNumberDto dto = new FfxtsSerialNumberDto();
		dto.setIdentifier(new Identifier(SYSTEM_TYPE, SERIAL_ID));
		dto.setBeginNo(BEGIN_NO);
		dto.setEndNo(END_NO);

		// act
		FfxtsSerialNumber FfxtsSerialNumber = new FfxtsSerialNumber(dto);
		fxSerialNumberRepository.saveAndFlush(FfxtsSerialNumber);

		// then validate
		Optional<FfxtsSerialNumber> fxSerialNumberOptional =
				fxSerialNumberRepository.get(SYSTEM_TYPE, SERIAL_ID);
		assertTrue(fxSerialNumberOptional.isPresent());

		List<FfxtsSerialNumber> expected = List.of(fxSerialNumberOptional.get());

		assertTrue(expected.contains(FfxtsSerialNumber));
	}
}
