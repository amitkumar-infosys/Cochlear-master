package com.example.cochlear.ui.patient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PatientViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is patient Fragment"
    }
    val text: LiveData<String> = _text
}