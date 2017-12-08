package whatsappclone.ericserafim.com.whatsappclone.application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfiguracaoFirebase {

    private static FirebaseApp firebase;
    private static FirebaseAuth firebaseAuth;
    private static DatabaseReference database;

    public static FirebaseApp getFirebase() {

        if (firebase == null) {
            firebase = FirebaseApp.getInstance();
        }

        return firebase;
    }

    public static FirebaseAuth getFirebaseAuth() {

        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }

        return firebaseAuth;
    }

    public static DatabaseReference getDatabase() {

        if (database == null) {
            database = FirebaseDatabase.getInstance().getReference();
        }

        return database;
    }
}
