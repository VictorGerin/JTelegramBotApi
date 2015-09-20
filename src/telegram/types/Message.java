/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegram.types;

import java.util.Date;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author victor
 */
public class Message extends TelegramBaseClass {

    private int message_id = 0;
    private User from = null;
    private int date = 0;
    private String chat = null;
    private User forward_from = null;
    private int forward_date = 0;
    private Message reply_to_message = null;
    private String text = null;
    private Audio audio = null;
    private Document document = null;
    private List<PhotoSize> photo = null;
    private Sticker sticker = null;
    private Video video = null;
    private Voice voice = null;
    private String caption = null;
    private Contact contact = null;
    private Location location = null;
    private User new_chat_participant = null;
    private User left_chat_participant = null;
    private String new_chat_title = null;
    private List<PhotoSize> new_chat_photo = null;
    @isBoolean
    private int delete_chat_photo = -1;
    @isBoolean
    private int group_chat_created = -1;

    /**
     * Get the value of message_id
     *
     * @return the value of message_id
     */
    public int getMessage_id() {
        return message_id;
    }

    /**
     * Get the value of from
     *
     * @return the value of from
     */
    public User getFrom() {
        return from;
    }

    /**
     * Get the value of date
     *
     * @return the value of date
     */
    public int getTimeStamp() {
        return date;
    }

    public Date getDate() {
        return new Date((long) getTimeStamp() * 1000);
    }

    /**
     * Get the value of chat
     *
     * @return the value of chat
     */
    public String getChat() {
        return chat;
    }

    public boolean isUserChat() {
        return getUserChat().getFirst_name() != null;
    }

    public boolean isGroupChat() {
        return getGroupChat().getTitle() != null;
    }

    public User getUserChat() {
        return ResponseParser.objectParser(User.class, new JSONObject(getChat()));
    }

    public GroupChat getGroupChat() {
        return ResponseParser.objectParser(GroupChat.class, new JSONObject(getChat()));
    }
    
    public int getChatId() {
        return getGroupChat().getId();
    }

    /**
     * Get the value of forward_from
     *
     * @return the value of forward_from
     */
    public User getForward_from() {
        return forward_from;
    }

    /**
     * Get the value of forward_date
     *
     * @return the value of forward_date
     */
    public int getForward_date() {
        return forward_date;
    }

    /**
     * Get the value of reply_to_message
     *
     * @return the value of reply_to_message
     */
    public Message getReply_to_message() {
        return reply_to_message;
    }

    /**
     * Get the value of text
     *
     * @return the value of text
     */
    public String getText() {
        return text;
    }

    /**
     * Get the value of audio
     *
     * @return the value of audio
     */
    public Audio getAudio() {
        return audio;
    }

    /**
     * Get the value of document
     *
     * @return the value of document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Get the value of photo
     *
     * @return the value of photo
     */
    public List<PhotoSize> getPhotos() {
        return photo;
    }

    /**
     * Get the value of sticker
     *
     * @return the value of sticker
     */
    public Sticker getSticker() {
        return sticker;
    }

    /**
     * Set the value of sticker
     *
     * @param sticker new value of sticker
     */
    public void setSticker(Sticker sticker) {
        this.sticker = sticker;
    }

    /**
     * Get the value of video
     *
     * @return the value of video
     */
    public Video getVideo() {
        return video;
    }

    /**
     * Get the value of voice
     *
     * @return the value of voice
     */
    public Voice getVoice() {
        return voice;
    }

    /**
     * Get the value of caption
     *
     * @return the value of caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Get the value of contact
     *
     * @return the value of contact
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Get the value of location
     *
     * @return the value of location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Get the value of new_chat_participant
     *
     * @return the value of new_chat_participant
     */
    public User getNew_chat_participant() {
        return new_chat_participant;
    }

    /**
     * Get the value of left_chat_participant
     *
     * @return the value of left_chat_participant
     */
    public User getLeft_chat_participant() {
        return left_chat_participant;
    }

    /**
     * Get the value of new_chat_title
     *
     * @return the value of new_chat_title
     */
    public String getNew_chat_title() {
        return new_chat_title;
    }

    /**
     * Get the value of new_chat_photo
     *
     * @return the value of new_chat_photo
     */
    public List<PhotoSize> getNew_chat_photo() {
        return new_chat_photo;
    }

    /**
     * Get the value of delete_chat_photo
     *
     * @return the value of delete_chat_photo
     */
    public boolean isDelete_chat_photo() {
        return delete_chat_photo != 0 && delete_chat_photo != -1;
    }

    public void setDelete_chat_photo(boolean delete_chat_photo) {
        this.delete_chat_photo = delete_chat_photo ? 1 : 0;
    }

    public void removeDelete_chat_photo() {
        this.delete_chat_photo = -1;
    }

    /**
     * Get the value of group_chat_created
     *
     * @return the value of group_chat_created
     */
    public boolean isGroup_chat_created() {
        return group_chat_created != 0 && group_chat_created != -1;
    }

    public void setGroup_chat_created(boolean group_chat_created) {
        this.group_chat_created = group_chat_created ? 1 : 0;
    }

    public void removeGroup_chat_created() {
        this.group_chat_created = -1;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public void removeMessage_id() {
        setMessage_id(0);
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public void removeFrom() {
        setFrom(null);
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void removeDate() {
        setDate(0);
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public void removeChat() {
        setChat(null);
    }

    public void setForward_from(User forward_from) {
        this.forward_from = forward_from;
    }

    public void removeForward_from() {
        setForward_from(null);
    }

    public void setForward_date(int forward_date) {
        this.forward_date = forward_date;
    }

    public void removeForward_date() {
        setForward_date(0);
    }

    public void setReply_to_message(Message reply_to_message) {
        this.reply_to_message = reply_to_message;
    }

    public void removeReply_to_message() {
        setReply_to_message(null);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void removeText() {
        setText(null);
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public void removeAudio() {
        setAudio(null);
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public void removeDocument() {
        setDocument(null);
    }

    public void setPhoto(List<PhotoSize> photo) {
        this.photo = photo;
    }

    public void removePhoto() {
        setPhoto(null);
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public void removeVideo() {
        setVideo(null);
    }

    public void setVoice(Voice voice) {
        this.voice = voice;
    }

    public void removeVoice() {
        setVoice(null);
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void removeCaption() {
        setCaption(null);
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public void removeContact() {
        setContact(null);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void removeLocation() {
        setLocation(null);
    }

    public void setNew_chat_participant(User new_chat_participant) {
        this.new_chat_participant = new_chat_participant;
    }

    public void removeNew_chat_participant() {
        setNew_chat_participant(null);
    }

    public void setLeft_chat_participant(User left_chat_participant) {
        this.left_chat_participant = left_chat_participant;
    }

    public void removeLeft_chat_participant() {
        setLeft_chat_participant(null);
    }

    public void setNew_chat_title(String new_chat_title) {
        this.new_chat_title = new_chat_title;
    }

    public void removeNew_chat_title() {
        setNew_chat_title(null);
    }

    public void setNew_chat_photo(List<PhotoSize> new_chat_photo) {
        this.new_chat_photo = new_chat_photo;
    }

    public void removeNew_chat_photo() {
        setNew_chat_photo(null);
    }

}
