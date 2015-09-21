package telegram;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import telegram.exception.TelegramBaseException;
import telegram.exception.TelegramErroResponse;
import telegram.exception.TelegramFileLimit;
import telegram.types.Message;
import telegram.types.ResponseParser;
import telegram.types.Update;
import telegram.types.User;
import telegram.types.UserProfilePhotos;

/**
 *
 * @author Victor Gerin de Lacerda
 *
 */
public class BotApi {

    private final String baseUrl = "https://api.telegram.org/bot<token>/<metodo>";
    private final String fileUrl = "https://api.telegram.org/file/bot<token>/<file_path>";

    public static byte[] MULTIPART_BOUNDARY;
    public static byte[] MULTIPART_CARRIAGE_RETURN_AND_NEWLINE;
    public static byte[] MULTIPART_TWO_HYPHENS;
    public static byte[] Content_Transfer_Encoding_base64;
    public static String Content_Disposition_file;
    public static String Content_Disposition;

    private final String token;

    public static enum TypeAction {

        typing, upload_photo, record_video, upload_video, record_audio, upload_audio, upload_document, find_location
    }

    public static class Metodo {

        public static enum Type {

            get, post
        }

        private Metodo(String nome, String adicionalField, Type type) {
            this.nome = nome;
            this.adicionalField = adicionalField;
            this.type = type;
        }

        private Metodo(String nome, String adicionalField) {
            this.nome = nome;
            this.adicionalField = adicionalField;
        }

        private Metodo(String nome, Type type) {
            this.nome = nome;
            this.type = type;
        }

        private Metodo(String nome) {
            this(nome, Type.post);
        }

        public String nome;
        public String adicionalField;
        public Type type = Type.post;
    }

    public static final Metodo getMe = new Metodo("getMe", Metodo.Type.get);
    public static final Metodo getUpdates = new Metodo("getUpdates", Metodo.Type.get);
    public static final Metodo getUserProfilePhotos = new Metodo("getUserProfilePhotos", Metodo.Type.get);
    public static final Metodo getFile = new Metodo("getFile", Metodo.Type.get);
    public static final Metodo sendMessage = new Metodo("sendMessage");
    public static final Metodo forwardMessage = new Metodo("forwardMessage");
    public static final Metodo sendPhoto = new Metodo("sendPhoto", "photo");
    public static final Metodo sendAudio = new Metodo("sendAudio", "audio");
    public static final Metodo sendDocument = new Metodo("sendDocument", "document");
    public static final Metodo sendSticker = new Metodo("sendSticker", "sticker");
    public static final Metodo sendVideo = new Metodo("sendVideo", "video");
    public static final Metodo sendVoice = new Metodo("sendVoice", "voice");
    public static final Metodo sendLocation = new Metodo("sendLocation");
    public static final Metodo sendChatAction = new Metodo("sendChatAction");

    /**
     * Create a new bot that will work with the given token
     *
     * @param token Token that was given by Telegram
     */
    public BotApi(String token) {
        try {
            BotApi.Content_Disposition = "Content-Disposition: form-data; name=\"<name>\"";
            BotApi.Content_Disposition_file = "Content-Disposition: form-data; name=\"<fieldName>\"; filename=\"<name>\"";
            BotApi.MULTIPART_BOUNDARY = "**05a5dbfc248e6425d627ca2a2e03c0d16bfaf82afa8ac3d7cd44e5190ddbe3d4**".getBytes("UTF-8");
            BotApi.MULTIPART_CARRIAGE_RETURN_AND_NEWLINE = "\r\n".getBytes("UTF-8");
            BotApi.MULTIPART_TWO_HYPHENS = "--".getBytes("UTF-8");
            BotApi.Content_Transfer_Encoding_base64 = "Content-Transfer-Encoding: base64".getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BotApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.token = token;
    }

    /**
     * Get the basic info of the bot, this only work for teste
     *
     * @return The basic info of the bot
     * @throws TelegramBaseException
     */
    public User getMe() throws TelegramBaseException {
        JSONObject execute = (JSONObject) execute(getMe, new Param[]{});
        return ResponseParser.objectParser(User.class, execute);
    }

    /**
     *
     * @param params Extras parameters that can be sent to telegram server
     * @return A list of updates that contains mensagens
     * @see https://core.telegram.org/bots/api#getupdates
     * @throws TelegramBaseException
     */
    public List<Update> getUpdates(Param... params) throws TelegramBaseException {
        JSONArray execute = (JSONArray) execute(getUpdates, params);
        return ResponseParser.arrayParser(Update.class, execute);
    }

    /**
     * Send a mensage to chat
     *
     * @param chat_id The chat id to where this message will be sent
     * @param text The text of message to be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendmessage
     * @throws TelegramBaseException
     */
    public Message sendMessage(int chat_id, String text, Param... params) throws TelegramBaseException {
        params = addParamsToArray(params, new Param("chat_id", Integer.toString(chat_id)), new Param("text", text));
        JSONObject execute = (JSONObject) execute(sendMessage, params);
        return ResponseParser.objectParser(Message.class, execute);
    }

    /**
     * Send a message to chat, that is like copy and past the message
     *
     * @param chat_id The chat id to where this message will be sent
     * @param from_chat_id The source of message
     * @param message_id the message id that will be forward
     * @return The message that was forward
     * @see https://core.telegram.org/bots/api#forwardmessage
     * @throws TelegramBaseException
     */
    public Message forwardMessage(int chat_id, int from_chat_id, int message_id) throws TelegramBaseException {
        JSONObject execute = (JSONObject) execute(forwardMessage,
                new Param("chat_id", Integer.toString(chat_id)),
                new Param("from_chat_id", Integer.toString(from_chat_id)),
                new Param("message_id", Integer.toString(message_id))
        );
        return ResponseParser.objectParser(Message.class, execute);
    }

    /**
     * Send a photo to chat use .jpg, .jpeg, .gif, .png, .tif or .bmp
     *
     * @param chat_id The chat id to where this photo will be sent
     * @param InputFile The file that need to be a photo
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendphoto
     * @throws TelegramBaseException
     * @throws java.io.IOException
     */
    public Message sendPhoto(int chat_id, File InputFile, Param... params) throws TelegramBaseException, IOException {
        return sendPhoto(chat_id, new FileInputStream(InputFile), getFileExtension(InputFile), params);
    }

    /**
     * Send a photo to chat, all data must be available if not only part will be
     * sent and use .jpg, .jpeg, .gif, .png, .tif or .bmp
     *
     * @param chat_id The chat id to where this photo will be sent
     * @param InputFile The file that need to be a photo
     * @param type The type of date like jpg, jpeg ...
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendphoto
     * @throws TelegramBaseException
     * @throws java.io.IOException
     */
    public Message sendPhoto(int chat_id, InputStream InputFile, String type, Param... params) throws TelegramBaseException, IOException {
        byte[] temp = new byte[InputFile.available()];
        InputFile.read(temp);
        return sendPhoto(chat_id, temp, type, params);
    }

    /**
     * Send a photo to chat use .jpg, .jpeg, .gif, .png, .tif or .bmp
     *
     * @param chat_id The chat id to where this photo will be sent
     * @param InputFile The file that need to be a photo
     * @param type The type of date like jpg, jpeg ...
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendphoto
     * @throws TelegramBaseException
     */
    public Message sendPhoto(int chat_id, byte[] InputFile, String type, Param... params) throws TelegramBaseException {
        params = addParamsToArray(params,
                Param.get("chat_id", Integer.toString(chat_id))
        );
        JSONObject execute = (JSONObject) execute(sendPhoto, InputFile, type, params);
        return ResponseParser.objectParser(Message.class, execute);
    }

    /**
     * Send a photo to chat
     *
     * @param chat_id The chat id to where this photo will be sent
     * @param photoId The Id of photo to be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendphoto
     * @throws TelegramBaseException
     */
    public Message sendPhoto(int chat_id, String photoId, Param... params) throws TelegramBaseException {
        params = addParamsToArray(params,
                Param.get("chat_id", Integer.toString(chat_id)),
                Param.get(sendPhoto.adicionalField, photoId)
        );
        JSONObject execute = (JSONObject) execute(sendPhoto, params);
        return ResponseParser.objectParser(Message.class, execute);
    }

    /**
     * Use this method to send audio files, if you want Telegram clients to
     * display them in the music player. Your audio must be in the .mp3 format.
     *
     * @param chat_id The chat to where this audio will be sent
     * @param InputFile The file that contains a audiun
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendaudio
     * @throws TelegramBaseException
     * @throws java.io.IOException
     */
    public Message sendAudio(int chat_id, File InputFile, Param... params) throws TelegramBaseException, IOException {
        return sendAudio(chat_id, new FileInputStream(InputFile), params);
    }

    /**
     * Use this method to send audio files, if you want Telegram clients to
     * display them in the music player. Your audio must be in the .mp3 format.,
     * all data must be available to read if not only part will sent
     *
     * @param chat_id The chat to where this audio will be sent
     * @param InputFile The file that contains a audiun
     * @param params Extras parameters that can be sent to telegram server
     * @return
     * @see https://core.telegram.org/bots/api#sendaudio
     * @throws java.io.IOException
     * @throws TelegramBaseException
     */
    public Message sendAudio(int chat_id, InputStream InputFile, Param... params) throws TelegramBaseException, IOException {
        byte[] temp = new byte[InputFile.available()];
        InputFile.read(temp);
        return sendAudio(chat_id, temp, params);
    }

    /**
     * Use this method to send audio files, if you want Telegram clients to
     * display them in the music player. Your audio must be in the .mp3 format.
     *
     * @param chat_id The chat to where this audio will be sent
     * @param InputFile The file that contains a audiun
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendaudio
     * @throws TelegramBaseException
     */
    public Message sendAudio(int chat_id, byte[] InputFile, Param... params) throws TelegramBaseException {
        params = addParamsToArray(params, new Param("chat_id", Integer.toString(chat_id)));

        TelegramFileLimit.check(sendAudio.adicionalField, InputFile, "mp3", params);

        JSONObject execute = (JSONObject) execute(sendAudio, InputFile, "mp3", params);
        return ResponseParser.objectParser(Message.class, execute);
    }

    /**
     * Send a audio to chat
     *
     * @param chat_id The chat to where this audio will be sent
     * @param audioId The Id of audio to be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendaudio
     * @throws TelegramBaseException
     */
    public Message sendAudio(int chat_id, String audioId, Param... params) throws TelegramBaseException {
        params = addParamsToArray(params,
                Param.get("chat_id", Integer.toString(chat_id)),
                Param.get(sendAudio.adicionalField, audioId)
        );
        JSONObject execute = (JSONObject) execute(sendAudio, params);
        return ResponseParser.objectParser(Message.class, execute);
    }

    /**
     * Send a document to the chat
     *
     * @param chat_id The chat id that the document will be sent
     * @param InputFile The document that will be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#senddocument
     * @throws TelegramBaseException
     * @throws java.io.IOException
     */
    public Message sendDocument(int chat_id, File InputFile, Param... params) throws TelegramBaseException, IOException {
        return sendDocument(chat_id, new FileInputStream(InputFile), getFileExtension(InputFile), params);
    }

    /**
     * Send a document to the chat, all data must be available if not only part
     * will be sent
     *
     * @param chat_id The chat id that the document will be sent
     * @param InputFile The document that will be sent
     * @param type The type of file to be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#senddocument
     * @throws TelegramBaseException
     * @throws java.io.IOException
     */
    public Message sendDocument(int chat_id, InputStream InputFile, String type, Param... params) throws TelegramBaseException, IOException {
        byte[] temp = new byte[InputFile.available()];
        InputFile.read(temp);
        return sendDocument(chat_id, temp, type, params);
    }

    /**
     * Send a document to the chat
     *
     * @param chat_id The chat id that the document will be sent
     * @param InputFile The document that will be sent
     * @param type The type of file to be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#senddocument
     * @throws TelegramBaseException
     */
    public Message sendDocument(int chat_id, byte[] InputFile, String type, Param... params) throws TelegramBaseException {
        params = addParamsToArray(params, Param.get("chat_id", Integer.toString(chat_id)));

        TelegramFileLimit.check(sendDocument.adicionalField, InputFile, type, params);

        JSONObject execute = (JSONObject) execute(sendDocument, InputFile, type, params);
        return ResponseParser.objectParser(Message.class, execute);
    }

    /**
     * Send a document to the chat
     *
     * @param chat_id The chat id that the document will be sent
     * @param docId The Id of doc to be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#senddocument
     * @throws TelegramBaseException
     */
    public Message sendDocument(int chat_id, String docId, Param... params) throws TelegramBaseException {
        params = addParamsToArray(params,
                new Param("chat_id", Integer.toString(chat_id)),
                Param.get(sendDocument.adicionalField, docId)
        );
        JSONObject execute = (JSONObject) execute(sendDocument, params);
        return ResponseParser.objectParser(Message.class, execute);
    }

    /**
     * Use this method to send .webp stickers
     *
     * @param chat_id The chat id that the .webp stickers will be sent
     * @param InputFile The document that will be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendsticker
     * @throws TelegramBaseException
     * @throws java.io.IOException
     */
    public Message sendSticker(int chat_id, File InputFile, Param... params) throws TelegramBaseException, IOException {
        return sendSticker(chat_id, new FileInputStream(InputFile), getFileExtension(InputFile), params);
    }

    /**
     * Use this method to send .webp stickers, all data must be available if not
     * only part will be sent
     *
     * @param chat_id The chat id that the .webp stickers will be sent
     * @param InputFile The document that will be sent
     * @param type The type of file to be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendsticker
     * @throws TelegramBaseException
     * @throws java.io.IOException
     */
    public Message sendSticker(int chat_id, InputStream InputFile, String type, Param... params) throws TelegramBaseException, IOException {
        byte[] temp = new byte[InputFile.available()];
        InputFile.read(temp);
        return sendSticker(chat_id, temp, type, params);
    }

    /**
     * Use this method to send .webp stickers
     *
     * @param chat_id The chat id that the .webp stickers will be sent
     * @param InputFile The document that will be sent
     * @param type The type of file to be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendsticker
     * @throws TelegramBaseException
     */
    public Message sendSticker(int chat_id, byte[] InputFile, String type, Param... params) throws TelegramBaseException {
        params = addParamsToArray(params, new Param("chat_id", Integer.toString(chat_id)));

        JSONObject execute = (JSONObject) execute(sendSticker, InputFile, type, params);
        return ResponseParser.objectParser(Message.class, execute);
    }

    /**
     * Use this method to send .webp stickers
     *
     * @param chat_id The chat id that the .webp stickers will be sent
     * @param stickerId The .webp stickers Id to be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendsticker
     * @throws TelegramBaseException
     */
    public Message sendSticker(int chat_id, String stickerId, Param... params) throws TelegramBaseException {
        params = addParamsToArray(params,
                new Param("chat_id", Integer.toString(chat_id)),
                Param.get(sendSticker.adicionalField, stickerId)
        );
        JSONObject execute = (JSONObject) execute(sendSticker, params);
        return ResponseParser.objectParser(Message.class, execute);
    }

    /**
     * Use this method to send video files, Telegram clients support mp4 videos
     * (other formats may be sent as Document).
     *
     * @param chat_id The chat id that the video will be sent
     * @param InputFile The video that will be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendvideo
     * @throws TelegramBaseException
     * @throws java.io.IOException
     */
    public Message sendVideo(int chat_id, File InputFile, Param... params) throws TelegramBaseException, IOException {
        return sendVideo(chat_id, new FileInputStream(InputFile), params);
    }

    /**
     * Use this method to send video files, Telegram clients support mp4 videos
     * (other formats may be sent as Document). All data must be available if
     * not only part will be sent
     *
     * @param chat_id The chat id that the video will be sent
     * @param InputFile The video that will be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendvideo
     * @throws TelegramBaseException
     * @throws java.io.IOException
     */
    public Message sendVideo(int chat_id, InputStream InputFile, Param... params) throws TelegramBaseException, IOException {
        byte[] temp = new byte[InputFile.available()];
        InputFile.read(temp);
        return sendVideo(chat_id, temp, params);
    }

    /**
     * Use this method to send video files, Telegram clients support mp4 videos
     * (other formats may be sent as Document).
     *
     * @param chat_id The chat id that the video will be sent
     * @param InputFile The video that will be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendvideo
     * @throws TelegramBaseException
     */
    public Message sendVideo(int chat_id, byte[] InputFile, Param... params) throws TelegramBaseException {
        params = addParamsToArray(params, new Param("chat_id", Integer.toString(chat_id)));

        TelegramFileLimit.check(sendVideo.adicionalField, InputFile, "mp4", params);

        JSONObject execute = (JSONObject) execute(sendVideo, InputFile, "mp4", params);
        return ResponseParser.objectParser(Message.class, execute);
    }

    /**
     * Use this method to send video files, Telegram clients support mp4 videos
     * (other formats may be sent as Document).
     *
     * @param chat_id The chat id that the video will be sent
     * @param videoId The Video id to be send
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendvideo
     * @throws TelegramBaseException
     */
    public Message sendVideo(int chat_id, String videoId, Param... params) throws TelegramBaseException {
        params = addParamsToArray(params,
                new Param("chat_id", Integer.toString(chat_id)),
                Param.get(sendVideo.adicionalField, videoId)
        );
        JSONObject execute = (JSONObject) execute(sendVideo, params);
        return ResponseParser.objectParser(Message.class, execute);
    }

    /**
     * Use this method to send audio files
     *
     * @param chat_id The chat id that the audio will be sent
     * @param InputFile The audio that will be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendvoice
     * @throws TelegramBaseException
     * @throws java.io.IOException
     */
    public Message sendVoice(int chat_id, File InputFile, Param... params) throws TelegramBaseException, IOException {
        return sendVoice(chat_id, new FileInputStream(InputFile), getFileExtension(InputFile), params);
    }

    /**
     * Use this method to send audio files, all data must be available if not
     * only part will be sent
     *
     * @param chat_id The chat id that the audio will be sent
     * @param InputFile The audio that will be sent
     * @param type The type of file to be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendvoice
     * @throws TelegramBaseException
     * @throws java.io.IOException
     */
    public Message sendVoice(int chat_id, InputStream InputFile, String type, Param... params) throws TelegramBaseException, IOException {
        byte[] temp = new byte[InputFile.available()];
        InputFile.read(temp);
        return sendVoice(chat_id, temp, type, params);
    }

    /**
     * Use this method to send audio files
     *
     * @param chat_id The chat id that the audio will be sent
     * @param InputFile The audio that will be sent
     * @param type The type of file to be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendvoice
     * @throws TelegramBaseException
     */
    public Message sendVoice(int chat_id, byte[] InputFile, String type, Param... params) throws TelegramBaseException {
        params = addParamsToArray(params, new Param("chat_id", Integer.toString(chat_id)));

        TelegramFileLimit.check(sendVoice.adicionalField, InputFile, type, params);

        JSONObject execute = (JSONObject) execute(sendVoice, InputFile, type, params);
        return ResponseParser.objectParser(Message.class, execute);
    }

    /**
     * Use this method to send audio files
     *
     * @param chat_id The chat id that the audio will be sent
     * @param audioId The id audio to be sent
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendvoice
     * @throws TelegramBaseException
     */
    public Message sendVoice(int chat_id, String audioId, Param... params) throws TelegramBaseException {
        params = addParamsToArray(params,
                new Param("chat_id", Integer.toString(chat_id)),
                Param.get(sendVoice.adicionalField, audioId)
        );
        JSONObject execute = (JSONObject) execute(sendVoice, params);
        return ResponseParser.objectParser(Message.class, execute);
    }

    /**
     * Use this method to send point on the map
     *
     * @param chat_id The chat id that the location will be sent
     * @param latitude Latitude of location
     * @param longitude Longitude of location
     * @param params Extras parameters that can be sent to telegram server
     * @return The message that was sent
     * @see https://core.telegram.org/bots/api#sendlocation
     * @throws TelegramBaseException
     */
    public Message sendLocation(int chat_id, double latitude, double longitude, Param... params) throws TelegramBaseException {
        params = addParamsToArray(params,
                new Param("chat_id", Integer.toString(chat_id)),
                new Param("longitude", Double.toString(longitude)),
                new Param("latitude", Double.toString(latitude))
        );
        JSONObject execute = (JSONObject) execute(sendLocation, params);
        return ResponseParser.objectParser(Message.class, execute);
    }

    /**
     * Use this method to send a action menssage to the chat
     *
     * @param chat_id The chat id that the action menssage will be sent
     * @param action The action to be sent
     * @return True if works
     * @see https://core.telegram.org/bots/api#sendchataction
     * @throws TelegramBaseException
     */
    public boolean sendChatAction(int chat_id, TypeAction action) throws TelegramBaseException {
        Param[] params = new Param[2];
        params[0] = new Param("chat_id", Integer.toString(chat_id));
        params[1] = new Param("action", action.name());
        return (boolean) execute(sendChatAction, params);
    }

    /**
     * Use this method to get a list of profile pictures for a user
     *
     * @param user_id The user id to get the list
     * @param params Extras parameters that can be sent to telegram server
     * @return UserProfilePhotos the user photo list
     * @see https://core.telegram.org/bots/api#getuserprofilephotos
     * @throws TelegramBaseException
     */
    public UserProfilePhotos getUserProfilePhotos(int user_id, Param... params) throws TelegramBaseException {
        params = addParamsToArray(params, new Param("user_id", Integer.toString(user_id)));
        JSONObject execute = (JSONObject) execute(getUserProfilePhotos, params);
        return ResponseParser.objectParser(UserProfilePhotos.class, execute);
    }

    /**
     * Use this method to get basic info about a file and prepare it for
     * downloading
     *
     * @param file_id File identifier to get info about
     * @return The request info
     * @see https://core.telegram.org/bots/api#getfile
     * @throws TelegramBaseException
     */
    public telegram.types.File getFile(String file_id) throws TelegramBaseException {
        JSONObject execute = (JSONObject) execute(getFile, new Param[]{Param.get("file_id", file_id)});
        return ResponseParser.objectParser(telegram.types.File.class, execute);
    }

    /**
     * get InputStream Of the pass file
     *
     * @param file The file to get
     * @return The InputStream that contains the data of file
     * @see BotApi#getFile(java.lang.String)
     * @see BotApi#getFileData(telegram.types.File)
     * @throws IOException
     */
    public InputStream getInputStreamFile(telegram.types.File file) throws IOException {
        String url = fileUrl.replace("<token>", token).replace("<file_path>", file.getFile_path());
        try {
            return new URL(url).openStream();
        } catch (MalformedURLException ex) {
            Logger.getLogger(BotApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * get InputStream Of the pass file
     *
     * @param file_id The file id to get
     * @return The InputStream that contains the data of file
     * @see BotApi#getFile(java.lang.String)
     * @see BotApi#getFileData(telegram.types.File)
     * @throws IOException
     */
    public InputStream getInputStreamFile(String file_id) throws IOException, TelegramBaseException {
        return getInputStreamFile(getFile(file_id));
    }

    /**
     * Use this method to get all data os the telegram file
     *
     * @param file The file to get
     * @return The byte array with all content file
     * @see BotApi#getFile(java.lang.String)
     * @see BotApi#getInputStreamFile(telegram.types.File)
     * @throws IOException
     */
    @SuppressWarnings("empty-statement")
    public byte[] getFileData(telegram.types.File file) throws IOException {
        BufferedInputStream b = new BufferedInputStream(getInputStreamFile(file), file.getFile_size());

        //baixa tudo
        b.mark(Integer.MAX_VALUE);
        while (b.read() != -1);
        b.reset();

        //pegar o buffer
        byte[] temp = new byte[b.available()];
        b.read(temp);
        return temp;
    }

    /**
     * Use this method to get all data os the telegram file
     *
     * @param file_id The file id to get
     * @return The byte array with all content file
     * @see BotApi#getFile(java.lang.String)
     * @see BotApi#getInputStreamFile(telegram.types.File)
     * @throws IOException
     */
    public byte[] getFileData(String file_id) throws IOException, TelegramBaseException {
        return getFileData(getFile(file_id));
    }

    /**
     * This method works for calculate the max size of one file can have to be
     * sent, onces the telegram server only can recive 50MB of data and that
     * include all dada sent by the multipart-form soo the max file fize is not
     * 50MB but is (50MB - multipart-form-size), soo to do this calc use this
     * method
     *
     * @param metodo The Method that you will use like sendDocument or sendAudio
     * @param fileType The file type like txt, png, jpeg
     * @param params All parameters that you will use that include the required
     * and optional
     * @return The max file size that can be sent
     */
    public long calcTheMaxFileSizeToBeSent(Metodo metodo, String fileType, Param... params) {
        return TelegramFileLimit.maxSendSize - TelegramFileLimit.calcTotalSizeToSent(metodo.adicionalField, new byte[]{}, fileType, params);
    }

    private Param[] addParamsToArray(Param[] original, Param... paramsToAdd) {
        Param[] _final = Arrays.copyOf(paramsToAdd, paramsToAdd.length + original.length);
        System.arraycopy(original, 0, _final, paramsToAdd.length, original.length);
        return _final;
    }

    private String ParamToString(Param... params) {
        return String.join("&", Stream.of(params).map((p) -> {
            try {
                return p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(BotApi.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "";
        }).toArray((value) -> new String[value]));
    }

    private JSONObject readAndGetJSONObj(InputStream in) {
        String collect = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));

        if (!collect.isEmpty()) {
            return new JSONObject(collect);
        } else {
            return new JSONObject();
        }

    }

    private String getFileExtension(File f) {
        String extension = "";
        String fileName = f.getName();

        int i = fileName.lastIndexOf('.');
        if (i >= 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    private Object execute(Metodo metodo, Param... params) throws TelegramBaseException {
        return execute(metodo, null, null, params);
    }

    private void writeData(OutputStream out, byte[] fileToUpload, String fileType, Metodo metodo) throws IOException {
        String name;
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(fileToUpload);
            name = new String(Base64.getEncoder().encode(digest), "UTF-8");
        } catch (NoSuchAlgorithmException ex) {
            name = "hue";
            Logger.getLogger(BotApi.class.getName()).log(Level.SEVERE, null, ex);
        }

        name += "." + fileType;

        out.write(Content_Disposition_file
                .replaceAll("<fieldName>", metodo.adicionalField)
                .replaceAll("<name>", name)
                .getBytes("UTF-8"));
        out.write(MULTIPART_CARRIAGE_RETURN_AND_NEWLINE);
        out.write(MULTIPART_CARRIAGE_RETURN_AND_NEWLINE);
        if (out.equals(System.out)) {
            out.write("Content".getBytes());
        } else {
            out.write(fileToUpload);
        }
    }

    private void writeParams(OutputStream out, Metodo metodo, byte[] fileToUpload, String fileType, Param... params) throws IOException {
        if (params.length > 0) {
            for (Param param : params) {
                out.write(MULTIPART_TWO_HYPHENS);
                out.write(MULTIPART_BOUNDARY);
                out.write(MULTIPART_CARRIAGE_RETURN_AND_NEWLINE);
                out.write(Content_Disposition.replaceAll("<name>", param.getName()).getBytes("UTF-8"));
                out.write(MULTIPART_CARRIAGE_RETURN_AND_NEWLINE);
                out.write(MULTIPART_CARRIAGE_RETURN_AND_NEWLINE);
                out.write(param.getValue().getBytes("UTF-8"));
                out.write(MULTIPART_CARRIAGE_RETURN_AND_NEWLINE);
            }
        }
        if (fileToUpload != null) {
            out.write(MULTIPART_TWO_HYPHENS);
            out.write(MULTIPART_BOUNDARY);
            out.write(MULTIPART_CARRIAGE_RETURN_AND_NEWLINE);
            writeData(out, fileToUpload, fileType, metodo);
            out.write(MULTIPART_CARRIAGE_RETURN_AND_NEWLINE);
        }
        out.write(MULTIPART_TWO_HYPHENS);
        out.write(MULTIPART_BOUNDARY);
        out.write(MULTIPART_TWO_HYPHENS);
        out.write(MULTIPART_CARRIAGE_RETURN_AND_NEWLINE);
        out.flush();
    }

    private Object execute(Metodo metodo, byte[] fileToUpload, String fileType, Param... params) throws TelegramBaseException {
        try {
            String url = baseUrl.replace("<token>", token).replace("<metodo>", metodo.nome);
            if (metodo.type == Metodo.Type.get) {
                url += "?" + ParamToString(params);
            }

            HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();

            con.setRequestMethod(metodo.type == Metodo.Type.get ? "GET" : "POST");

            if (metodo.type == Metodo.Type.post && (params.length > 0 || fileToUpload != null)) {
                con.addRequestProperty("Content-Type", "multipart/form-data;boundary=" + new String(MULTIPART_BOUNDARY, "UTF-8"));
                con.setDoOutput(true);
                try (OutputStream out = con.getOutputStream()) {
                    writeParams(out, metodo, fileToUpload, fileType, params);
                }
            }

            JSONObject saida;

            if (con.getResponseCode() == 200) {
                saida = readAndGetJSONObj(con.getInputStream());
            } else {
                saida = readAndGetJSONObj(con.getErrorStream());
            }

            if (saida.getBoolean("ok")) {
                return saida.get("result");
            } else {
                throw new TelegramErroResponse(saida.optInt("error_code"), saida.getString("description"));
            }

        } catch (IOException ex) {
            throw new TelegramBaseException(ex.getMessage(), ex);
        }
    }
}
