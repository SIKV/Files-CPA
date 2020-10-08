package com.example.files.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.files.model.FileModel;
import com.example.files.repository.FilesRepository;
import com.example.files.repository.impl.ExternalFilesRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private FilesRepository filesRepository = new ExternalFilesRepository();

    private MutableLiveData<List<FileModel>> filesLiveData;

    public LiveData<List<FileModel>> getFiles() {
        if (filesLiveData == null) {
            filesLiveData = new MutableLiveData<>();

            List<FileModel> files = filesRepository.getAllFiles();
            filesLiveData.setValue(files);
        }

        return filesLiveData;
    }
}