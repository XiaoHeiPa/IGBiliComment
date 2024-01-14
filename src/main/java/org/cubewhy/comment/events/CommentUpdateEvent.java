package org.cubewhy.comment.events;

import net.weavemc.loader.api.event.Event;
import org.cubewhy.comment.stream.Comment;

import java.util.List;

public class CommentUpdateEvent extends Event {

    public final List<Comment> comments;

    public CommentUpdateEvent(List<Comment> comments) {
        this.comments = comments;
    }
}
