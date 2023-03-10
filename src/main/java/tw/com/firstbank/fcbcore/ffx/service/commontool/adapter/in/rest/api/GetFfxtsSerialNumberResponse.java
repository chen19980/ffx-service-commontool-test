package tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.ToString;
import tw.com.firstbank.fcbcore.fcbframework.core.adapter.in.rest.api.ResponseBody;

/**
 * The type Get serial number response.
 */
@Setter
@ToString
public class GetFfxtsSerialNumberResponse implements ResponseBody {

	@JsonProperty("serialNo")
	@Schema(description = "取得號碼", example = "2")
	private String serialNo;
}
