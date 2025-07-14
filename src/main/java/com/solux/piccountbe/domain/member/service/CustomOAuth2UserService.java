package com.solux.piccountbe.domain.member.service;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.solux.piccountbe.config.security.UserDetailsImpl;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.entity.Provider;
import com.solux.piccountbe.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

		// 카카오로부터 사용자 정보 로드
		OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
		//saveOrUpdate(oAuth2User);

		//그냥 바로 여기서 파싱
		Map<String, Object> attributes = oAuth2User.getAttributes();

		// 카카오 Id
		Map<String, Object> account = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>)account.get("profile");

		log.info("kakao_acount: {}", account);
		log.info("profile: {}", profile);

		if (account == null || profile == null) {
			return null;
		}

		Long kakaoId = (Long)attributes.get("id");
		// 닉네임
		String kakaoNickname = (String)profile.get("nickname");
		// 이메일
		String kakaoEmail = (String)account.get("email");

		Member member = memberRepository.findByProviderAndOauthId(Provider.KAKAO, kakaoId)
			.map(m -> m.memberUpdate(kakaoEmail))
			.orElseGet(() -> memberRepository.save(
				Member.builder()
					.provider(Provider.KAKAO)
					.email(kakaoEmail)
					.oauthId(kakaoId)
					.nickname(kakaoNickname)
					.profileImageUrl("images/default-member-profile.jpg") //임시 defaultImageUrl
					.withdraw(false)
					.isMainVisible(false)
					.friendCode("WRECKITR") // 임시설정
					.build()
			));

		return UserDetailsImpl.create(member, attributes);

		// public
		// Member(Provider provider, String email, String password, String nickname, String profileImageUrl, Gender gender, String friendCode, Integer age, Boolean withdraw, Boolean isMainVisible) {
		// 	this.provider = provider;
		// 	this.email = email;
		// 	this.password = password;
		// 	this.nickname = nickname;
		// 	this.profileImageUrl = profileImageUrl;
		// 	this.gender = gender;
		// 	this.friendCode = friendCode;
		// 	this.age = age;
		// 	this.withdraw = withdraw;
		// 	this.isMainVisible = isMainVisible;
		// 	));

	}

}

// 이메일 꺼내기

// // 프로바이더 구분
// String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
//
// // 프로바이더별 userINfo 파싱
// OAuth2UserInfo userInfo = oAuth2UserInfoFactory.getOAuth2UserInfo(
// 	registrationId, oAuth2User.getAttributes());
//
// //DB에 이미 있으면 업데이트, 없으면 회원ㄱ아ㅣㅂ
// Member member = registerOrUpdateMember(registrationId, userInfo);
//
// //시큐리티 컨텍스트에 담을  User 객체 반환
// return UserPrincipal.create(member, oAuth2User.getAttributes());
//}

// private Member registerOrUpdateMember(String registrationId, OAuth2UserInfo userInfo) {
// 	return memberRepository.findByProviderAndProviderId(registrationId, userInfo.getId())
// 		.map(existing -> existing.update(userInfo.getName(), userInfo.getEmail()))
// 		.orElseGet(() -> memberRepository.save(
// 			Member.builder()
// 				.provider(registrationId)
// 				.providerId(userInfo.getId())
// 				.email(userInfo.getEmail())
// 				.name(userInfo.getName())
// 				// .role(Role.USER)
// 				.build()
// 		));
//
// }

