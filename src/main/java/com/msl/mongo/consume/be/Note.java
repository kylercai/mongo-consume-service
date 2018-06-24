package com.msl.mongo.consume.be;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "note")
public class Note {

    @Id
    private String id;
    //客户编号
    @Field("client_id")
    private String clientId;
    //用户类型（1：准客户，2：个险客户，3：其他）
    @Field("client_type")
    private Integer clientType;
    //营销员编号或是内勤人员域账户
    @Field("user_id")
    private String userId;
    //内容类型(1：销售线索，2：增员线索，3：其它线索，4：系统线索)
    @Field("note_type")
    private Integer noteType;
    //笔记类型(1:文字，2:语音)
    @Field("content_type")
    private Integer contentType;
    //来源类型（1：用户，2：系统）
    @Field("source_type")
    private Integer sourceType;
    //日志内容
    @Field("content")
    private String content;
    //语音URL
    @Field("voice_url")
    private String voiceUrl;
    //语音长度
    @Field("voice_length")
    private Integer voiceLength;
    //关联的活动ID
    @Field("campaign_id")
    private String campaignId;
    //关联的日程任务id
    @Field("calendar_task_id")
    private String calendarTaskId;
    //删除标记(0:未删除,1:已删除)
    @Field("status")
    private Integer status = 0;
    //创建时间
    @Field("created_time")
    private Date createdTime = new Date();
    //记录最后修改时间
    @Field("updated_time")
    private Date updatedTime = new Date();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getNoteType() {
        return noteType;
    }

    public void setNoteType(Integer noteType) {
        this.noteType = noteType;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public Integer getVoiceLength() {
        return voiceLength;
    }

    public void setVoiceLength(Integer voiceLength) {
        this.voiceLength = voiceLength;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCalendarTaskId() {
        return calendarTaskId;
    }

    public void setCalendarTaskId(String calendarTaskId) {
        this.calendarTaskId = calendarTaskId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
