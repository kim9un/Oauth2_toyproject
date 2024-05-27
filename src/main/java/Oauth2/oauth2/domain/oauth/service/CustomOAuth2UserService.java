package Oauth2.oauth2.domain.oauth.service;

import Oauth2.oauth2.domain.member.entity.Member;
import Oauth2.oauth2.domain.member.repository.MemberRepository;
import Oauth2.oauth2.domain.oauth.dto.CustomOAuth2User;
import Oauth2.oauth2.domain.oauth.dto.KakaoResponse;
import Oauth2.oauth2.domain.oauth.dto.UserDTO;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("oAuth2User :" + oAuth2User);

        KakaoResponse oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        Member existData = memberRepository.findByUsername(username);

        if (existData == null) {

            Member userEntity = new Member();
            userEntity.setUsername(username);
            userEntity.setName(oAuth2Response.getNickname());
            userEntity.setRole("ROLE_USER");

            memberRepository.save(userEntity);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getNickname());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }
        else {

            existData.setName(oAuth2Response.getNickname());

            memberRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setName(oAuth2Response.getNickname());
            userDTO.setRole(existData.getRole());

            return new CustomOAuth2User(userDTO);
        }
    }
}

