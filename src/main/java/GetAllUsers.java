import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ApiAccessException;
import com.vk.api.sdk.exceptions.ClientException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class GetAllUsers {

    public static void main(String[] args) throws ClientException, InterruptedException, ApiException, GeneralSecurityException, IOException, ApiAccessException {

        VkGetListOfUsers.accessToken = "TOKEN";
        String mySpreadSheetId = "1Gq-W2ZbaCv1BrP8OyXGUE2INALS00MipFUz-t8y85To";
        List<String> groupsIdsFromSheet = new ArrayList<>();
        int B = 5, F = 5;

        while (true) {
            String nString, mString;
            nString = "B" + String.valueOf(B);
            mString = "F" + String.valueOf(F);

            B += 4;
            F += 4;

            Object retrievedInfo = GoogleSheetData.getSpecificDataFromSheet(nString, mString, mySpreadSheetId);

            if (retrievedInfo.equals(0)) {
                break;
            }

            groupsIdsFromSheet.addAll((Collection<? extends String>) retrievedInfo);
        }

        List<String> screenNames = new ArrayList<>();

        for (String url : groupsIdsFromSheet) {
            String[] parts = url.split("/");
            String screenName = parts[parts.length - 1];
            screenNames.add(screenName);
        }

        Random r = new Random();
        int number = 1000000 + (int) (r.nextFloat() * 8999000);

        String mySheetName = generateName();
        Integer mySheetId = number;

        List<List<Object>> totalIds = new ArrayList<>();

        try {
            GoogleSheetData.addSheet(mySheetName, mySheetId, mySpreadSheetId);

            List<String> info = VkGetListOfUsers.getGroupSubs(screenNames.get(0));
            List<Object> infoObject = new ArrayList<>(info);
            totalIds.add(infoObject);

            GoogleSheetData.addDataToSheet(totalIds, mySpreadSheetId, mySheetName);
        } catch (com.google.api.client.googleapis.json.GoogleJsonResponseException Bad) {
            String mySpreadSheetName = "Таблица подписчиков " + generateName();
            mySpreadSheetId = GoogleSheetData.createSpreadsheet(mySpreadSheetName);
            mySheetName = "Sheet1";

            List<String> info = VkGetListOfUsers.getGroupSubs(screenNames.get(0));
            List<Object> infoObject = new ArrayList<>(info);
            totalIds.add(infoObject);

            GoogleSheetData.addDataToSheet(totalIds, mySpreadSheetId, mySheetName);
        }

        int i = 1;

        while (i < screenNames.size()) {
            try {
                List<List<Object>> tempList = GoogleSheetData.getAllDataFromSheet(mySheetName, mySpreadSheetId);
                List<String> tempStringList = VkGetListOfUsers.getGroupSubs(screenNames.get(i));
                List<Object> tempObjectList = new ArrayList<>(tempStringList);
                tempList.add(tempObjectList);
                GoogleSheetData.addDataToSheet(tempList, mySpreadSheetId, mySheetName);
                i++;
            } catch (com.google.api.client.googleapis.json.GoogleJsonResponseException Bad) {
                String mySpreadSheetName = "Таблица подписчиков " + generateName();
                mySpreadSheetId = GoogleSheetData.createSpreadsheet(mySpreadSheetName);
            }
        }

        GoogleSheetData.autoResizeCells(mySpreadSheetId, mySheetId);
    }

    public static String generateName() {

        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}