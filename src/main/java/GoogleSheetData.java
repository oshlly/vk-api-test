import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.sheets.v4.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GoogleSheetData {

    private static Sheets sheets;
    private static final String name = "spreadsheets-test";

    private static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream inputStream = GoogleSheetData.class.getResourceAsStream("/credentials.json");
        assert inputStream != null;
        GoogleClientSecrets googleClientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(inputStream));

        List<String> scopes = List.of(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                googleClientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver())
                .authorize("user");
    }

    private static HttpRequestInitializer createHttpRequestInitializer(final HttpRequestInitializer requestInitializer) {
        return new HttpRequestInitializer() {
            @Override
            public void initialize(final HttpRequest httpRequest) throws IOException {
                requestInitializer.initialize(httpRequest);
                httpRequest.setConnectTimeout(10 * 60000);
                httpRequest.setReadTimeout(10 * 60000);
            }
        };
    }

    public static Sheets getSheetsService() throws GeneralSecurityException, IOException {
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(name)
                .setHttpRequestInitializer(createHttpRequestInitializer(credential))
                .build();
    }

    public static void addDataToSheet(List<List<Object>> vkList, String spreadsheetId, String sheetName) throws GeneralSecurityException, IOException {
        sheets = getSheetsService();

        ValueRange appendBody = new ValueRange()
                .setMajorDimension("COLUMNS")
                .setValues(vkList);

        UpdateValuesResponse appendResult = sheets.spreadsheets().values()
                .update(spreadsheetId, sheetName, appendBody)
                .setValueInputOption("USER_ENTERED")
                .execute();
    }

    public static Object getSpecificDataFromSheet(String n, String m, String spreadsheetId) throws IOException, GeneralSecurityException {
        sheets = getSheetsService();

        ValueRange response = sheets.spreadsheets().values()
                .get(spreadsheetId, n + ":" + m)
                .execute();

        List<List<Object>> groupsUrls = response.getValues();

        if (groupsUrls == null || groupsUrls.isEmpty()) {
            return 0;
        } else {
            List<Object> flatGroupsUrls = groupsUrls.stream()
                    .flatMap(List::stream).toList();

            return flatGroupsUrls.stream()
                    .map(object -> Objects.toString(object, null))
                    .toList();
        }
    }

    public static List<List<Object>> getAllDataFromSheet(String sheetName, String spreadsheetId) throws GeneralSecurityException, IOException {
        sheets = getSheetsService();

        ValueRange response = sheets.spreadsheets().values()
                .get(spreadsheetId, sheetName)
                .setMajorDimension("COLUMNS")
                .execute();

        return response.getValues();
    }

    public static void addSheet (String sheetName, Integer sheetId, String spreadsheetId) throws GeneralSecurityException, IOException {
        sheets = getSheetsService();

        List<Request> requests = new ArrayList<>();
        requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties()
                .setSheetId(sheetId)
                .setTitle(sheetName))));

        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();
        batchUpdateSpreadsheetRequest.setRequests(requests);

        BatchUpdateSpreadsheetResponse response = sheets.spreadsheets().batchUpdate(spreadsheetId, batchUpdateSpreadsheetRequest)
                .execute();

    }

    public static void autoResizeCells (String spreadsheetId, Integer sheetId) throws GeneralSecurityException, IOException {
        sheets = getSheetsService();

        Request resizeRequest = new Request();
        resizeRequest.setAutoResizeDimensions(new AutoResizeDimensionsRequest()
                .setDimensions(new DimensionRange()
                        .setSheetId(sheetId)
                        .setDimension("COLUMNS")));

        List<Request> requests = new ArrayList<>();
        requests.add(resizeRequest);

        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();
        batchUpdateSpreadsheetRequest.setRequests(requests);

        BatchUpdateSpreadsheetResponse autoUpdateCellSize = sheets.spreadsheets()
                .batchUpdate(spreadsheetId, batchUpdateSpreadsheetRequest)
                .execute();
    }

    public static String createSpreadsheet(String newSpreadsheetName) throws GeneralSecurityException, IOException {
        sheets = getSheetsService();

        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties()
                        .setTitle(newSpreadsheetName));
        spreadsheet = sheets.spreadsheets().create(spreadsheet)
                .setFields("spreadsheetId")
                .execute();

        return spreadsheet.getSpreadsheetId();
    }
}
