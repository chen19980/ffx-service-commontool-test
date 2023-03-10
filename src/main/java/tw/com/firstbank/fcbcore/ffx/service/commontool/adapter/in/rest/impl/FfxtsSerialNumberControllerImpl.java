package tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.impl;

import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.com.firstbank.fcbcore.fcbframework.core.adapter.in.rest.api.RequestWrapper;
import tw.com.firstbank.fcbcore.fcbframework.core.adapter.in.rest.api.ResponseWrapper;
import tw.com.firstbank.fcbcore.fcbframework.core.adapter.in.rest.impl.BaseController;
import tw.com.firstbank.fcbcore.fcbframework.core.application.in.CommandBus;
import tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.api.FfxtsSerialNumberControllerApi;
import tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.api.GetFfxtsSerialNumberRequest;
import tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.api.GetFfxtsSerialNumberResponse;
import tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.mapper.FfxtsSerialNumberControllerMapper;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.api.GetFfxtsSerialNumberResponseCommand;

/**
 * The type of SerialNumber's controller's implementation.
 */
@AllArgsConstructor
@RestController
public class FfxtsSerialNumberControllerImpl extends BaseController
		implements FfxtsSerialNumberControllerApi {

	private static final FfxtsSerialNumberControllerMapper mapper =
			Mappers.getMapper(FfxtsSerialNumberControllerMapper.class);
	private final CommandBus commandBus;

	/**
	 * Gets a new serial number from database with input columns as conditions. If corresponding
	 * primary key is not found, a new record will also be created.
	 *
	 * @param requestWrapper the request wrapper
	 * @return the serial number
	 */
	@PostMapping("/get/v1")
	@Override
	public ResponseEntity<ResponseWrapper<GetFfxtsSerialNumberResponse>> getFfxtsSerialNumber(
			RequestWrapper<GetFfxtsSerialNumberRequest> requestWrapper) {
		GetFfxtsSerialNumberResponseCommand responseCommand =
				commandBus.handle(requestWrapper.toCommand());
		return this.makeResponseEntity(responseCommand, mapper::toGetSerialNumberResponse);
	}
}
