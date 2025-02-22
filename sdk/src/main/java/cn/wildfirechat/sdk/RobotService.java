package cn.wildfirechat.sdk;

import cn.wildfirechat.common.APIPath;
import cn.wildfirechat.common.ErrorCode;
import cn.wildfirechat.pojos.*;
import cn.wildfirechat.pojos.moments.*;
import cn.wildfirechat.sdk.model.IMResult;
import cn.wildfirechat.sdk.utilities.RobotHttpUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

import static cn.wildfirechat.proto.ProtoConstants.ApplicationType.ApplicationType_Robot;

public class RobotService {
    private final RobotHttpUtils robotHttpUtils;

    public RobotService(String url, String robotId, String robotSecret) {
        robotHttpUtils = new RobotHttpUtils(url, robotId, robotSecret);
    }

    public String getRobotId() {
        return robotHttpUtils.getRobotId();
    }

    public IMResult<SendMessageResult> sendMessage(String sender, Conversation conversation, MessagePayload payload) throws Exception {
        return sendMessage(sender, conversation, payload, null);
    }

    public IMResult<SendMessageResult> sendMessage(String sender, Conversation conversation, MessagePayload payload, List<String> toUsers) throws Exception {
        String path = APIPath.Robot_Message_Send;
        SendMessageData messageData = new SendMessageData();
        messageData.setSender(sender);
        messageData.setConv(conversation);
        messageData.setToUsers(toUsers);
        messageData.setPayload(payload);
        return robotHttpUtils.httpJsonPost(path, messageData, SendMessageResult.class);
    }

    public IMResult<String> recallMessage(long messageUid) throws Exception {
        String path = APIPath.Robot_Message_Recall;
        RecallMessageData messageData = new RecallMessageData();
        messageData.setMessageUid(messageUid);
        return robotHttpUtils.httpJsonPost(path, messageData, String.class);
    }

    public IMResult<Void> updateMessage(long messageUid, MessagePayload payload) throws Exception {
        String path = APIPath.Robot_Message_Update;
        UpdateMessageContentData updateMessageContentData = new UpdateMessageContentData();
        updateMessageContentData.setMessageUid(messageUid);
        updateMessageContentData.setPayload(payload);
        updateMessageContentData.setUpdateTimestamp(0);
        updateMessageContentData.setDistribute(1);
        return robotHttpUtils.httpJsonPost(path, updateMessageContentData, Void.class);
    }

    public IMResult<InputOutputUserInfo> getUserInfo(String userId) throws Exception {
        String path = APIPath.Robot_User_Info;
        InputGetUserInfo getUserInfo = new InputGetUserInfo(userId, null, null);
        return robotHttpUtils.httpJsonPost(path, getUserInfo, InputOutputUserInfo.class);
    }

    public IMResult<InputOutputUserInfo> getUserInfoByMobile(String phone) throws Exception {
        String path = APIPath.Robot_User_Info;
        InputGetUserInfo getUserInfo = new InputGetUserInfo(null, null, phone);
        return robotHttpUtils.httpJsonPost(path, getUserInfo, InputOutputUserInfo.class);
    }

    public IMResult<InputOutputUserInfo> getUserInfoByName(String userName) throws Exception {
        String path = APIPath.Robot_User_Info;
        InputGetUserInfo getUserInfo = new InputGetUserInfo(null, userName, null);
        return robotHttpUtils.httpJsonPost(path, getUserInfo, InputOutputUserInfo.class);
    }

    public IMResult<Void> setCallback(String url) throws Exception {
        String path = APIPath.Robot_Set_Callback;
        RobotCallbackPojo pojo = new RobotCallbackPojo();
        pojo.setUrl(url);
        return robotHttpUtils.httpJsonPost(path, pojo, Void.class);
    }
    
    public IMResult<RobotCallbackPojo> getCallback() throws Exception {
        String path = APIPath.Robot_Get_Callback;
        return robotHttpUtils.httpJsonPost(path, null, RobotCallbackPojo.class);
    }

    public IMResult<Void> deleteCallback() throws Exception {
        String path = APIPath.Robot_Delete_Callback;
        return robotHttpUtils.httpJsonPost(path, null, Void.class);
    }

    public IMResult<OutputRobot> getProfile() throws Exception {
        String path = APIPath.Robot_Get_Profile;
        return robotHttpUtils.httpJsonPost(path, null, OutputRobot.class);
    }

    /*
    type可选范围为MyInfoType，注意不能修改电话号码，如果要修改电话号码请使用adminapi进行修改
     */
    public IMResult<Void> updateProfile(int/*MyInfoType*/ type, String value) throws Exception {
        String path = APIPath.Robot_Update_Profile;
        IntStringPairPojo pojo = new IntStringPairPojo(type, value);
        return robotHttpUtils.httpJsonPost(path, pojo, Void.class);
    }

    public IMResult<OutputCreateGroupResult> createGroup(PojoGroupInfo group_info, List<PojoGroupMember> members, String member_extra, List<Integer> to_lines, MessagePayload notify_message) throws Exception {
        String path = APIPath.Robot_Create_Group;
        PojoGroup pojoGroup = new PojoGroup();
        pojoGroup.setGroup_info(group_info);
        pojoGroup.setMembers(members);
        InputCreateGroup createGroup = new InputCreateGroup();
        createGroup.setGroup(pojoGroup);
        createGroup.setMember_extra(member_extra);
        createGroup.setTo_lines(to_lines);
        createGroup.setNotify_message(notify_message);

        return robotHttpUtils.httpJsonPost(path, createGroup, OutputCreateGroupResult.class);
    }

    public IMResult<PojoGroupInfo> getGroupInfo(String groupId) throws Exception {
        String path = APIPath.Robot_Group_Get_Info;
        InputGetGroup input = new InputGetGroup();
        input.setGroupId(groupId);

        return robotHttpUtils.httpJsonPost(path, input, PojoGroupInfo.class);
    }

    public IMResult<Void> dismissGroup(String groupId, List<Integer> to_lines, MessagePayload notify_message) throws Exception {
        String path = APIPath.Robot_Group_Dismiss;
        InputDismissGroup dismissGroup = new InputDismissGroup();
        dismissGroup.setGroup_id(groupId);
        dismissGroup.setTo_lines(to_lines);
        dismissGroup.setNotify_message(notify_message);
        return robotHttpUtils.httpJsonPost(path, dismissGroup, Void.class);
    }

    public IMResult<Void> transferGroup(String groupId, String newOwner, List<Integer> to_lines, MessagePayload notify_message) throws Exception {
        String path = APIPath.Robot_Group_Transfer;
        InputTransferGroup transferGroup = new InputTransferGroup();
        transferGroup.setGroup_id(groupId);
        transferGroup.setNew_owner(newOwner);
        transferGroup.setTo_lines(to_lines);
        transferGroup.setNotify_message(notify_message);
        return robotHttpUtils.httpJsonPost(path, transferGroup, Void.class);
    }

    public IMResult<Void> modifyGroupInfo(String groupId, /*ModifyGroupInfoType*/int type, String value, List<Integer> to_lines, MessagePayload notify_message) throws Exception {
        String path = APIPath.Robot_Group_Modify_Info;
        InputModifyGroupInfo modifyGroupInfo = new InputModifyGroupInfo();
        modifyGroupInfo.setGroup_id(groupId);
        modifyGroupInfo.setTo_lines(to_lines);
        modifyGroupInfo.setType(type);
        modifyGroupInfo.setValue(value);
        modifyGroupInfo.setNotify_message(notify_message);
        return robotHttpUtils.httpJsonPost(path, modifyGroupInfo, Void.class);
    }


    public IMResult<OutputGroupMemberList> getGroupMembers(String groupId) throws Exception {
        String path = APIPath.Robot_Group_Member_List;
        InputGetGroup input = new InputGetGroup();
        input.setGroupId(groupId);
        return robotHttpUtils.httpJsonPost(path, input, OutputGroupMemberList.class);
    }

    public IMResult<PojoGroupMember> getGroupMember(String groupId, String memberId) throws Exception {
        String path = APIPath.Robot_Group_Member_Get;
        InputGetGroupMember input = new InputGetGroupMember();
        input.setGroupId(groupId);
        input.setMemberId(memberId);
        return robotHttpUtils.httpJsonPost(path, input, PojoGroupMember.class);
    }

    public IMResult<Void> addGroupMembers(String groupId, List<PojoGroupMember> groupMembers, String member_extra, List<Integer> to_lines, MessagePayload notify_message) throws Exception {
        String path = APIPath.Robot_Group_Member_Add;
        InputAddGroupMember addGroupMember = new InputAddGroupMember();
        addGroupMember.setGroup_id(groupId);
        addGroupMember.setMembers(groupMembers);
        addGroupMember.setMemberExtra(member_extra);
        addGroupMember.setTo_lines(to_lines);
        addGroupMember.setNotify_message(notify_message);
        return robotHttpUtils.httpJsonPost(path, addGroupMember, Void.class);
    }

    public IMResult<Void> setGroupManager(String groupId, List<String> groupMemberIds, boolean isManager, List<Integer> to_lines, MessagePayload notify_message) throws Exception {
        String path = APIPath.Robot_Group_Set_Manager;
        InputSetGroupManager addGroupMember = new InputSetGroupManager();
        addGroupMember.setGroup_id(groupId);
        addGroupMember.setMembers(groupMemberIds);
        addGroupMember.setIs_manager(isManager);
        addGroupMember.setTo_lines(to_lines);
        addGroupMember.setNotify_message(notify_message);
        return robotHttpUtils.httpJsonPost(path, addGroupMember, Void.class);
    }

    public IMResult<Void> muteGroupMember(String groupId, List<String> groupMemberIds, boolean isMute, List<Integer> to_lines, MessagePayload notify_message) throws Exception {
        String path = APIPath.Robot_Group_Mute_Member;
        InputMuteGroupMember addGroupMember = new InputMuteGroupMember();
        addGroupMember.setGroup_id(groupId);
        addGroupMember.setMembers(groupMemberIds);
        addGroupMember.setIs_manager(isMute);
        addGroupMember.setTo_lines(to_lines);
        addGroupMember.setNotify_message(notify_message);
        return robotHttpUtils.httpJsonPost(path, addGroupMember, Void.class);
    }

    public IMResult<Void> allowGroupMember(String groupId, List<String> groupMemberIds, boolean isAllow, List<Integer> to_lines, MessagePayload notify_message) throws Exception {
        String path = APIPath.Robot_Group_Allow_Member;
        InputMuteGroupMember addGroupMember = new InputMuteGroupMember();
        addGroupMember.setGroup_id(groupId);
        addGroupMember.setMembers(groupMemberIds);
        addGroupMember.setIs_manager(isAllow);
        addGroupMember.setTo_lines(to_lines);
        addGroupMember.setNotify_message(notify_message);
        return robotHttpUtils.httpJsonPost(path, addGroupMember, Void.class);
    }

    public IMResult<Void> kickoffGroupMembers(String groupId, List<String> groupMemberIds, List<Integer> to_lines, MessagePayload notify_message) throws Exception {
        String path = APIPath.Robot_Group_Member_Kickoff;
        InputKickoffGroupMember kickoffGroupMember = new InputKickoffGroupMember();
        kickoffGroupMember.setGroup_id(groupId);
        kickoffGroupMember.setMembers(groupMemberIds);
        kickoffGroupMember.setTo_lines(to_lines);
        kickoffGroupMember.setNotify_message(notify_message);
        return robotHttpUtils.httpJsonPost(path, kickoffGroupMember, Void.class);
    }

    public IMResult<Void> quitGroup(String groupId, List<Integer> to_lines, MessagePayload notify_message) throws Exception {
        String path = APIPath.Robot_Group_Member_Quit;
        InputQuitGroup quitGroup = new InputQuitGroup();
        quitGroup.setGroup_id(groupId);
        quitGroup.setTo_lines(to_lines);
        quitGroup.setNotify_message(notify_message);
        return robotHttpUtils.httpJsonPost(path, quitGroup, Void.class);
    }

    public IMResult<Void> setGroupMemberAlias(String groupId, String memberId, String alias, List<Integer> to_lines, MessagePayload notify_message) throws Exception {
        String path = APIPath.Robot_Group_Set_Member_Alias;
        InputSetGroupMemberAlias input = new InputSetGroupMemberAlias();
        input.setGroup_id(groupId);
        input.setMemberId(memberId);
        input.setAlias(alias);
        input.setTo_lines(to_lines);
        input.setNotify_message(notify_message);
        return robotHttpUtils.httpJsonPost(path, input, Void.class);
    }

    public IMResult<Void> setGroupMemberExtra(String groupId, String memberId, String extra, List<Integer> to_lines, MessagePayload notify_message) throws Exception {
        String path = APIPath.Robot_Group_Set_Member_Extra;
        InputSetGroupMemberExtra input = new InputSetGroupMemberExtra();
        input.setGroup_id(groupId);
        input.setMemberId(memberId);
        input.setExtra(extra);
        input.setTo_lines(to_lines);
        input.setNotify_message(notify_message);
        return robotHttpUtils.httpJsonPost(path, input, Void.class);
    }

    public IMResult<OutputApplicationUserInfo> applicationGetUserInfo(String authCode) throws Exception {
        String path = APIPath.Robot_Application_Get_UserInfo;
        InputApplicationGetUserInfo input = new InputApplicationGetUserInfo();
        input.setAuthCode(authCode);
        return robotHttpUtils.httpJsonPost(path, input, OutputApplicationUserInfo.class);
    }

    public OutputApplicationConfigData getApplicationSignature() {
        int nonce = (int)(Math.random() * 100000 + 3);
        long timestamp = System.currentTimeMillis()/1000;
        String str = nonce + "|" + robotHttpUtils.getRobotId() + "|" + timestamp + "|" + robotHttpUtils.getRobotSecret();
        String sign = DigestUtils.sha1Hex(str);
        OutputApplicationConfigData configData = new OutputApplicationConfigData();
        configData.setAppId(robotHttpUtils.getRobotId());
        configData.setAppType(ApplicationType_Robot);
        configData.setTimestamp(timestamp);
        configData.setNonceStr(nonce+"");
        configData.setSignature(sign);
        return configData;
    }

    public IMResult<String> sendConferenceRequest(String robotId, String clientId, String request, long sessionId, String roomId, String data, boolean advance) throws Exception {
        String path = APIPath.Robot_Conference_Request;
        InputConferenceRequest input = new InputConferenceRequest(robotId, clientId, request, sessionId, roomId, data, advance);
        return robotHttpUtils.httpJsonPost(path, input, String.class);
    }

    public IMResult<FeedPojo> postMomentsFeed(int/*MomentsContentType*/ type, String text, List<MediaEntry> medias, List<String> toUsers, List<String> excludeUsers, List<String> mentionedUsers, String extra) throws Exception {
        String path = APIPath.Robot_Moments_Post_Feed;
        FeedPojo feedPojo = new FeedPojo();
        feedPojo.type = type;
        feedPojo.text = text;
        feedPojo.medias = medias;
        feedPojo.to = toUsers;
        feedPojo.ex = excludeUsers;
        feedPojo.mu = mentionedUsers;
        feedPojo.extra = extra;
        IMResult<PostFeedResult> imResult = robotHttpUtils.httpJsonPost(path, feedPojo, PostFeedResult.class);
        IMResult<FeedPojo> feedResult = new IMResult<>();
        if(imResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            feedPojo.feedId = imResult.result.id;
            feedPojo.timestamp = imResult.result.timestamp;
            feedResult.setCode(ErrorCode.ERROR_CODE_SUCCESS.getCode());
            feedResult.setResult(feedPojo);
        } else {
            feedResult.setCode(imResult.getCode());
            feedResult.setMsg(imResult.getMsg());
        }
        return feedResult;
    }

    public IMResult<Void> updateMomentsFeed(long feedId, int/*MomentsContentType*/ type, String text, List<MediaEntry> medias, List<String> toUsers, List<String> excludeUsers, List<String> mentionedUsers, String extra) throws Exception {
        String path = APIPath.Robot_Moments_Update_Feed;
        FeedPojo feedPojo = new FeedPojo();
        feedPojo.feedId = feedId;
        feedPojo.type = type;
        feedPojo.text = text;
        feedPojo.medias = medias;
        feedPojo.to = toUsers;
        feedPojo.ex = excludeUsers;
        feedPojo.mu = mentionedUsers;
        feedPojo.extra = extra;
        return robotHttpUtils.httpJsonPost(path, feedPojo, Void.class);
    }

    public IMResult<FeedsPojo> getMomentsFeeds(long feedId, int count, String user) throws Exception {
        String path = APIPath.Robot_Moments_Pull_Feeds;
        PullFeedRequestPojo requestPojo = new PullFeedRequestPojo();
        requestPojo.feedId = feedId;
        requestPojo.count = count;
        requestPojo.user = user;
        return robotHttpUtils.httpJsonPost(path, requestPojo, FeedsPojo.class);
    }

    public IMResult<FeedPojo> getMomentsFeed(long feedId) throws Exception {
        String path = APIPath.Robot_Moments_Fetch_Feed;
        PullOneFeedRequestPojo requestPojo = new PullOneFeedRequestPojo();
        requestPojo.feedId = feedId;
        return robotHttpUtils.httpJsonPost(path, requestPojo, FeedPojo.class);
    }

    public IMResult<Void> deleteMomentsFeed(long feedId) throws Exception {
        String path = APIPath.Robot_Moments_Recall_Feed;
        IdPojo requestPojo = new IdPojo();
        requestPojo.id = feedId;
        return robotHttpUtils.httpJsonPost(path, requestPojo, Void.class);
    }

    public IMResult<CommentPojo> postMomentsComment(long feedId, long replyId, int/*MomentsCommentType*/ type, String text, String replyTo, String extra) throws Exception {
        String path = APIPath.Robot_Moments_Post_Comment;
        CommentPojo commentPojo = new CommentPojo();
        commentPojo.type = type;
        commentPojo.text = text;
        commentPojo.replyId = replyId;
        commentPojo.feedId = feedId;
        commentPojo.replyTo = replyTo;
        commentPojo.extra = extra;
        IMResult<PostFeedResult> imResult = robotHttpUtils.httpJsonPost(path, commentPojo, PostFeedResult.class);
        IMResult<CommentPojo> feedResult = new IMResult<>();
        if(imResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
            commentPojo.commentId = imResult.result.id;
            commentPojo.timestamp = imResult.result.timestamp;
            feedResult.setCode(ErrorCode.ERROR_CODE_SUCCESS.getCode());
            feedResult.setResult(commentPojo);
        } else {
            feedResult.setCode(imResult.getCode());
            feedResult.setMsg(imResult.getMsg());
        }
        return feedResult;
    }

    public IMResult<Void> deleteMomentsComment(long feedId, long commentId) throws Exception {
        String path = APIPath.Robot_Moments_Recall_Comment;
        IdPojo requestPojo = new IdPojo();
        requestPojo.id = commentId;
        requestPojo.id2 = feedId;
        return robotHttpUtils.httpJsonPost(path, requestPojo, Void.class);
    }

    public IMResult<MomentProfilePojo> getUserMomentsProfile(String userId) throws Exception {
        String path = APIPath.Robot_Moments_Fetch_Profiles;
        PullProfileRequestPojo requestPojo = new PullProfileRequestPojo();
        requestPojo.u = userId;
        return robotHttpUtils.httpJsonPost(path, requestPojo, MomentProfilePojo.class);
    }

    public IMResult<Void> updateMomentsBackgroundUrl(String url) throws Exception {
        String path = APIPath.Robot_Moments_Update_Profiles_Value;
        PushProfileValueRequestPojo requestPojo = new PushProfileValueRequestPojo();
        requestPojo.t = 0;
        requestPojo.v = url;
        return robotHttpUtils.httpJsonPost(path, requestPojo, Void.class);
    }

    public IMResult<Void> updateMomentsStrangerVisibleCount(int count) throws Exception {
        String path = APIPath.Robot_Moments_Update_Profiles_Value;
        PushProfileValueRequestPojo requestPojo = new PushProfileValueRequestPojo();
        requestPojo.t = 1;
        requestPojo.i = count;
        return robotHttpUtils.httpJsonPost(path, requestPojo, Void.class);
    }

    public IMResult<Void> updateMomentsVisibleScope(int/*MomentsVisibleScope*/ scope) throws Exception {
        String path = APIPath.Robot_Moments_Update_Profiles_Value;
        PushProfileValueRequestPojo requestPojo = new PushProfileValueRequestPojo();
        requestPojo.t = 2;
        requestPojo.i = scope;
        return robotHttpUtils.httpJsonPost(path, requestPojo, Void.class);
    }

    public IMResult<Void> updateMomentsBlackList(List<String> addList, List<String> removeList) throws Exception {
        String path = APIPath.Robot_Moments_Update_Profiles_List_Value;
        PushProfileListRequestPojo requestPojo = new PushProfileListRequestPojo();
        requestPojo.b = false;
        requestPojo.al = addList;
        requestPojo.rl = removeList;
        return robotHttpUtils.httpJsonPost(path, requestPojo, Void.class);
    }

    public IMResult<Void> updateMomentsBlockList(List<String> addList, List<String> removeList) throws Exception {
        String path = APIPath.Robot_Moments_Update_Profiles_List_Value;
        PushProfileListRequestPojo requestPojo = new PushProfileListRequestPojo();
        requestPojo.b = true;
        requestPojo.al = addList;
        requestPojo.rl = removeList;
        return robotHttpUtils.httpJsonPost(path, requestPojo, Void.class);
    }
}
