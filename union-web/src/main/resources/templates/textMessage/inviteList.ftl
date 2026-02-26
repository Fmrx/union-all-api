【邀请统计】
总邀请人数：${totalInviteNum}人
有效邀请人数：${effectiveInviteNum}人

【邀请用户列表】
<#list inviteUserList as user>
${user_index + 1}. ${user.nickName} - <#if user.effective>有效<#else>无效</#if>
</#list>

邀请好友注册并完成首单，即可获得奖励！
