package com.msl.mongo.consume.be;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "campaign.target")
public class CampaignTarget {

    @Field("campaign_id")
    private String campaignId;
    @Field("client_name")
    private String clientName;
    @Field("client_num")
    private String clientNum;
    @Field("mobile_phone_num")
    private String mobilePhoneNum;
    @Field("agent_code")
    private String agentCode;

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientNum() {
        return clientNum;
    }

    public void setClientNum(String clientNum) {
        this.clientNum = clientNum;
    }

    public String getMobilePhoneNum() {
        return mobilePhoneNum;
    }

    public void setMobilePhoneNum(String mobilePhoneNum) {
        this.mobilePhoneNum = mobilePhoneNum;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }
}
