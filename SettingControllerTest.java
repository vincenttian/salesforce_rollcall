@isTest
private class SettingControllerTest {


    static testMethod void test_get_settings() {
        SettingsController s = new SettingsController();
        List<String> types= s.getCampaignTypes();
        System.assertNotEquals(types, null);
    }

    static testMethod void test_update_settings() {
        String[] info = SettingsController.check_settings();
        System.assertEquals(info[0], 'Responded');
        SettingsController.update_settings('1', '2', 'All');
        info = SettingsController.check_settings();
        System.assertEquals(info[0], '1');

    }

}