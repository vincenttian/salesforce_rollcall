@isTest
public class RollCallTestUtility{
    
    private static SObject[] campaignGeneration() {
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
        Contact cn = new Contact(firstName='Contact', lastName='C', email='b@a.com');
        insert cn;
        CampaignMember m3 = new CampaignMember(ContactId=cn.ID, campaignId=c.ID);
        insert m3;
        Contact cn2 = new Contact(firstName='Contact2', lastName='C', email='test@test.com');
        insert cn2;
        CampaignMember m4 = new CampaignMember(ContactId=cn2.ID, campaignId=c2.ID);
        insert m4;
        return new SObject[]{c, le2, cn, le, cn2, m1, m4};
    }

    private static SObject[] campaignGeneration2() {
        Campaign c = new Campaign(name='Parent', startdate=Date.today(), isActive=true, maxCapacity__c = 5);
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
        Contact cn = new Contact(firstName='Contact', lastName='C', email='b@a.com');
        insert cn;
        CampaignMember m3 = new CampaignMember(ContactId=cn.ID, campaignId=c.ID);
        insert m3;
        Contact cn2 = new Contact(firstName='Contact2', lastName='C', email='test@test.com');
        insert cn2;
        CampaignMember m4 = new CampaignMember(ContactId=cn2.ID, campaignId=c2.ID);
        insert m4;
        return new SObject[]{c, le2, cn, le, cn2, m1, m4};
    }

    private static SObject[] campaignGeneration3() {
        Campaign c = new Campaign(name='Parent', startdate=Date.today(), isActive=true, maxCapacity__c = 0);
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
        Contact cn = new Contact(firstName='Contact', lastName='C', email='b@a.com');
        insert cn;
        CampaignMember m3 = new CampaignMember(ContactId=cn.ID, campaignId=c.ID);
        insert m3;
        Contact cn2 = new Contact(firstName='Contact2', lastName='C', email='test@test.com');
        insert cn2;
        CampaignMember m4 = new CampaignMember(ContactId=cn2.ID, campaignId=c2.ID);
        insert m4;
        return new SObject[]{c, le2, cn, le, cn2, m1, m4};
    }

    public static Campaign createEventCampaign() {
        return (Campaign) campaignGeneration()[0];
    }

    public static SObject[] createEventCampaign2() {
        SObject[] getList = campaignGeneration();
        SObject[] retList = new SObject[]{};
        retList.add(getList.get(0));
        retList.add(getList.get(1));
        return retList;
    }

    public static SObject[] createEventCampaign3() {
        SObject[] getList = campaignGeneration();
        SObject[] retList = new SObject[]{};
        retList.add(getList.get(0));
        retList.add(getList.get(2));
        return retList;
    }

    public static SObject[] createEventCampaign4() {
        SObject[] getList = campaignGeneration();
        SObject[] retList = new SObject[]{};
        retList.add(getList.get(0));
        retList.add(getList.get(3));
        retList.add(getList.get(4));
        retList.add(getList.get(5));
        retList.add(getList.get(6));
        return retList;
    }

    public static SObject[] createEventCampaign5() {
        SObject[] getList = campaignGeneration2();
        SObject[] retList = new SObject[]{};
        retList.add(getList.get(0));
        retList.add(getList.get(2));
        return retList;
    }

    public static SObject[] createEventCampaign6() {
        SObject[] getList = campaignGeneration3();
        SObject[] retList = new SObject[]{};
        retList.add(getList.get(0));
        retList.add(getList.get(2));
        return retList;
    }
}
