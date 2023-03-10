package tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tw.com.firstbank.fcbcore.fcbframework.core.application.ClientHeaderLegacy;
import tw.com.firstbank.fcbcore.fcbframework.core.application.in.CommandBus;
import tw.com.firstbank.fcbcore.ffx.service.commontool.ActiveProfileResolver;
import tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.api.FfxtsSerialNumberControllerApi;
import tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.api.GetFfxtsSerialNumberRequest;
import tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.impl.FfxtsSerialNumberControllerImpl;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.api.GetFfxtsSerialNumberRequestCommand;
import tw.com.firstbank.fcbcore.ffx.service.commontool.application.in.ffxtsserialnumber.api.GetFfxtsSerialNumberResponseCommand;

@WebMvcTest
@ImportAutoConfiguration({RabbitAutoConfiguration.class, FeignAutoConfiguration.class})
@ActiveProfiles(resolver = ActiveProfileResolver.class)
class FfxtsSerialNumberControllerTest {

	private static final String SYSTEM_TYPE = "FIR";
	private static final String SERIAL_ID = "PaymentMessage2077";
	private static final String BEGIN_NO = "1";
	private static final String END_NO = "6999999";
	private final HttpHeaders httpHeaders = new HttpHeaders();
	@Mock
	private CommandBus commandBus;
	@Autowired
	private ObjectMapper objectMapper;
	private MockMvc mockMvc;

	@Test
	void getSerialNumber_WillExecuteGetFfxtsSerialNumber_AndReturnNewSerialNumber() throws Exception {
		GetFfxtsSerialNumberRequest getFxBranchRequest = new GetFfxtsSerialNumberRequest();

		getFxBranchRequest.setSystemType(SYSTEM_TYPE);
		getFxBranchRequest.setSerialId(SERIAL_ID);
		getFxBranchRequest.setBeginNo(BEGIN_NO);
		getFxBranchRequest.setEndNo(END_NO);

		// Request client Header
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("terminalId", "terminalId");
		requestMap.put("empId", "empId");
		requestMap.put("txId", "txId");
		requestMap.put("txSeqNo", "txSeqNo");
		requestMap.put("businessDate", "businessDate");
		requestMap.put("requestTimeStamp", "123456");
		requestMap.put("transactionDateTime", "2022-11-18T15:06:000000");
		requestMap.put(ClientHeaderLegacy.BRANCH_ID, "093");
		requestMap.put(ClientHeaderLegacy.PREFIX, "A");
		requestMap.put(ClientHeaderLegacy.BRANCH_NO, "093");
		requestMap.put(ClientHeaderLegacy.OP_ID, "AA01");
		requestMap.put(ClientHeaderLegacy.TRANSACTION_ID, "transactionId");
		requestMap.put(ClientHeaderLegacy.CLIENT_IP, "127.0.0.1");
		requestMap.put(ClientHeaderLegacy.MEDIA_TRAN_STATUS, "test");
		requestMap.put(ClientHeaderLegacy.MEDIA_OCCUR, "0");
		requestMap.put("clientRequest", getFxBranchRequest);



		String requestJson = objectMapper.writeValueAsString(requestMap);

		// Response Client Body
		GetFfxtsSerialNumberResponseCommand responseCommand = new GetFfxtsSerialNumberResponseCommand();
		responseCommand.setSerialNo(BEGIN_NO);

		when(commandBus.handle(any())).thenReturn(responseCommand);

		// act
		mockMvc
				.perform(post("/na/ffx/ts/ffxtsserialnumber/get/v1").headers(httpHeaders)
						.contentType(MediaType.APPLICATION_JSON).content(requestJson).characterEncoding("utf-8")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				// 驗證client Body
				.andExpect(jsonPath("$.clientResponse.serialNo").value(responseCommand.getSerialNo()));

		// validate
		verify(commandBus).handle(any(GetFfxtsSerialNumberRequestCommand.class));
	}

	@BeforeEach
	public void setup() {
		httpHeaders.add("x-core-channel", "channel");
		httpHeaders.add("x-core-traceid", "traceId");
		httpHeaders.add("x-core-uid", "uid");
		httpHeaders.add("x-core-txid", "txid");
		httpHeaders.add("x-core-timestamp", "2022-09-01T10:01:400480");
		httpHeaders.add("x-core-msg-type", "");
		httpHeaders.add("Accept-Language", "zh-TW");
		httpHeaders.add("Accept-Charset", "utf-8");

		FfxtsSerialNumberControllerApi fxSerialNumberController =
				new FfxtsSerialNumberControllerImpl(commandBus);
		mockMvc = MockMvcBuilders.standaloneSetup(fxSerialNumberController).build();
	}
}
