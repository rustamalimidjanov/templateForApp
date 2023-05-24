package com.example.templateforapp

import androidx.lifecycle.ViewModel

class CrimeListViewModel: ViewModel() {
    val crimes = mutableListOf<Crime>()
    init {

    }
}