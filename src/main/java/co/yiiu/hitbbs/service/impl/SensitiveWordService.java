package co.yiiu.hitbbs.service.impl;

import co.yiiu.hitbbs.mapper.SensitiveWordMapper;
import co.yiiu.hitbbs.model.SensitiveWord;
import co.yiiu.hitbbs.service.ISensitiveWordService;
import co.yiiu.hitbbs.service.ISystemConfigService;
import co.yiiu.hitbbs.util.MyPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
@Service
@Transactional
public class SensitiveWordService implements ISensitiveWordService {

    @Resource
    private ISystemConfigService systemConfigService;

    @Resource
    private SensitiveWordMapper sensitiveWordMapper;

    @Override
    public void save(SensitiveWord sensitiveWord) {
        sensitiveWordMapper.insert(sensitiveWord);
    }

    @Override
    public void update(SensitiveWord sensitiveWord) {
        sensitiveWordMapper.updateById(sensitiveWord);
    }

    @Override
    public List<SensitiveWord> selectAll() {
        return sensitiveWordMapper.selectList(null);
    }

    @Override
    public void deleteById(Integer id) {
        sensitiveWordMapper.deleteById(id);
    }

    // ---------admin------------

    @Override
    public IPage<SensitiveWord> page(Integer pageNo, String word) {
        IPage<SensitiveWord> iPage = new MyPage<>(pageNo, Integer.parseInt(systemConfigService.selectAllConfig().get
                ("page_size").toString()));
        QueryWrapper<SensitiveWord> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(word)) wrapper.lambda().eq(SensitiveWord::getWord, word);
        return sensitiveWordMapper.selectPage(iPage, wrapper);
    }

    @Override
    public void updateWordById(Integer id, String word) {
        SensitiveWord sensitiveWord = new SensitiveWord();
        sensitiveWord.setId(id);
        sensitiveWord.setWord(word);
        sensitiveWordMapper.updateById(sensitiveWord);
    }

    @Override
    public SensitiveWord selectByWord(String word) {
        QueryWrapper<SensitiveWord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SensitiveWord::getWord, word);
        return sensitiveWordMapper.selectOne(wrapper);
    }
}
