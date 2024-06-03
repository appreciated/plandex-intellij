package com.github.appreciated.plandex_plugin.api;

import okhttp3.*;

import java.io.IOException;

public class ApiClient {
    private static final String BASE_URL = "https://api.example.com";
    private final OkHttpClient client;
    private String authToken;

    public ApiClient() {
        this.client = new OkHttpClient();
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPlans() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/plans")
                .get()
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String archivePlan(String planId) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/archive")
                .patch(RequestBody.create("", null))
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String unarchivePlan(String planId) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/unarchive")
                .patch(RequestBody.create("", null))
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String renamePlan(String planId, String newName) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("newName", newName)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/rename")
                .patch(body)
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String getPlanContext(String planId, String branch) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/" + branch + "/context")
                .get()
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String addPlanContext(String planId, String branch, String context) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("context", context)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/" + branch + "/context")
                .post(body)
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String updatePlanContext(String planId, String branch, String context) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("context", context)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/" + branch + "/context")
                .put(body)
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String deletePlanContext(String planId, String branch) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/" + branch + "/context")
                .delete()
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String getPlanDiffs(String planId, String branch) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/" + branch + "/diffs")
                .get()
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String getPlanConvo(String planId, String branch) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/" + branch + "/convo")
                .get()
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String getPlanLogs(String planId, String branch) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/" + branch + "/logs")
                .get()
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String getPlanBranches(String planId) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/branches")
                .get()
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String createPlanBranch(String planId, String branchName) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("branchName", branchName)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/branches")
                .post(body)
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String deletePlanBranch(String planId, String branch) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/branches/" + branch)
                .delete()
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String getPlanSettings(String planId, String branch) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/" + branch + "/settings")
                .get()
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String updatePlanSettings(String planId, String branch, String settings) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("settings", settings)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/" + branch + "/settings")
                .put(body)
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String getPlanStatus(String planId, String branch) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/plans/" + planId + "/" + branch + "/status")
                .get()
                .header("Authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }
}







