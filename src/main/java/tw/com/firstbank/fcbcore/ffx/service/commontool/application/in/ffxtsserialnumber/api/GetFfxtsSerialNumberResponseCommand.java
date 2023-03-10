package tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tw.com.firstbank.fcbcore.fcbframework.core.application.in.ResponseCommand;

/**
 * The type Get serial number response command.
 */
@Getter
@Setter
@ToString
public class GetFfxtsSerialNumberResponseCommand implements ResponseCommand {

	private String serialNo;
}
