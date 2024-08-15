package com.example.geofencing.utils;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;

public class TokenUtil {
    private static final String firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging";

    public static String getAccessToken() {

        try{
            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"geofencing-skripsi-ridho\",\n" +
                    "  \"private_key_id\": \"bf820f9aaa55bfde0e5acc59b15e923a5c7c2acc\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDHiVGE5W/NS5h2\\nMhK7M4GRynzopLIP77Z2fQIHOE1vye6AwoeQ/Gm0d8PbnxztiC53/w4ZBeu4Gfy/\\nQow3QaTpXYZQyjyxQVXu5A03Dl465RaMHOp6hnLuDNY2EtJRVUMGZRAXDxbOFKKU\\niv8Rd9G90j/DS3J5PS8v59ZlWeDKsuYF44yvYYo4jf1Nd2Fu04TCrhqJW4sxrCep\\n/kQ852UDUt0/1y69WHmgfsvRpEMiWwxd+uyipRi8Lb050PWxMc2wnx+kR+fGmEGB\\n0W297G61eCxn9coJk6n+uq1XyGOMrs6BVkV78/jtGtivnwowBt4BI2et08EtGGFd\\nh58bNmYbAgMBAAECggEAX0N/KPzFYPVbh+qmKLwRe0J6WjPvU052BaHvKvVFez6k\\nYNL/GEh6Sw/28U7I59rhnKan22iJPq8hjUq/WM49ZrEhJEc5rMLtvQ0iBgwVDvc6\\nyZAwnmi8TEzSpKRdxzj2tAHiKbApiiAt8s9dscBL4d2UVbAaqil88tkz8dV0TjE7\\n/l4uTq8Uua69mHbCiDbEfrJ/p0U0bYwzUxOPJna2FvM50l4gJBrMQWyHMUfb/0VA\\n6g3tth/TmANDrGBNPv5yR8t/uXYeXB4GLia/GvU5Mddr39yxultWNXvMDnGwfRlB\\nBOEapEgSfV7sA+UDCoQJc4GnOIEDhPW42KTXqrMdSQKBgQD911R9y5HOxjPFggHM\\ngL+DmyQ4a+YKo1jQRHxryjKmcKp2MxUKl477IOz+LhimrA4dtZ8kcyps7dVmieNz\\nCq8uQrZ5xgXz2hmfDF0M7PD412eTeUtIFs3xDCFAS1U3vi4pGF2z36fR9zYfzd1P\\nqNvEEpgUGUFIrCHXOpkjxmOjLQKBgQDJO8EutcDDrfPzlN4UYMBr9sJC15JBZQKz\\n5cYpXaOZW1BJWm/LpsgbxFCQmJkCu8DwuaGuUHBwIR8TrFHfC4I+gYGHUVX1cZWu\\nJi6NfcdqMQXsJ7aHru/FsCvb9YCGO8fVvSC6mzhFtlV9F0HbIMwUCVa4RbjqsGQS\\nH3s9HNIbZwKBgQCnEZhciyVWUIE2l1CTxpFGD1ARioKHseb8fHbfgt8/SJwI26oX\\npJTImG0jBjfXSxqnVyh9FwdvVkHEETEQQJH4ApSTJVA5pOyED+EWZsRbI3raR1TT\\nB59X7UHdTTezToqXJaBWiPeta+XFnPryORMbw5kwcUbUi+ZiAAFW2HYvqQKBgDmU\\n6K12Aqe7of/ETFvmZ4k9Pc8kokwmHETf7fkuOz8JuwqSX0UV/1K6tRb7I2V91jLL\\n23DmwZXEVIEroO3C/Ezof+j5pDAshVkkwdNo92PyjTBLGNbwftnBlHtHX7SJbKBD\\nkxbyjYKdLw8xVX3ff6YIGhF2mfhF6wyoh3owQgrnAoGARLCtBnW9X7/n2GNOq2v6\\nEKfv8rwAhaAedD+qbXsbcyhZBoZh9A0YptCJrRveqAT/IOQnOGIXFbAvS6zQDZrX\\nphutVO0DJ/j5IL9TntZtZJ775ddt3eiSu1anWQXXiTBRPUivZ3z54OVhTwnc5Ntq\\n1S6Rmzki4FDib5IA+EgWi0Y=\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-3cnnd@geofencing-skripsi-ridho.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"115906012430670700938\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-3cnnd%40geofencing-skripsi-ridho.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}";

            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Collections.singleton(firebaseMessagingScope));

            AccessToken token = googleCredentials.getAccessToken();
            if (token == null || token.getExpirationTime().before(new Date())) {
                googleCredentials.refresh();
                token = googleCredentials.getAccessToken();
            }

            return token.getTokenValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
