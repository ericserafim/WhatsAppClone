package whatsappclone.ericserafim.com.whatsappclone.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import whatsappclone.ericserafim.com.whatsappclone.R;
import whatsappclone.ericserafim.com.whatsappclone.activity.ConversaActivity;
import whatsappclone.ericserafim.com.whatsappclone.adapter.ContatoAdapter;
import whatsappclone.ericserafim.com.whatsappclone.application.ConfiguracaoFirebase;
import whatsappclone.ericserafim.com.whatsappclone.helper.Preferencias;
import whatsappclone.ericserafim.com.whatsappclone.model.Contato;

public class ContatosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    private ValueEventListener listenerContatos;
    private DatabaseReference dataBase;

    public ContatosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contatos = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        listView = (ListView) view.findViewById(R.id.lv_contatos);
        adapter = new ContatoAdapter(getActivity(), contatos);
        listView.setAdapter(adapter);

        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();
        dataBase = ConfiguracaoFirebase.getDatabase()
                .child("contatos")
                .child(identificadorUsuarioLogado);

        listenerContatos = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    contatos.clear();

                                    //Listar os contatos do usuário
                                    for (DataSnapshot dados: dataSnapshot.getChildren()) {
                                        Contato contato = dados.getValue(Contato.class);
                                        contatos.add(contato);
                                    }

                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Cria e configura a intent
                Intent intent = new Intent(getActivity(), ConversaActivity.class);
                intent.putExtra("destinatario", contatos.get(position));

                //Abre a activity
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        dataBase.addValueEventListener(listenerContatos);
    }

    @Override
    public void onStop() {
        super.onStop();
        dataBase.removeEventListener(listenerContatos);
    }
}
