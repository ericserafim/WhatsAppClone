package whatsappclone.ericserafim.com.whatsappclone.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {

    private final static String NOME_ARQUIVO = "whatsapp.preferencias";
    public static final String CHAVE_IDENTIFICADOR = "identificador";
    public static final String CHAVE_NOME = "nome";
    private final Context contexto;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    public Preferencias(Context contextoParametro) {
        this.contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void salvarDados(String identificador, String nome) {
        editor.putString(CHAVE_IDENTIFICADOR, identificador);
        editor.putString(CHAVE_NOME, nome);
        editor.commit();
    }

    public String getIdentificador() {
      return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNome() {
      return preferences.getString(CHAVE_NOME, null);
    }

}
