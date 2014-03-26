global class Event{
    
    public String name {get;set;}
    public String start {get;set;}
    public String description {get;set;}
    public Integer checkedIn {get;set;}
    public Integer registered {get;set;}
    public String maxCapacity {get;set;}
    public String titleColor {get;set;}
    public String textColor {get;set;}
    public String picData {get;set;}
    public String cal {get;set;}
    public List<String> checkedInTimes {get;set;}
    global static String registeredStatus = RollCall_Settings__c.getAll().isEmpty()?'Sent':RollCall_Settings__c.getAll().values()[0].Registered_Status__c;
    global static String checkedInStatus = RollCall_Settings__c.getAll().isEmpty()?'Responded':RollCall_Settings__c.getAll().values()[0].Check_in_Status__c;
    public ID cid {get;set;}
    
   
    /** Main constructor for an event. */ 
    public Event (Campaign c) {
        name = c.Name;
        if (c.StartDate != null) {
            if (c.StartDate.isSameDay(Date.today())) {
                start = 'Today'
            } else {
                start = c.StartDate.format();
            }
        } else {
            start = '';
        }
        description = c.description;
        Map<Id, Campaign> potential_children =
            new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId,
            Id FROM Campaign WHERE ParentId=:c.Id OR Id=:c.Id]);
        registered = [SELECT Count() FROM CampaignMember
                      WHERE CampaignId in :potential_children.keySet() and (Status=:RegisteredStatus 
                      or Status=:checkedInStatus)];
        checkedIn = [SELECT Count() FROM CampaignMember
                     WHERE CampaignId in :potential_children.keySet() AND
                     (Status=:checkedInStatus)];
        if (c.MaxCapacity__c != null) {
            maxCapacity = c.MaxCapacity__c + '';
        } else {
            maxCapacity = 'Unlimited';
        }
        titleColor = 'title blue';
        textColor = 'blue_text';
        cid = c.id;
        CampaignMember[] checked_in = [SELECT LastModifiedDate, Lead.Firstname, Lead.Lastname, Lead.Email, Contact.Firstname, Contact.Lastname, Contact.Email, Contact.Company__c FROM CampaignMember WHERE Status=:Event.checkedInStatus AND CampaignId in :potential_children.keySet()];
        List<String> data = new List<String>();
        for (CampaignMember check_in: checked_in) {
            data.add('\'' + check_in.LastModifiedDate.format('HH:mm:ss') + '\'');
        }
        checkedInTimes = data;
    }

    /** Special constructor for an event to add color. */
    public Event (Campaign c, Integer i) {
        name = c.Name;
        if (c.StartDate != null) {
            Integer month = c.StartDate.month();
            if (c.StartDate.isSameDay(Date.today())) {
                start = 'Today';
            } else if (month == 1) {
                start = 'JAN ' + c.StartDate.day();
            } else if (month == 2) {
                start = 'FEB ' + c.StartDate.day();
            } else if (month == 3) {
                start = 'MAR ' + c.StartDate.day();
            } else if (month == 4) {
                start = 'APR ' + c.StartDate.day();
            } else if (month == 5) {
                start = 'MAY ' + c.StartDate.day();
            } else if (month == 6) {
                start = 'JUN ' + c.StartDate.day();
            } else if (month == 7) {
                start = 'JUL ' + c.StartDate.day();
            } else if (month == 8) {
                start = 'AUG ' + c.StartDate.day();
            } else if (month == 9) {
                start = 'Sep ' + c.StartDate.day();
            } else if (month == 10) {
                start = 'Oct ' + c.StartDate.day();
            } else if (month == 11) {
                start = 'Nov ' + c.StartDate.day();
            } else {
                start = 'Dec ' + c.StartDate.day();
            }
        } else {
            start = '';
        }
        description = c.description;
        if (c.MaxCapacity__c != null) {
            maxCapacity = c.MaxCapacity__c + '';
        } else {
            maxCapacity = 'Unlimited';
        }
        cid = c.id;
        if (Math.mod(i, 5) == 0) {
            titleColor = 'title blue';
            textColor = 'blue_text';
            picData = 'pic_data ' + textColor;
            cal = 'pic cal_blue';
        }
        if (Math.mod(i, 5) == 1) {
            titleColor = 'title magenta';
            textColor = 'magenta_text';
            picData = 'pic_data ' + textColor;
            cal = 'pic cal_magenta';
        }
        if (Math.mod(i, 5) == 2) {
            titleColor = 'title green';
            textColor = 'green_text';
            picData = 'pic_data ' + textColor;
            cal = 'pic cal_green';
        }
        if (Math.mod(i, 5) == 3) {
            titleColor = 'title red';
            textColor = 'red_text';
            picData = 'pic_data ' + textColor;
            cal = 'pic cal_red';

        }
        if (Math.mod(i, 5) == 4) {
            titleColor = 'title orange';
            textColor = 'orange_text';
            picData = 'pic_data ' + textColor;
            cal = 'pic cal_orange';
        }
    }

}