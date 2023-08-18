package co.yiiu.hitbbs.service.impl;

import co.yiiu.hitbbs.config.service.EmailService;
import co.yiiu.hitbbs.config.service.SmsService;
import co.yiiu.hitbbs.mapper.CodeMapper;
import co.yiiu.hitbbs.model.Code;
import co.yiiu.hitbbs.service.ICodeService;
import co.yiiu.hitbbs.util.DateUtil;
import co.yiiu.hitbbs.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
public class CodeService implements ICodeService {

    @Resource
    private CodeMapper codeMapper;
    @Resource
    private EmailService emailService;
    @Resource
    private SmsService smsService;

    // 递归生成code，防止code重复
    private String generateToken() {
        String _code = StringUtil.randomNumber(6);
        Code code = this.selectByCode(_code);
        if (code != null) {
            return this.generateToken();
        }
        return _code;
    }

    @Override
    public Code selectByCode(String _code) {
        QueryWrapper<Code> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Code::getCode, _code);
        return codeMapper.selectOne(wrapper);
    }

    // 查询没有用过的code
    @Override
    public Code selectNotUsedCode(Integer userId, String email, String mobile) {
        QueryWrapper<Code> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<Code> lambda = wrapper.lambda();
        if (email != null) {
            lambda.eq(Code::getEmail, email);
            lambda.eq(Code::getUserId, userId);
        } else if (mobile != null) {
            lambda.eq(Code::getMobile, mobile);
        }
        lambda.eq(Code::getUsed, false);
        lambda.gt(Code::getExpireTime, new Date());
        List<Code> codes = codeMapper.selectList(wrapper);
        if (codes.isEmpty()) return null;
        return codes.get(0);
    }

    // 创建一条验证码记录
    @Override
    public Code createCode(Integer userId, String email, String mobile) {
        Code code = this.selectNotUsedCode(userId, email, mobile);
        if (code == null) {
            code = new Code();
            String _code = generateToken();
            code.setUserId(userId);
            code.setCode(_code);
            code.setEmail(email);
            code.setMobile(mobile);
            code.setInTime(new Date());
            code.setExpireTime(DateUtil.getMinuteAfter(new Date(), 30));
            codeMapper.insert(code);
        }
        return code;
    }

    // 验证邮箱验证码
    @Override
    public Code validateCode(Integer userId, String email, String mobile, String _code) {
        QueryWrapper<Code> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<Code> lambda = wrapper.lambda();
        if (email != null) {
            lambda.eq(Code::getEmail, email);
            lambda.eq(Code::getUserId, userId);
        } else if (mobile != null) {
            lambda.eq(Code::getMobile, mobile);
        }
        lambda.eq(Code::getCode, _code);
        lambda.eq(Code::getUsed, false);
        lambda.gt(Code::getExpireTime, new Date());
        return codeMapper.selectOne(wrapper);
    }

    // 发送邮件
    @Override
    public boolean sendEmail(Integer userId, String email, String title, String content) {
        Code code = this.createCode(userId, email, null);
        return emailService.sendEmail(email, title, content.replace("${code}", code.getCode()));
    }

    // 发送短信
    @Override
    public boolean sendSms(String mobile) {
        // 生成code
        Code code = this.createCode(null, null, mobile);
        return smsService.sendSms(mobile, code.getCode());
    }

    @Override
    public void update(Code code) {
        codeMapper.updateById(code);
    }

    // 根据用户id删除评论记录
    @Override
    public void deleteByUserId(Integer userId) {
        QueryWrapper<Code> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Code::getUserId, userId);
        codeMapper.delete(wrapper);
    }
}
