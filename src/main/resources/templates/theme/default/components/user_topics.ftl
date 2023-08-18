<#macro user_topics pageNo pageSize username isPaginate=false isFooter=false>
    <div class="card">
        <@tag_user_topics username=username pageNo=pageNo pageSize=pageSize>
            <div class="card-header">${username}创建的话题</div>
            <#if topics.total == 0>
                <div class="card-body">
                    暂无话题
                </div>
            <#else>
                <div class="card-body paginate-bot">
                    <#list topics.records as topic>
                        <div class="media">
                            <div class="media-body">
                                <div class="title">
                                    <a href="/topic/${topic.id}">
                                        ${topic.title!?html}
                                    </a>
                                </div>
                                <div>
                                    <span><a href="/user/${topic.username}">${topic.username}</a></span>
                                    <span class="hidden-sm hidden-xs">•</span>
                                    <span class="hidden-sm hidden-xs"><a
                                                href="/topic/${topic.id}">${topic.commentCount}个评论</a></span>
                                    <span class="hidden-sm hidden-xs">•</span>
                                    <span class="hidden-sm hidden-xs">${topic.view}次浏览</span>
                                    <span>•</span>
                                    <span>${model.formatDate(topic.inTime)}</span>
                                </div>
                            </div>
                        </div>
                        <#if topic_has_next>
                            <div class="divide mt-2 mb-2"></div>
                        </#if>
                    </#list>
                    <#if isPaginate>
                        <#include "paginate.ftl"/>
                        <@paginate currentPage=topics.current totalPage=topics.pages actionUrl="/user/${username}/topics" urlParas=""/>
                    </#if>
                </div>
                <#if isFooter>
                    <div class="card-footer">
                        <a href="/user/${username}/topics">${username}更多话题&gt;&gt;</a>
                    </div>
                </#if>
            </#if>
        </@tag_user_topics>
    </div>
</#macro>
