package dev.be.feign.feign.client;

import dev.be.feign.common.dto.BaseRequestInfo;
import dev.be.feign.common.dto.BaseResponseInfo;
import dev.be.feign.feign.config.DemoFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "demo-client", /* 해당 인터페이스를 지칭하는 유일한 PK 역할을 해주는 값 */
        url = "${feign.url.prefix}", /* 현재 Client가 요청을 보내고자 하는 Target이 되는 Server의 URL 값 */
        configuration = DemoFeignConfig.class
)
public interface DemoFeignClient {

    @GetMapping("/get") // @FeignClient의 url에 지정한 application.yaml파일의 prefix 주소값이 앞에 붙게됨 - http://localhost:8080/target-server/get
    ResponseEntity<BaseResponseInfo> callGet(@RequestHeader("CustomHeaderName") String customHeader,
                                             @RequestParam("name") String name, @RequestParam("age") Long age);

    @PostMapping("/post")
    ResponseEntity<BaseResponseInfo> callPost(@RequestHeader("CustomHeaderName") String customHeader,
                                             @RequestBody BaseRequestInfo baseRequestInfo);
    @GetMapping("/error")
    ResponseEntity<BaseResponseInfo> callErrorDecoder();
}
