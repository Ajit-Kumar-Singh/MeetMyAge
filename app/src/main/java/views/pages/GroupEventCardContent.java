package views.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class GroupEventCardContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<GroupEventCardItem> ITEMS = new ArrayList<GroupEventCardItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, GroupEventCardItem> ITEM_MAP = new HashMap<String, GroupEventCardItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createGroupEventCardItem(i));
        }
    }

    private static void addItem(GroupEventCardItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static GroupEventCardItem createGroupEventCardItem(int position) {
        return new GroupEventCardItem(String.valueOf(position), "20 Km Marathon", "Burn the Fuel and You will love it", "25/09/2018", "8:00AM", "0 INR");
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class GroupEventCardItem {
        public final String id;
        public final String eventName;
        public final String eventDetails;
        public final String eventDate;
        public final String eventTime;
        public final String eventPrice;

        public GroupEventCardItem(String id, String eventName, String eventDetails, String eventDate, String eventTime, String eventPrice) {
            this.id = id;
            this.eventName = eventName;
            this.eventDetails = eventDetails;
            this.eventDate = eventDate;
            this.eventTime = eventTime;
            this.eventPrice = eventPrice;
        }
    }
}