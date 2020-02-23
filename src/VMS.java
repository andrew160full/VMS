import java.util.Vector;

public class VMS {
    private static final VMS instance = new VMS ();
    private Vector <Campaign> campaigns;
    private Vector <User> users;

    private VMS () {
        campaigns = new Vector<>();
        users = new Vector<>();
    }

    public static VMS getInstance () {
        return instance;
    }

    public Vector <Campaign> getCampaigns () {
        return campaigns;
    }
    public Campaign getCampaign (Integer id) {
        for (Campaign campaign : campaigns)
            if (campaign.getCampaignId() == id)
                return campaign;
        return null;
    }
    public void addCampaign (Campaign campaign) {
        if (!campaigns.contains (campaign))
            campaigns.add (campaign);
    }
    public void updateCampaign (Integer id, Campaign campaign) {
        for (Campaign c : campaigns) {
            if (c.getCampaignId () == id) {
                if (c.getStatus () == Campaign.CampaignStatusType.NEW) {
                    c.setDescription (campaign.getDescription ());
                    c.setStart (campaign.getStart ());
                    c.setFinish (campaign.getFinish ());
                    c.setName (campaign.getName());
                }
                else if (c.getStatus () == Campaign.CampaignStatusType.
                            STARTED) {
                    if (campaign.getTotal() >= c.getAvailable())
                        c.setTotal(campaign.getTotal());
                    else {
                        c.setAvailable(campaign.getTotal());
                        c.setTotal(campaign.getTotal());
                    }
                    c.setFinish(campaign.getFinish());
                }
            }
        }
    }
    public void cancelCampaign (Integer id) {
        for (Campaign campaign : campaigns) {
            if (campaign.getCampaignId () == id) {
                if (campaign.getStatus () == Campaign.CampaignStatusType.NEW
                || campaign.getStatus () == Campaign.CampaignStatusType.
                        STARTED) {
                    campaigns.remove (campaign);
                }
            }
        }
    }
    public Vector <User> getUsers () {
        return users;
    }
    public void addUser (User user) {
        if (!users.contains (user))
            users.add (user);
    }
}
