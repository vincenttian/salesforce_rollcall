@isTest
private class SettingControllerTest {


    static testMethod void test_get_settings() {
        SettingsController s = new SettingsController();
        List<String> types= s.getCampaignTypes();
        System.assertNotEquals(types, null);
        boolean size = types.size() >= 1;
        System.assertEquals(size, true);
    }

    static testMethod void test_update_settings() {
        String[] info = SettingsController.check_settings();
        System.assertEquals(info[0], 'Responded');
        System.assertEquals(info[1], 'Sent');
        System.assertEquals(info[2], 'All');
        SettingsController.update_settings('1', '2', 'None');
        info = SettingsController.check_settings();
        System.assertEquals(info[0], '1');
        System.assertEquals(info[1], '2');
        System.assertEquals(info[2], 'None');

    }

}