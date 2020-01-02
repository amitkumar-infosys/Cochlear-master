package com.example.cochlear.ui.clinical

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClinicalViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is clinical Fragment"
    }
    val text: LiveData<String> = _text
}