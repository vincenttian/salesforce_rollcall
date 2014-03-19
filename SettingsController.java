global class SettingsController{
    
    @RemoteAction
    global static void update_settings(String checkin_status, String register_status) {
    	Map<String, RollCall_Settings__c> settings = RollCall_Settings__c.getAll();
		if (settings.isempty()) { 
			RollCall_Settings__c setting = new RollCall_Settings__c();
			setting.Check_in_Status__c = checkin_status;
			setting.Registered_Status__c = register_status;
			setting.name = 'Default Settings';
			insert setting;
		} else {
			RollCall_Settings__c first_setting = settings.values()[0];	
			first_setting.Check_in_Status__c = checkin_status;
			first_setting.Registered_Status__c = register_status;
			update first_setting;
		}		
	}

    @RemoteAction
    global static String[] check_settings() {
    	Map<String, RollCall_Settings__c> setting = RollCall_Settings__c.getAll();
		String[] info = new String[2];
		if (setting.isempty()) {
			info[0] = 'Sent';
			info[1] = 'Received';
			return info;
		} else {
			RollCall_Settings__c first_setting = setting.values()[0];	
			info[0] = first_setting.Check_in_Status__c;
			info[1] = first_setting.Registered_Status__c;
			return info;
		}
    }
}