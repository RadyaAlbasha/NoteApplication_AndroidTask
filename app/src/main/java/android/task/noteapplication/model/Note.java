package android.task.noteapplication.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String noteTitle;

    private String noteBody;
    /*
        public Note()
        {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }
    */
    public Note(String noteTitle, String noteBody) {
        this.noteTitle = noteTitle;
        this.noteBody = noteBody;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteBody() {
        return noteBody;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteBody(String noteBody) {
        this.noteBody = noteBody;
    }

    @Override
    public String toString() {
        return noteTitle + ": " + noteBody;

    }
}
