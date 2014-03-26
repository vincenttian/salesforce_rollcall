@isTest
public class RollCallTestUtility{
    
    public static Campaign createEventCampaign() {
        Campaign c = new Campaign(name='Parent', startdate=Date.today(), isActive=true);
        insert c;
        Campaign c2 = new Campaign(name='Child', startdate=Date.today(), isActive=true, ParentID=c.ID);
        insert c2;
        Lead le = new Lead(firstName='Lead', lastName='Lee', company='SF', email='test@test.com');
        insert le;
        CampaignMember m1 = new CampaignMember(LeadId=le.ID, campaignId=c.ID);
        insert m1;
        Lead le2 = new Lead(firstName='Lead2', lastName='Lee', company='SF', email='a@b.com');
        insert le2;
        CampaignMember m2 = new CampaignMember(LeadId=le2.ID, campaignId=c.ID);
        insert m2;
        Contact cn = new Contact(firstName='Contact', lastName='C');
        insert cn;
        CampaignMember m3 = new CampaignMember(ContactId=cn.ID, campaignId=c.ID);
        insert m3;
        Contact cn2 = new Contact(firstName='Contact2', lastName='C', email='test@test.com');
        insert cn2;
        CampaignMember m4 = new CampaignMember(ContactId=cn2.ID, campaignId=c2.ID);
        insert m4;
        return c;
    }

}
