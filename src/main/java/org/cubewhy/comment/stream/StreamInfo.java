package org.cubewhy.comment.stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import okhttp3.Response;
import org.cubewhy.comment.utils.RequestUtils;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class StreamInfo {
    public String apiCommentsHistory = "https://api.live.bilibili.com/xlive/web-room/v1/dM/gethistory?roomid=";
    public String apiRoomInfo = "https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=";
    public final String liveID;

    public StreamInfo(String liveID) {
        this.liveID = liveID;
        // join live id to api
        this.apiCommentsHistory += this.liveID;
        this.apiRoomInfo += this.liveID;
    }


    @Nullable
    @SneakyThrows
    public List<Comment> getComments() {
        if (liveID == null) {
            return null;
        }
        List<Comment> list = new ArrayList<>();
        JsonArray comments;
        try (Response response = RequestUtils.get(apiCommentsHistory).execute()) {
            JsonObject json;
            if (response.body() != null) {
                json = new JsonParser().parse(response.body().string()).getAsJsonObject();
                if (json.get("code").getAsInt() != 0) {
                    // Room not exist
                    return null;
                }
                comments = json.getAsJsonObject("data").getAsJsonArray("room");
            } else {
                // unreachable
                return null;
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        // parse json
        for (JsonElement comment : comments) {
            String nickname = comment.getAsJsonObject().get("nickname").getAsString(); // for example, cubewhy, MENGKE_15
            String text = comment.getAsJsonObject().get("text").getAsString();
            long time = sdf.parse(comment.getAsJsonObject().get("timeline").getAsString()).getTime();
            list.add(new Comment(nickname, text, time));
        }
        return Collections.unmodifiableList(list);
    }
}
