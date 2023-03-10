package tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.api.GetFfxtsSerialNumberRequest;
import tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.api.GetFfxtsSerialNumberResponse;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.api.GetFfxtsSerialNumberRequestCommand;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.api.GetFfxtsSerialNumberResponseCommand;

/**
 * The interface of SerialNumber Controller's Mapper.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface FfxtsSerialNumberControllerMapper {

	/**
	 * Maps a GetSerialNumberRequest into GetSerialNumberRequestCommand.
	 *
	 * @param source the source
	 * @return the get serial number request command
	 */
	GetFfxtsSerialNumberRequestCommand toGetSerialNumberRequestCommand(
			GetFfxtsSerialNumberRequest source);

	/**
	 * Maps a GetSerialNumberResponseCommand into GetSerialNumberResponse.
	 *
	 * @param source the source
	 * @return the get serial number response
	 */
	GetFfxtsSerialNumberResponse toGetSerialNumberResponse(
			GetFfxtsSerialNumberResponseCommand source);
}
