package dev.be.feign.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static feign.Request.*;

//@Component
//@RequiredArgsConstructor
@RequiredArgsConstructor(staticName = "of") // 의존성 주입을 해주는 생성자의 accessLevel을 Private로 지정하고, 팩토리메소드 패턴을 적용해준다.
public class DemoFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        /**
         * get 요청일 경우
         * 쿼리 파라미터 출력
         */
        if (template.method() == HttpMethod.GET.name()){
            System.out.println("[GET] [DemoFeignInterceptor] queries : " + template.queries()); // 해당 요청에 사용되는 모든 Query
            return; // void일 경우 메소드 중단
        }
        /**
         * post 요청일 경우
         * body의 값을 UTF8로 인코딩하여 String문자열로 반환
         */
        String encodedRequestBody = StringUtils.toEncodedString(template.body(), StandardCharsets.UTF_8);
        System.out.println("[POST] [DemoFeignInterceptor] requestBody : " + encodedRequestBody);

        // todo. 추가적으로 본인이 필요한 로직을 추가한다.

        /**
         * 만약 Post요청일 경우 어떤 값을 바꿔줄 경우...
         * 예를들어 name 이라는 파라미터 뒤에 특정 값을 붙혀준다 라는 옵션 등에 대한 설정
         */
        String converRequestBody = encodedRequestBody;
        template.body(converRequestBody); // 컨버팅한 결과값을 기존 body에 세팅한다.
    }
}
