package api.payloads;

import org.json.JSONObject;

public class GoogleMapsPayload {

    public static String addPlacePayload() {
        JSONObject obj = new JSONObject();

        obj.put("accuracy", 50);
        obj.put("name", "Frontline house");
        obj.put("phone_number", "(+91) 983 893 3937");
        obj.put("address", "29, side layout, cohen 09");
        obj.put("website", "http://google.com");
        obj.put("language", "French-IN");

        JSONObject location = new JSONObject();
        location.put("lat", -38.383494);
        location.put("lng", 33.427362);

        obj.put("location", location);

        return obj.toString();
    }

    public static String deletePayload(String placeId) {
        return "{ \"place_id\":\"" + placeId + "\" }";
    }

    public static String updatePayload(String placeId) {
        return "{ \"place_id\":\"" + placeId + "\", \"address\":\"70 Summer walk, USA\", \"key\":\"qaclick123\" }";
    }
}
