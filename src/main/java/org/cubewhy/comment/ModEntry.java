package org.cubewhy.comment;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.event.EventBus;
import org.cubewhy.comment.events.CommentUpdateEvent;
import org.cubewhy.comment.files.ConfigFile;
import org.cubewhy.comment.stream.Comment;
import org.cubewhy.comment.stream.HeartThread;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ModEntry implements ModInitializer {

    public static HeartThread thread;
    public static final ConfigFile config = new ConfigFile(new File(System.getProperty("user.home"), ".cubewhy/lunarcn/mods/config/igbc.json"));


    @Override
    public void preInit() {
        config.load()
                .initValue("id", "");
        thread = new HeartThread(config.getValue("id").getAsString());
        thread.start();

        List<Long> sent = new ArrayList<>();

        EventBus.subscribe(CommentUpdateEvent.class, (e) -> {
            for (Comment comment : e.comments) {
                System.out.println("Receive Comment! " + comment.getText());
                Minecraft mc = Minecraft.getMinecraft();
                if (mc != null && mc.thePlayer != null) {
                    if (sent.contains(comment.getTime())) {
                        continue;
                    }
                    // add to send queue
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("text", "§6[直播] §b" + comment);
                    mc.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()));
                    sent.add(comment.getTime());
                }
            }
        });
    }
}
