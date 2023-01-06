package com.example.requestprmissionsresultapi

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

inline fun <reified T : ViewBinding> AppCompatActivity.viewBinding(
    noinline bindingInflater: (LayoutInflater) -> T
) = ActivityBindingProperty(this, bindingInflater)