package whatsappclone.ericserafim.com.whatsappclone.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import whatsappclone.ericserafim.com.whatsappclone.R;
import whatsappclone.ericserafim.com.whatsappclone.model.Contato;

public class ContatoAdapter extends ArrayAdapter<Contato> {

    private Context context;
    private ArrayList<Contato> contatos;

    public ContatoAdapter(Context context, ArrayList<Contato> objects) {
        super(context, 0, objects);
        this.context = context;
        this.contatos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (contatos != null) {
            //Inicializa objeto para montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_contatos, parent, false);

            //Recupera elementos da view para exibição do conteúdo
            TextView textView = (TextView) view.findViewById(R.id.tv_nome);

            //Pega o contato selecionado na lista
            Contato contato = contatos.get(position);

            //Move nome do contato para o campo da view
            textView.setText(contato.getNome());
        }

        return view;
    }
}
