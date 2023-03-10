package tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tw.com.firstbank.fcbcore.fcbframework.core.application.in.CommandHandler;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.FfxtsSerialNumberService;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.api.GetFfxtsSerialNumberRequestCommand;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.api.GetFfxtsSerialNumberResponseCommand;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.api.GetFfxtsSerialNumberUseCaseApi;

/**
 * The type Get serial number use case.
 */
@Service
@AllArgsConstructor
public class GetFfxtsSerialNumberUseCaseImpl
		implements CommandHandler, GetFfxtsSerialNumberUseCaseApi {

	private final FfxtsSerialNumberService fxSerialNumberService;

	/**
	 * Execute get serial number response command.
	 *
	 * @param requestCommand the get serial number request command
	 * @return the get serial number response command
	 */
	@Override
	public GetFfxtsSerialNumberResponseCommand execute(
			GetFfxtsSerialNumberRequestCommand requestCommand) {
		GetFfxtsSerialNumberResponseCommand responseCommand = new GetFfxtsSerialNumberResponseCommand();

		String result = fxSerialNumberService.tryGetSerialNumber(requestCommand.getSystemType(),
				requestCommand.getSerialId(), requestCommand.getBeginNo(), requestCommand.getEndNo());

		responseCommand.setSerialNo(result);

		return responseCommand;
	}
}
