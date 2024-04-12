package dev.be.feign.feign.logger;

import feign.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import static feign.Util.*;


@RequiredArgsConstructor(staticName = "of")
public class FeignCustomLogger extends Logger {

    /**
     * elapsedTime
     * 특정 요청에 대한 응답이 오랜시간 걸렸다면, 언젠가는 TimeOut 익셉션 발생 가능성이 있으므로 주의깊게 봐야한다.
     * 내가 지정한 시간보다 오래걸렸을 경우 SlowAPI라는 텍스트를 노출해주는 코드
     */
    private static final int DEFAULT_SLOW_API_TIME = 3_000;
    private static final String SLOW_API_NOTICE = "Slow API";



    /**
     * 실제 로그를 출력하는 메소드
     * [클래스#메소드명] format: 값
     */
    @Override
    protected void log(String configKey, String format, Object... args) {
        System.out.println(String.format(methodTag(configKey) + format, args));
    }

    /**
     * request 핸들링 로그를 출력하는 메소드
     */
    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        System.out.println("[logRequest] : " + request);
    }

    /**
     * request와 response를 모두 핸들링한다.
     * 시작시 프로토콜 버전 출력 및 Loop를 통해 header에 들어있는 값들을 출력한다.
     */
    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        String protocolVersion = resolveProtocolVersion(response.protocolVersion());
        String reason =
                response.reason() != null && logLevel.compareTo(Level.NONE) > 0 ? " " + response.reason()
                        : "";
        int status = response.status();
        log(configKey, "<--- %s %s%s (%sms)", protocolVersion, status, reason, elapsedTime);
        if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {

            for (String field : response.headers().keySet()) {
                if (shouldLogResponseHeader(field)) {
                    for (String value : valuesOrEmpty(response.headers(), field)) {
                        log(configKey, "%s: %s", field, value);
                    }
                }
            }

            int bodyLength = 0;
            if (response.body() != null && !(status == 204 || status == 205)) {
                // HTTP 204 No Content "...response MUST NOT include a message-body"
                // HTTP 205 Reset Content "...response MUST NOT include an entity"
                if (logLevel.ordinal() >= Level.FULL.ordinal()) {
                    log(configKey, ""); // CRLF
                }
                byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                bodyLength = bodyData.length;
                if (logLevel.ordinal() >= Level.FULL.ordinal() && bodyLength > 0) {
                    log(configKey, "%s", decodeOrDefault(bodyData, UTF_8, "Binary data"));
                }

                /* Slow API*/
                if (elapsedTime > DEFAULT_SLOW_API_TIME) {
                    log(configKey, "[%s] elapsedTime: %s", SLOW_API_NOTICE, elapsedTime);
                }

                log(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
                return response.toBuilder().body(bodyData).build();
            } else {
                log(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
            }
        }
        return response;
    }
}
