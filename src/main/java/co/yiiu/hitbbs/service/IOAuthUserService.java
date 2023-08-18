package co.yiiu.hitbbs.service;

import co.yiiu.hitbbs.model.OAuthUser;

import java.util.List;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
public interface IOAuthUserService {
    OAuthUser selectByTypeAndLogin(String type, String login);

    List<OAuthUser> selectByUserId(Integer userId);

    void addOAuthUser(Integer oauthId, String type, String login, String accessToken, String bio, String email, Integer
            userId, String refreshToken, String unionId, String openId);

    void update(OAuthUser oAuthUser);
}
