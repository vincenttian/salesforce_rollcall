global class Event{
    
    public String name {get;set;}
    public String start {get;set;}
    public String description {get;set;}
    public Integer checkedIn {get;set;}
    public Integer registered {get;set;}
    public Decimal maxCapacity {get;set;}
    public String titleColor {get;set;}
    public String textColor {get;set;}
    public String picData {get;set;}
    public String cal {get;set;}
    public List<String> checkedInTimes {get;set;}
    public ID cid {get;set;}
    
   
    /** Main constructor for an event. */ 
    public Event (Campaign c) {
        name = c.Name;
        start = c.StartDate.format();
        description = c.description;
        Map<Id, Campaign> potential_children =
            new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId,
            Id FROM Campaign WHERE ParentId=:c.Id OR Id=:c.Id]);
        registered = [SELECT Count() FROM CampaignMember
                      WHERE CampaignId in :potential_children.keySet()];
        checkedIn = [SELECT Count() FROM CampaignMember
                     WHERE CampaignId in :potential_children.keySet() AND
                     (Status='Responded')];
        maxCapacity = c.MaxCapacity__c;
        titleColor = 'title blue';
        textColor = 'blue_text';
        cid = c.id;
        CampaignMember[] checked_in = [SELECT LastModifiedDate, Lead.Firstname, Lead.Lastname, Lead.Email, Contact.Firstname, Contact.Lastname, Contact.Email, Contact.Company__c FROM CampaignMember WHERE Status='Responded' AND CampaignId in :potential_children.keySet()];
        List<String> data = new List<String>();
        for (CampaignMember check_in: checked_in) {
            data.add('\'' + check_in.LastModifiedDate.format('HH:mm:ss') + '\'');
        }
        checkedInTimes = data;
    }

    /** Special constructor for an event to add color. */
    public Event (Campaign c, Integer i) {
        name = c.Name;
        Integer month = c.StartDate.month();
        if (month == 1) {
            start = 'Jan ' + c.StartDate.day();
        } else if (month == 2) {
            start = 'Feb ' + c.StartDate.day();
        } else if (month == 3) {
            start = 'Mar ' + c.StartDate.day();
        } else if (month == 4) {
            start = 'Apr ' + c.StartDate.day();
        } else if (month == 5) {
            start = 'May ' + c.StartDate.day();
        } else if (month == 6) {
            start = 'Jun ' + c.StartDate.day();
        } else if (month == 7) {
            start = 'Jul ' + c.StartDate.day();
        } else if (month == 8) {
            start = 'Aug ' + c.StartDate.day();
        } else if (month == 9) {
            start = 'Sep ' + c.StartDate.day();
        } else if (month == 10) {
            start = 'Oct ' + c.StartDate.day();
        } else if (month == 11) {
            start = 'Nov ' + c.StartDate.day();
        } else {
            start = 'Dec ' + c.StartDate.day();
        }
        description = c.description;
        Map<Id, Campaign> potential_children =
            new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId,
            Id FROM Campaign WHERE ParentId=:c.Id OR Id=:c.Id]);
        registered = [SELECT Count() FROM CampaignMember
                      WHERE CampaignId in :potential_children.keySet()];
        checkedIn = [SELECT Count() FROM CampaignMember
                     WHERE CampaignId in :potential_children.keySet() AND
                     (Status='Responded')];
        maxCapacity = c.MaxCapacity__c;
        cid = c.id;
        if (Math.mod(i, 5) == 0) {
            titleColor = 'title blue';
            textColor = 'blue_text';
            picData = 'pic_data ' + textColor;
            cal = 'calendar_blue.svg';
        }
        if (Math.mod(i, 5) == 1) {
            titleColor = 'title magenta';
            textColor = 'magenta_text';
            picData = 'pic_data ' + textColor;
            cal = '{!URLFOR($Resource.RollCallAssets, \'calendar_magenta.svg\')}';
        }
        if (Math.mod(i, 5) == 2) {
            titleColor = 'title green';
            textColor = 'green_text';
            picData = 'pic_data ' + textColor;
            cal = '{!URLFOR($Resource.RollCallAssets, \'calendar_green.svg\')}';
        }
        if (Math.mod(i, 5) == 3) {
            titleColor = 'title red';
            textColor = 'red_text';
            picData = 'pic_data ' + textColor;
            cal = '{!URLFOR($Resource.RollCallAssets, \'calendar_red.svg\')}';
        }
        if (Math.mod(i, 5) == 4) {
            titleColor = 'title orange';
            textColor = 'orange_text';
            picData = 'pic_data ' + textColor;
            cal = '{!URLFOR($Resource.RollCallAssets, \'calendar_orange.svg\')}';
        }
    }

}