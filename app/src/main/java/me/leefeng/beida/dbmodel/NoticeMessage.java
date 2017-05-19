package me.leefeng.beida.dbmodel;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by limxing on 2017/5/18.
 */
@Table("db_notice")
public class NoticeMessage {
    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    private String messageId;
    private String alias;
    private String content;
    private String description;
    private String title;
    private String type;
    private Long time;
    private boolean isRead;

    public NoticeMessage(String messageId, String alias, String content, String description,
                         String title, String type) {
        this.messageId = messageId;
        this.alias = alias;
        this.content = content;
        this.description = description;
        this.title = title;
        this.type = type;
    }

    public NoticeMessage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "NoticeMessage{" +
                "id=" + id +
                ", messageId='" + messageId + '\'' +
                ", alias='" + alias + '\'' +
                ", content='" + content + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
