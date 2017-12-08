package whatsappclone.ericserafim.com.whatsappclone.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsappclone.ericserafim.com.whatsappclone.R;
import whatsappclone.ericserafim.com.whatsappclone.adapter.MensagemAdapter;
import whatsappclone.ericserafim.com.whatsappclone.application.ConfiguracaoFirebase;
import whatsappclone.ericserafim.com.whatsappclone.helper.Preferencias;
import whatsappclone.ericserafim.com.whatsappclone.model.Contato;
import whatsappclone.ericserafim.com.whatsappclone.model.Conversa;
import whatsappclone.ericserafim.com.whatsappclone.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    private DatabaseReference dataBase;
    private Toolbar toolbar;
    private Contato destinatario;
    private Contato usuarioLogado;

    private EditText editMensagem;
    private ImageButton btEnviar;
    private ListView listView;
    private MensagemAdapter arrayAdapter;
    private ArrayList<Mensagem> mensagens;
    private ValueEventListener valueEventListenerMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        //Recupera o usuário logado no app
        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        usuarioLogado = new Contato();
        usuarioLogado.setIdentificadorUsuario(preferencias.getIdentificador());
        usuarioLogado.setNome(preferencias.getNome());

        editMensagem = (EditText) findViewById(R.id.edit_mensagem);
        btEnviar     = (ImageButton) findViewById(R.id.bt_enviar);
        toolbar      = (Toolbar) findViewById(R.id.tb_conversa);
        listView     = (ListView) findViewById(R.id.lv_mensagens);

        //Configura toolbar
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left);
        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            //Recuperar o contato (destinatário)
            destinatario = (Contato) extra.getSerializable("destinatario");
            if  (destinatario != null) toolbar.setTitle(destinatario.getNome());
        }

        //Define o uso da toolbar
        setSupportActionBar(toolbar);

        //Montagem da ListView de mensgaens
        mensagens = new ArrayList<>();
        arrayAdapter = new MensagemAdapter(ConversaActivity.this, mensagens);
        listView.setAdapter(arrayAdapter);

        //Recuperar mensagens do firebase
        dataBase = ConfiguracaoFirebase.getDatabase()
                    .child("mensagens")
                    .child(usuarioLogado.getIdentificadorUsuario())
                    .child(destinatario.getIdentificadorUsuario());

        //Criar listener para buscar mensagens
        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mensagens.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()) {
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dataBase.addValueEventListener(valueEventListenerMensagem);

        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoMensagem = editMensagem.getText().toString();
                
                if (textoMensagem.isEmpty()) {
                    Toast.makeText(ConversaActivity.this, "Digite uma mensagem para enviar", Toast.LENGTH_SHORT).show();
                } else {
                    //Salva mensagem no Firebase
                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(usuarioLogado.getIdentificadorUsuario());
                    mensagem.setMensagem(textoMensagem);

                    if (!salvarMensagem(usuarioLogado.getIdentificadorUsuario(), destinatario.getIdentificadorUsuario(), mensagem)) {
                        Toast.makeText(ConversaActivity.this, "Problema ao enviar mensagem, tente novamente", Toast.LENGTH_LONG).show();
                    }

                    if (!salvarMensagem(destinatario.getIdentificadorUsuario(), usuarioLogado.getIdentificadorUsuario(), mensagem)) {
                        Toast.makeText(ConversaActivity.this, "Problema ao enviar mensagem, tente novamente (2)", Toast.LENGTH_SHORT).show();
                    }

                    Conversa conversa = new Conversa();
                    conversa.setIdUsuario(destinatario.getIdentificadorUsuario());
                    conversa.setNome(destinatario.getNome());
                    conversa.setMensagem(textoMensagem);

                    if (!salvarConversa(usuarioLogado.getIdentificadorUsuario(), destinatario.getIdentificadorUsuario(), conversa)) {
                        Toast.makeText(ConversaActivity.this, "Problema ao salva conversa", Toast.LENGTH_SHORT).show();
                    }

                    conversa = new Conversa();
                    conversa.setIdUsuario(usuarioLogado.getIdentificadorUsuario());
                    conversa.setNome(usuarioLogado.getNome());
                    conversa.setMensagem(textoMensagem);

                    if (!salvarConversa(destinatario.getIdentificadorUsuario(), usuarioLogado.getIdentificadorUsuario(), conversa)) {
                        Toast.makeText(ConversaActivity.this, "Problema ao salvar ocnversa (2)", Toast.LENGTH_SHORT).show();
                    }

                    editMensagem.setText("");
                }
            }
        });
    }

    private Boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem) {
        try {
            dataBase = ConfiguracaoFirebase.getDatabase().child("mensagens");
            dataBase.child(idRemetente)
                    .child(idDestinatario)
                    .push()
                    .setValue(mensagem);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Boolean salvarConversa(String idRemetente, String idDestinatario, Conversa conversa) {
        try {
            dataBase = ConfiguracaoFirebase.getDatabase().child("conversas");
            dataBase.child(idRemetente)
                    .child(idDestinatario)
                    .setValue(conversa);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dataBase.removeEventListener(valueEventListenerMensagem);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        dataBase.addValueEventListener(valueEventListenerMensagem);
    }
}
