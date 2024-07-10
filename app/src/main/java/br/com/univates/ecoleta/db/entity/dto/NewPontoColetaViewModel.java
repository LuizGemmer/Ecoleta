package br.com.univates.ecoleta.db.entity.dto;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewPontoColetaViewModel extends ViewModel {

    private MutableLiveData<Bitmap> capturedImage = new MutableLiveData<>();

    public LiveData<Bitmap> getCapturedImage() {
        return capturedImage;
    }

    public void setCapturedImage(Bitmap imageBitmap) {
        capturedImage.setValue(imageBitmap);
    }
}