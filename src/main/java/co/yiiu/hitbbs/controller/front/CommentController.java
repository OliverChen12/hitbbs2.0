package co.yiiu.hitbbs.controller.front;

import co.yiiu.hitbbs.model.Comment;
import co.yiiu.hitbbs.model.Topic;
import co.yiiu.hitbbs.service.ICommentService;
import co.yiiu.hitbbs.service.ITopicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Resource
    private ICommentService commentService;
    @Resource
    private ITopicService topicService;

    // 编辑评论
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Comment comment = commentService.selectById(id);
        Topic topic = topicService.selectById(comment.getTopicId());
        model.addAttribute("comment", comment);
        model.addAttribute("topic", topic);
        return render("comment/edit");
    }
}
