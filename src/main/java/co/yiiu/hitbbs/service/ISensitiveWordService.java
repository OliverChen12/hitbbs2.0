package co.yiiu.hitbbs.service;

import co.yiiu.hitbbs.model.SensitiveWord;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *
 */
public interface ISensitiveWordService {
    void save(SensitiveWord sensitiveWord);

    void update(SensitiveWord sensitiveWord);

    List<SensitiveWord> selectAll();

    void deleteById(Integer id);

    IPage<SensitiveWord> page(Integer pageNo, String word);

    void updateWordById(Integer id, String word);

    SensitiveWord selectByWord(String word);
}
