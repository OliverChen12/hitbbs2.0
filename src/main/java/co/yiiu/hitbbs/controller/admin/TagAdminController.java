package co.yiiu.hitbbs.controller.admin;

import co.yiiu.hitbbs.model.Tag;
import co.yiiu.hitbbs.service.ITagService;
import co.yiiu.hitbbs.util.FileUtil;
import co.yiiu.hitbbs.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
@Controller
@RequestMapping("/admin/tag")
public class TagAdminController extends BaseAdminController {

    @Resource
    private ITagService tagService;
    @Resource
    private FileUtil fileUtil;

    @RequiresPermissions("tag:list")
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNo, String name, Model model) {
//        name= SecurityUtil.sanitizeInput(name);
        if (StringUtils.isEmpty(name)) name = null;
        IPage<Tag> page = tagService.selectAll(pageNo, null, name);
        model.addAttribute("page", page);
        model.addAttribute("name", name);
        return "admin/tag/list";
    }

    @RequiresPermissions("tag:edit")
    @GetMapping("/edit")
    public String edit(Integer id, Model model) {
        model.addAttribute("tag", tagService.selectById(id));
        return "admin/tag/edit";
    }

    @RequiresPermissions("tag:edit")
    @PostMapping("/edit")
    public String update(Integer id, String name, String description, Integer topicCount, MultipartFile file) {
        Tag tag = tagService.selectById(id);
        tag.setName(name);
        tag.setDescription(description);
        tag.setTopicCount(topicCount);
        String path = fileUtil.upload(file, null, "tag");
        tag.setIcon(path);
        tagService.update(tag);
        return redirect("/admin/tag/list");
    }

    @RequiresPermissions("tag:delete")
    @GetMapping("/delete")
    @ResponseBody
    public Result delete(Integer id) {
        Tag tag = tagService.selectById(id);
        if (tag.getTopicCount() > 0) return error("标签还关联着话题，要先把相关联的话题都删了，这个标签才能删除");
        tagService.delete(id);
        return success();
    }

    // 同步标签的话题数
    @RequiresPermissions("tag:async")
    @GetMapping("/async")
    @ResponseBody
    public Result async() {
        tagService.async();
        return success();
    }
}
