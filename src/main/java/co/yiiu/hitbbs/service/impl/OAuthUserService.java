package co.yiiu.hitbbs.service.impl;

import co.yiiu.hitbbs.mapper.OAuthUserMapper;
import co.yiiu.hitbbs.model.OAuthUser;
import co.yiiu.hitbbs.service.IOAuthUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *
 */
@Service
@Transactional
public class OAuthUserService implements IOAuthUserService {

    @Resource
    private OAuthUserMapper oAuthUserMapper;

    @Override
    public OAuthUser selectByTypeAndLogin(String type, String login) {
        QueryWrapper<OAuthUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OAuthUser::getType, type).eq(OAuthUser::getLogin, login);
        return oAuthUserMapper.selectOne(wrapper);
    }

    @Override
    public List<OAuthUser> selectByUserId(Integer userId) {
        QueryWrapper<OAuthUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OAuthUser::getUserId, userId);
        return oAuthUserMapper.selectList(wrapper);
    }

    @Override
    public void addOAuthUser(Integer oauthId, String type, String login, String accessToken, String bio, String email,
                             Integer userId, String refreshToken, String unionId, String openId) {
        OAuthUser oAuthUser = new OAuthUser();
        oAuthUser.setOauthId(oauthId);
        oAuthUser.setType(type);
        oAuthUser.setLogin(login);
        oAuthUser.setAccessToken(accessToken);
        oAuthUser.setBio(bio);
        oAuthUser.setEmail(email);
        oAuthUser.setUserId(userId);
        oAuthUser.setInTime(new Date());
        oAuthUserMapper.insert(oAuthUser);
    }

    @Override
    public void update(OAuthUser oAuthUser) {
        oAuthUserMapper.updateById(oAuthUser);
    }
}
