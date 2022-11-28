import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.responses.GetMembersResponse;
import models.VkGroupMainInfoModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VkGetListOfUsers {

    public static String accessToken;

    public static List<String> getGroupSubs(String groupId) throws ClientException, ApiException, InterruptedException, IOException {

        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        ObjectMapper mapper = new ObjectMapper();

        UserActor actor = new UserActor(1, accessToken);

        GetMembersResponse getResponse = new GetMembersResponse();
        try {
            getResponse = vk.groups().getMembers(actor)
                    .groupId(groupId)
                    .execute();
        } catch (com.vk.api.sdk.exceptions.ApiAccessException Access) {
            System.out.println(" ");
        }

        int offset = 0;
        List<Integer> totalSubs = new ArrayList<>();

        if (getResponse.getCount() == null) {
            totalSubs.add(0);
        } else {
            while (offset < getResponse.getCount()) {

                GetMembersResponse getResponseCycle = vk.groups().getMembers(actor)
                        .groupId(groupId)
                        .offset(offset)
                        .execute();

                totalSubs.addAll(getResponseCycle.getItems());

                Thread.sleep(500);

                offset += 1000;
            }
        }

        URL groupMainDataUrl = new URL("https://api.vk.com/method/groups.getById?group_id=" + groupId +"&access_token=" + accessToken + "&v=5.131");

        VkGroupMainInfoModel vkGroupMainInfoModel = mapper.readValue(groupMainDataUrl, VkGroupMainInfoModel.class);
        String groupName = vkGroupMainInfoModel.getResponse().get(0).getName();

        List<String> totalSubsString = new ArrayList<>(totalSubs.size());
        totalSubsString.add(groupName);
        for (Integer n : totalSubs) {
            totalSubsString.add("https://vk.com/id" + String.valueOf(n));
        }

        return totalSubsString;

    }
}
