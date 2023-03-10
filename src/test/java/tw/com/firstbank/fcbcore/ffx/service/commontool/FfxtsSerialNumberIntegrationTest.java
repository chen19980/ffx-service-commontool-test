package tw.com.firstbank.fcbcore.ffx.service.commontool;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import tw.com.firstbank.fcbcore.fcbframework.core.application.ClientHeaderLegacy;
import tw.com.firstbank.fcbcore.fcbframework.core.application.CoreHeaderLegacy;
import tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.in.rest.api.GetFfxtsSerialNumberRequest;
import tw.com.firstbank.fcbcore.ffx.service.commontool.adapter.out.repository.ffxtsserialnumber.api.FfxtsSerialNumberJpaRepository;
import tw.com.firstbank.fcbcore.ffx.service.commontool.domain.ffxtsserialnumber.FfxtsSerialNumber;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ImportAutoConfiguration({RabbitAutoConfiguration.class, FeignAutoConfiguration.class})
@ActiveProfiles(resolver = ActiveProfileResolver.class)
class FfxtsSerialNumberIntegrationTest {

	private static final String SYSTEM_TYPE = "FIR";
	private static final String SERIAL_ID = "Test_Template_Id";
	private static final String BEGIN_NO = "AA0001";
	private static final String END_NO = "ZZ0001";

	private static WireMockServer wireMockServer;
	@Autowired
	private TestRestTemplate testRestTemplate;
	@Autowired
	private FfxtsSerialNumberJpaRepository ffxtsSerialNumberJpaRepository;
	@Autowired
	private ObjectMapper objectMapper;
	@LocalServerPort
	private int localPort;

	@BeforeAll
	public static void setup() {
		wireMockServer = new WireMockServer(8085);
		wireMockServer.start();

		WireMock.configureFor(wireMockServer.port());
	}

	@AfterAll
	public static void teardown() {
		wireMockServer.stop();
	}

	@Test
	void getFfxtsSerialNumber_WillReturn_CreatedSerialNumber() throws JsonProcessingException {
		// Arrange
		String baseURI = "http://localhost:" + localPort;

		// Request Http Header
		HttpHeaders httpHeaders = getHttpHeader();

		// Request Client Body
		GetFfxtsSerialNumberRequest request = new GetFfxtsSerialNumberRequest();
		request.setSystemType(SYSTEM_TYPE);
		request.setSerialId(SERIAL_ID);
		request.setBeginNo(BEGIN_NO);
		request.setEndNo(END_NO);

		// Request Client Header
		Map<String, Object> requestMap = getClientRequestHeader(request);

		String requestJson = objectMapper.writeValueAsString(requestMap);

		HttpEntity<String> entity = new HttpEntity<>(requestJson, httpHeaders);

		// Act

		ResponseEntity<String> responseEntity = testRestTemplate.exchange(
				baseURI + "/na/ffx/ts/ffxtsserialnumber/get/v1", HttpMethod.POST, entity, String.class);

		// Assert
		// 驗證Http Status
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

		// 驗證Http Header
		assertEquals(httpHeaders.getFirst("x-core-channel"),
				responseEntity.getHeaders().getFirst("x-core-channel"));
		assertEquals(httpHeaders.getFirst("x-core-traceid"),
				responseEntity.getHeaders().getFirst("x-core-traceid"));
		assertEquals(httpHeaders.getFirst("x-core-uid"),
				responseEntity.getHeaders().getFirst("x-core-uid"));
		assertEquals(httpHeaders.getFirst("x-core-txid"),
				responseEntity.getHeaders().getFirst("x-core-txid"));
		assertEquals(httpHeaders.getFirst("x-core-timestamp"),
				responseEntity.getHeaders().getFirst("x-core-timestamp"));
		assertEquals(httpHeaders.getFirst("x-core-type"),
				responseEntity.getHeaders().getFirst("x-core-type"));

		// 驗證client Header
		assertNotNull(responseEntity.getBody());

		Map<String, Object> response =
				objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {});

		assertEquals("0000", response.get("statusCode"));

		// 驗證client Body
		Optional<FfxtsSerialNumber> queryResultOptional =
				ffxtsSerialNumberJpaRepository.findByIdentifierSystemTypeAndIdentifierSerialId(
						request.getSystemType(), request.getSerialId());

		assertThat(queryResultOptional).isNotNull().isNotEmpty();

		FfxtsSerialNumber queryResult = queryResultOptional.get();

		assertThat(queryResult.getIdentifier().getSystemType()).isEqualTo(request.getSystemType());
		assertThat(queryResult.getIdentifier().getSerialId()).isEqualTo(request.getSerialId());
		assertThat(queryResult.getBeginNo()).isEqualTo(request.getBeginNo());
		assertThat(queryResult.getEndNo()).isEqualTo(request.getEndNo());
		assertThat(queryResult.getLastNo()).isEqualTo(request.getBeginNo());
	}

	private HttpHeaders getHttpHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(CoreHeaderLegacy.X_CORE_CHANNEL, "channel");
		httpHeaders.add(CoreHeaderLegacy.X_CORE_TRACEID, "traceId");
		httpHeaders.add(CoreHeaderLegacy.X_CORE_UID, "uid");
		httpHeaders.add(CoreHeaderLegacy.X_CORE_TXID, "txid");
		httpHeaders.add(CoreHeaderLegacy.X_CORE_TIMESTAMP, "2022-09-01T10:01:400480");
		httpHeaders.add(CoreHeaderLegacy.X_CORE_MSG_TYPE, "");
		httpHeaders.add(CoreHeaderLegacy.ACCEPT_LANGUAGE, "zh-TW");
		httpHeaders.add(CoreHeaderLegacy.ACCEPT_CHARSET, "utf-8");
		httpHeaders.add(CoreHeaderLegacy.CONTENT_TYPE, "application/json");

		return httpHeaders;
	}

	private Map<String, Object> getClientRequestHeader(Object clientRequest) {
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put(ClientHeaderLegacy.TERMINAL_ID, "terminalId");
		requestMap.put(ClientHeaderLegacy.EMP_ID, "empId");
		requestMap.put(ClientHeaderLegacy.TX_ID, "txId");
		requestMap.put(ClientHeaderLegacy.TX_SEQ_NO, "txSeqNo");
		requestMap.put(ClientHeaderLegacy.BUSINESS_DATE, "businessDate");
		requestMap.put(ClientHeaderLegacy.TRANSACTION_DATETIME, "123456");
		requestMap.put(ClientHeaderLegacy.BRANCH_ID, "093");
		requestMap.put(ClientHeaderLegacy.PREFIX, "A");
		requestMap.put(ClientHeaderLegacy.BRANCH_NO, "093");
		requestMap.put(ClientHeaderLegacy.OP_ID, "AA01");
		requestMap.put(ClientHeaderLegacy.TRANSACTION_ID, "transactionId");
		requestMap.put(ClientHeaderLegacy.CLIENT_IP, "127.0.0.1");
		requestMap.put(ClientHeaderLegacy.AUTH_EMP_ID, "234567");
		requestMap.put(ClientHeaderLegacy.MEDIA_TRAN_STATUS, "test");
		requestMap.put(ClientHeaderLegacy.MEDIA_OCCUR, "0");
		requestMap.put("clientRequest", clientRequest);

		return requestMap;
	}

	@BeforeEach
	public void reset() {
		wireMockServer.resetAll();
	}
}
