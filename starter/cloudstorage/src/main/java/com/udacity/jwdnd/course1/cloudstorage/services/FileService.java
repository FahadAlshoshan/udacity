package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFiles(int userId) {
        return fileMapper.getAllUsersFiles(userId);
    }
    public File getFile(int fileId) {
        return fileMapper.getFileById(fileId);
    }
    public boolean isFilenameAvailable(String filename) {
        return fileMapper.getFileByName(filename) == null;
    }

    public void createFile(File file){
        fileMapper.insert(file);
    }
    public void removeFile(int id){
        fileMapper.delete(id);
    }
}
