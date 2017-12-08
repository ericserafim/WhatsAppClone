package whatsappclone.ericserafim.com.whatsappclone.helper;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static Boolean validaPermissoes(int requestCode, Activity activity, String[] permissoes) {

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> listaPermissao = new ArrayList<>();

            for (String permissao: permissoes) {
                Boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!validaPermissao) listaPermissao.add(permissao);
            }

            if (listaPermissao.isEmpty()) return true;

            String[] arrayPermissoes = new String[listaPermissao.size()];
            listaPermissao.toArray(arrayPermissoes);

            //Solicita permissões
            ActivityCompat.requestPermissions(activity, arrayPermissoes, requestCode);
        }

        return true;
    }
}
