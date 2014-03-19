/**
*
*Roll Call application
*Controller for Index page
*@authors Howard Chen and Vincent Tian
*
*/

global class RollCall{


    // For apex: repeat
    public Event[] getEvents() {
        Campaign[] campaigns = [SELECT Name, Description, StartDate, MaxCapacity__c FROM Campaign WHERE IsActive = True AND ParentId = null ORDER BY StartDate ASC NULLS FIRST];
        ID[] parentIds = new Id[]{};
        for (campaign c: campaigns) {
            parentIds.add(c.ID);
        }
        Map<Id, Campaign> potential_children =
            new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId,
            Id FROM Campaign WHERE ParentId IN :parentIds OR ID IN :parentIds]);
        CampaignMember[] registered = [SELECT Status, CampaignID, Campaign.ParentID FROM CampaignMember
                      WHERE CampaignId in :potential_children.keySet() AND (Status =:Event.registeredStatus
                        OR Status =:Event.checkedInStatus)];
        Map<Id, Integer> regMap = new Map<Id, Integer>();
        Map<Id, Integer> checkMap = new Map<Id, Integer>();
        CampaignMember[] ps = new CampaignMember[]{};
        CampaignMember[] cs = new CampaignMember[]{};
        for (CampaignMember m : registered) {
            if (m.Campaign.ParentId != null) {
                cs.add(m);
            } else {
                ps.add(m);
            }
        }
        mapVals(regMap, checkMap, ps);
        mapVals2(regMap, checkMap, cs);
        Event[] events = new Event[]{};
        Integer i = 0;
        for (Campaign c : campaigns) {
            Event e = new Event(c, i);
            e.registered = regMap.get(c.ID);
            if (e.registered == null) {
                e.registered = 0;
            }
            e.checkedin = regMap.get(c.ID);
            if (e.checkedin == null) {
                e.checkedin = 0;
            }
            events.add(e);
            i++;
        }
        return events;
    }

    private void mapVals(Map<Id, Integer> rMap, Map<Id, Integer> cMap,
        CampaignMember[] members) {
        for (CampaignMember m: members) {
            if (rMap.containsKey(m.CampaignID)) {
                rMap.put(m.CampaignID, rMap.get(m.CampaignID) + 1);
            } else {
                rMap.put(m.CampaignID, 1);
            }
            if (m.status.equals('Responded')) {
                if (cMap.containsKey(m.CampaignID)) {
                    cMap.put(m.CampaignID, cMap.get(m.CampaignID) + 1);
                } else {
                    cMap.put(m.CampaignID, 1);
                }
            }
        }
    }

    private void mapVals2(Map<Id, Integer> rMap, Map<Id, Integer> cMap,
        CampaignMember[] members) {
        for (CampaignMember m: members) {
            if (rMap.containsKey(m.Campaign.ParentId)) {
                rMap.put(m.Campaign.ParentId, rMap.get(m.Campaign.ParentId) + 1);
            } else {
                rMap.put(m.Campaign.ParentId, 1);
            }
            if (m.status.equals('Responded')) {
                if (cMap.containsKey(m.Campaign.ParentId)) {
                    cMap.put(m.Campaign.ParentId, cMap.get(m.Campaign.ParentId) + 1);
                } else {
                    cMap.put(m.Campaign.ParentId, 1);
                }
            }
        }
    }

}
