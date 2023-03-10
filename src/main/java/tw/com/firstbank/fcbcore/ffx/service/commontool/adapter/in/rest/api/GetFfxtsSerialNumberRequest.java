package tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mapstruct.factory.Mappers;
import tw.com.firstbank.fcbcore.fcbframework.core.adapter.in.rest.api.RequestBody;
import tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.mapper.FfxtsSerialNumberControllerMapper;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.api.GetFfxtsSerialNumberRequestCommand;

/**
 * The type Get serial number request.
 */
@Getter
@Setter
@ToString
public class GetFfxtsSerialNumberRequest implements RequestBody {

	private static final FfxtsSerialNumberControllerMapper mapper =
			Mappers.getMapper(FfxtsSerialNumberControllerMapper.class);

	@JsonProperty("systemType")
	@NotBlank(message = "{ffxtsserialnumber.systemtype}")
	@Size(min = 1, max = 3, message = "{ffxtsserialnumber.systemtype}")
	@Schema(description = "系統別", example = "IR")
	private String systemType;

	@JsonProperty("serialId")
	@NotBlank(message = "{ffxtsserialnumber.serialid}")
	@Size(min = 1, max = 50, message = "{ffxtsserialnumber.serialid}")
	@Schema(description = "序號類別", example = "PaymentMessage2022")
	private String serialId;

	@JsonProperty("beginNo")
	@NotNull(message = "{ffxtsserialnumber.beginno}")
	@Size(min = 1, max = 10, message = "{ffxtsserialnumber.beginno}")
	@Schema(description = "起始序號", example = "1")
	private String beginNo;

	@JsonProperty("endNo")
	@NotNull(message = "{ffxtsserialnumber.endno}")
	@Size(min = 1, max = 10, message = "{ffxtsserialnumber.endno}")
	@Schema(description = "結束序號", example = "99999999")
	private String endNo;

	/**
	 * To command get serial number request command.
	 *
	 * @return the get serial number request command
	 */
	@Override
	public GetFfxtsSerialNumberRequestCommand toCommand() {
		return mapper.toGetSerialNumberRequestCommand(this);
	}
}
