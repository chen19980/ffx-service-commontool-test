package tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tw.com.firstbank.fcbcore.fcbframework.core.application.in.BaseRequestCommand;

/**
 * The type Get serial number request command.
 */
@Getter
@Setter
@ToString
public class GetFfxtsSerialNumberRequestCommand extends BaseRequestCommand {

	private String systemType;
	private String serialId;
	private String beginNo;
	private String endNo;
}
