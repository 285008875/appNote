package com.maimemo.android.memonote.network.base;
import android.net.ParseException;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.maimemo.android.memonote.model.UserResModel;
import org.json.JSONException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Objects;
import javax.net.ssl.SSLHandshakeException;
import okhttp3.ResponseBody;
import retrofit2.HttpException;



public class RetrofitException {
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ResponseThrowable retrofitException(Throwable e) throws IOException {
        ResponseThrowable ex;
        ResponseBody body = Objects.requireNonNull(((HttpException) e).response()).errorBody();
        Gson gson = new Gson();
        assert body != null;
        UserResModel result = gson.fromJson(body.string(), UserResModel.class);
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;

            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex = new ResponseThrowable( ERROR.HTTP_ERROR,"网络错误",result.getError() ,result.getMessage());

                    break;
            }
            return ex;
        }  else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ResponseThrowable(ERROR.PARSE_ERROR, "解析错误",result.getError() ,result.getMessage());

            return ex;
        } else if (e instanceof ConnectException
                || e instanceof SocketTimeoutException
                || e instanceof UnknownHostException) {
            ex = new ResponseThrowable(ERROR.NETWORD_ERROR,"连接失败",result.getError(),result.getMessage() );

            return ex;
        } else if (e instanceof SSLHandshakeException) {
            ex = new ResponseThrowable(ERROR.SSL_ERROR,"证书验证失败",result.getError(),result.getMessage());

            return ex;
        } else {
            ex = new ResponseThrowable( ERROR.UNKNOWN, "未知错误",result.getError(),result.getMessage());
            return ex;
        }
    }
    /**
     * 约定异常
     */
    class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;
        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;
    }




    public static class ResponseThrowable extends Exception {
        public int msgCode;
        public String message;
        public int errorCode;
        public String serverMessage;



        public ResponseThrowable(String message, int msgCode) {
            super(message);
            this.msgCode = msgCode;
        }
        public ResponseThrowable(int msgCode,String message ,int errorCode, String serverMessage) {
            this.msgCode = msgCode;
        }
        public ResponseThrowable(String message, Throwable cause, int msgCode) {
            super(message, cause);
            this.msgCode = msgCode;
        }

        public ResponseThrowable(Throwable cause, int msgCode) {
            super(cause);
            this.msgCode = msgCode;
        }



        public int getMsgCode() {
            return msgCode;
        }

        public void setMsgCode(int msgCode) {
            this.msgCode = msgCode;
        }

        @Nullable
        @Override
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getServerMessage() {
            return serverMessage;
        }

        public void setServerMessage(String serverMessage) {
            this.serverMessage = serverMessage;
        }
    }
}