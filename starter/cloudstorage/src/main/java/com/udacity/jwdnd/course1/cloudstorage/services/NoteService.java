package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotes(int userId) {
        return noteMapper.getAllUsersNotes(userId);
    }
    public void createNote(Note note){
        noteMapper.insert(note);
    }
    public void removeNote(int id){
        noteMapper.delete(id);
    }
}
