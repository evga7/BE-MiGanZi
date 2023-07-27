package com.StreetNo5.StreetNo5.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info().title("미간지 API")
                        .description("미간지 API 명세서 입니다." +
                                "토큰이 필요한 작업시 오른쪽에 Authorize 버튼을 눌러 토큰값을 삽입해주세요. POST 기능도 잘 동작합니다." +
                                "글쓰기 기능은 이미지파일 때문에 스웨거에서 동작이 안됩니다. 만약 필요하다면 포스트맨으로 테스트해주세요." +
                                "(동작하게 수정은 가능하지만 프론트도 변경해야해서.. 변경하지 않겠습니다.)")
                        .version("1.0").contact(new Contact().name("BLUE")));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}