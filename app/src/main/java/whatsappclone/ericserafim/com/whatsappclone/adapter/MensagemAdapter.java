package whatsappclone.ericserafim.com.whatsappclone.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import whatsappclone.ericserafim.com.whatsappclone.R;
import whatsappclone.ericserafim.com.whatsappclone.helper.Preferencias;
import whatsappclone.ericserafim.com.whatsappclone.model.Mensagem;

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context context;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(Context context,  ArrayList<Mensagem> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mensagens = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (mensagens != null) {
            //Pega a mensagem  na lista
            Mensagem mensagem = mensagens.get(position);
            Preferencias preferencias = new Preferencias(context);
            String idUsuarioLogado = preferencias.getIdentificador();

            //Inicializa objeto para montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            if (idUsuarioLogado.equals(mensagem.getIdUsuario())) {
                //Monta view a partir do xml
                view = inflater.inflate(R.layout.item_mensagem_direita, parent, false);
            } else {
                //Monta view a partir do xml
                view = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
            }

            //Recupera elementos da view para exibição do conteúdo
            TextView textView = (TextView) view.findViewById(R.id.tv_mensagem);

            //Exibe a mensagem
            textView.setText(mensagem.getMensagem());
        }

        return view;

    }
}
