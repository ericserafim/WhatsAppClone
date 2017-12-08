package whatsappclone.ericserafim.com.whatsappclone.helper;


import android.util.Base64;

public class Base64Custom {

    public static String converterBase64(String texto) {
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).trim();
    }

    public static String decodificarBase64 (String textoCodificado) {
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}
