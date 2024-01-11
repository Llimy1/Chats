package org.project.chats.service.auth;

import lombok.Builder;
import lombok.Getter;
import org.project.chats.exception.NotFoundException;
import org.project.chats.type.ErrorMessage;

import java.util.HashMap;
import java.util.Map;

@Getter
public class OAuth2Attribute {

    private final Map<String, Object> attributes; // 사용자 속성 정보를 담는 Map
    private final String attributeKey; // 사용자 속성의 키 값
    private final String email; // 이메일 정보
    private final String name; // 이름 정보
    private final String picture; // 프로필 사진 정보
    private final String provider; // 제공자 정보

    @Builder
    private OAuth2Attribute(Map<String, Object> attributes, String attributeKey, String email, String name, String picture, String provider) {
        this.attributes = attributes;
        this.attributeKey = attributeKey;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.provider = provider;
    }

    // 서비스에 따라 OAuth2Attribute 객체를 생성하는 메서드
    static OAuth2Attribute of(String provider, String attributeKey,
                              Map<String, Object> attributes) {
        return switch (provider) {
            case "google" -> ofGoogle(provider, attributeKey, attributes);
            case "kakao" -> ofKakao(provider, "email", attributes);
            case "naver" -> ofNaver(provider, "id", attributes);
            default -> throw new NotFoundException(ErrorMessage.OAUTH_PROVIDER_NOT_FOUND);
        };
    }

    /*
     *   Google 로그인일 경우 사용하는 메서드, 사용자 정보가 따로 Wrapping 되지 않고 제공되어,
     *   바로 get() 메서드로 접근이 가능하다.
     * */
    private static OAuth2Attribute ofGoogle(String provider, String attributeKey, Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .email((String) attributes.get("email"))
                .provider(provider)
//                .name((String)attributes.get("name"))
//                .picture((String)attributes.get("picture"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    /*
     *   Kakao 로그인일 경우 사용하는 메서드, 필요한 사용자 정보가 kakaoAccount -> kakaoProfile 두번 감싸져 있어서,
     *   두번 get() 메서드를 이용해 사용자 정보를 담고있는 Map을 꺼내야한다.
     * */
    private static OAuth2Attribute ofKakao(String provider, String attributeKey, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .email((String) kakaoAccount.get("email"))
                .provider(provider)
//                .name((String) kakaoProfile.get("nickname"))
//                .picture((String) kakaoProfile.get("profile_image_url"))
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .build();
    }

    /*
     *  Naver 로그인일 경우 사용하는 메서드, 필요한 사용자 정보가 response Map에 감싸져 있어서,
     *  한번 get() 메서드를 이용해 사용자 정보를 담고있는 Map을 꺼내야한다.
     * */
    private static OAuth2Attribute ofNaver(String provider, String attributeKey, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attribute.builder()
                .email((String) response.get("email"))
                .provider(provider)
//                .name((String) response.get("name"))
//                .picture((String) response.get("profile_image"))
                .attributes(response)
                .attributeKey(attributeKey)
                .build();
    }

    // OAuth2User 객체에 넣어주기 위해서 Map으로 값들을 반환해준다.
    Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("email", email);
        map.put("provider", provider);

        return map;
    }

}
