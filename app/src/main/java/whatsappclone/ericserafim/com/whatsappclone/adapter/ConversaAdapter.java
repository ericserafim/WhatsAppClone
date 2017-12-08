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
import whatsappclone.ericserafim.com.whatsappclone.model.Conversa;

public class ConversaAdapter extends ArrayAdapter<Conversa> {

    private Context context;
    private ArrayList<Conversa> conversas;

    public ConversaAdapter(Context context, ArrayList<Conversa> objects) {
        super(context, 0, objects);
        this.context = context;
        this.conversas = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (conversas != null) {
            //Inicializa objeto para montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_conversas, parent, false);

            //Recupera elementos da view para exibição do conteúdo
            TextView nome = (TextView) view.findViewById(R.id.text_nome);
            TextView ultimaConversa = (TextView) view.findViewById(R.id.text_ultima_conversa);

            //Pega o contato selecionado na lista
            Conversa conversa = conversas.get(position);

            //Move nome do contato para o campo da view
            nome.setText(conversa.getNome());
            ultimaConversa.setText(conversa.getMensagem());
        }

        return view;
    }
}
