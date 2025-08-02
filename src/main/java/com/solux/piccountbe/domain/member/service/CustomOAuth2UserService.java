package com.solux.piccountbe.domain.member.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.solux.piccountbe.config.security.UserDetailsImpl;
import com.solux.piccountbe.domain.member.GenerateRandomCode;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.entity.MemberGroupType;
import com.solux.piccountbe.domain.member.entity.Provider;
import com.solux.piccountbe.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;

	@Value("${upload.default_profile_image}")
	private String defaultProfileImage;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

		// 카카오로부터 사용자 정보 로드
		OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
		//saveOrUpdate(oAuth2User);

		// 파싱
		Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());

		// 카카오 Id
		Map<String, Object> account = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>)account.get("profile");

		// log.info("kakao_acount: {}", account);
		// log.info("profile: {}", profile);

		Long kakaoId = (Long)attributes.get("id");
		// 닉네임
		String kakaoNickname = (String)profile.get("nickname");
		// 이메일
		String kakaoEmail = (String)account.get("email");
		// 신규유저 확인
		AtomicReference<Boolean> isOAuthNewMember = new AtomicReference<>(false);

		Member member = memberRepository.findByProviderAndOauthId(Provider.KAKAO, kakaoId)
			.map(m -> m.memberEmailUpdate(kakaoEmail))
			.orElseGet(() -> {
				isOAuthNewMember.set(true);
				String friendCode;
				do {
					friendCode = GenerateRandomCode.generateRandomCode();
				} while (memberRepository.existsByFriendCode(friendCode));

				Member newMember =
					Member.builder()
						.provider(Provider.KAKAO)
						.email(kakaoEmail)
						.oauthId(kakaoId)
						.nickname(kakaoNickname)
						.profileImageUrl(defaultProfileImage)
						.memberGroupType(MemberGroupType.STUDENT_UNIV) // 기본값
						.tokenVersion(1)
						.withdraw(false)
						.isMainVisible(false)
						.friendCode(friendCode)
						.build();
				memberRepository.save(newMember);
				return newMember;

			});

		attributes.put("isOAuthNewMember", isOAuthNewMember.get());

		return UserDetailsImpl.create(member, attributes);

	}

}
