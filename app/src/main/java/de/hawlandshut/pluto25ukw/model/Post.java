package de.hawlandshut.pluto25ukw.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.HashMap;

public class Post {
    public String uid;
    public String email;
    public String title;
    public String body;
    public Date createdAt;
    public String firestoreKey;
    public HashMap sys;

    public Post(String uid,
                String email,
                String title,
                String body,
                String firestoreKey,
                Date createdAt,
                HashMap sys
    ) {
        this.uid = uid;
        this.email = email;
        this.title = title;
        this.body = body;
        this.firestoreKey = firestoreKey;
        this.createdAt = createdAt;
        this.sys = sys;
    }

    public static Post fromDocument(DocumentSnapshot doc){
        String uid, email, title, body, firestoreKey;
        firestoreKey = (String) doc.getId();
        uid = (String) doc.get("uid");
        title = (String) doc.get("title");
        email = (String) doc.get("email");
        body = (String) doc.get("body");

        Date createdAt;
        Timestamp h_date = (Timestamp) doc.get("createdAt");
        if (h_date == null){
            createdAt = new Date(); // Datum des Clients als Ersatzwert;
        }
        else {
            createdAt = h_date.toDate();
        }

        return new Post(uid, email, title, body, firestoreKey, createdAt, null);

    }
}