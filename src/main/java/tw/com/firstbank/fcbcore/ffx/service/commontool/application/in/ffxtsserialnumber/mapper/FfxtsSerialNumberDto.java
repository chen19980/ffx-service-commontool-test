package tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.mapper;

import lombok.Getter;
import lombok.Setter;
import tw.com.firstbank.fcbcore.ffx.service.commontool.domain.ffxtsserialnumber.vo.Identifier;

/**
 * The data transfer object of FfxtsSerialNumber.
 */
@Setter
@Getter
public class FfxtsSerialNumberDto {

	private Identifier identifier;

	private String beginNo;

	private String endNo;
}
