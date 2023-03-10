package tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import tw.com.firstbank.fcbcore.fcbframework.core.adapter.in.rest.api.RequestWrapper;
import tw.com.firstbank.fcbcore.fcbframework.core.adapter.in.rest.api.ResponseWrapper;

/**
 * The interface of SerialNumber's controller.
 */
@Validated
@Tag(name = "取號服務 API", description = "提供取號相關 API 操作，可以取得並更新既有序號，或是新增序號")
@RequestMapping("/na/ffx/ts/ffxtsserialnumber")
public interface FfxtsSerialNumberControllerApi {

	/**
	 * Gets a new serial number from database with input columns as conditions. If corresponding
	 * primary key is not found, a new record will also be created.
	 *
	 * @param requestWrapper the request request wrapper
	 * @return the serial number
	 */
	@Operation(summary = "取得序號", description = "取得並更新既有序號，或新增序號")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "驗證失敗",
					content = {@Content(mediaType = "application/json",
							schema = @Schema(implementation = RequestWrapper.class))}),
			@ApiResponse(responseCode = "500", description = "伺服器內部錯誤",
					content = {@Content(mediaType = "application/json",
							schema = @Schema(implementation = RequestWrapper.class))})})
	ResponseEntity<ResponseWrapper<GetFfxtsSerialNumberResponse>> getFfxtsSerialNumber(
			@Valid @RequestBody final RequestWrapper<GetFfxtsSerialNumberRequest> requestWrapper);
}
