package org.cubewhy.comment.stream;

import lombok.SneakyThrows;
import net.weavemc.loader.api.event.EventBus;
import org.cubewhy.comment.events.CommentUpdateEvent;

import java.util.List;

public class HeartThread extends Thread {
    public StreamInfo stream;
    private List<Comment> last;

    public HeartThread(String liveID) {
        if (liveID != null && !liveID.isEmpty()) {
            this.stream = new StreamInfo(liveID);
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            if (this.stream == null) {
                break;
            }
            List<Comment> comments = stream.getComments();
            if (comments == null) {
                break;
            }

            if (!comments.equals(last)) {
                last = comments;
                EventBus.callEvent(new CommentUpdateEvent(comments));
            }
            Thread.sleep(5000); // wait 5s
        }
    }
}
