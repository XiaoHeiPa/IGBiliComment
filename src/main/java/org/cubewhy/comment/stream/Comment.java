package org.cubewhy.comment.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Comment {
    String nickname;
    String text;
    long time;

    @Override
    public String toString() {
        return nickname + ": " + text;
    }
}
