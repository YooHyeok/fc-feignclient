package dev.be.feign.feign.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 일반적인 API에서 ControllerAdvice에서 Exception을 핸들링하는 작업을 이곳에서 하게 된다.
 * AsyncResponseHandler 클래스의 handleResponse() 호출에 의해 내부적으로 현재 오버라이딩 된 decode() 메소드가 호출된다
 */
public class DemoFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    /**
     * 현재 응답 상태코드를 HttpStatus 객체로 반환한 뒤
     * 일치하는 상태코드에 대한 Exception을 Wrapping하여 Error에 대한 응답을 핸들링한다.
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus httpStatus = HttpStatus.resolve(response.status()); // break point
        if (httpStatus == HttpStatus.NOT_FOUND) {
            System.out.println("[DemoFeignErrorDecoder] Http Status = " + httpStatus);
            throw new RuntimeException(String.format("[RuntimeException] Http Status is %s ", httpStatus));
        }
        return errorDecoder.decode(methodKey, response);
    }
}
