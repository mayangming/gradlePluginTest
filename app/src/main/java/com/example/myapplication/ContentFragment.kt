package com.example.myapplication

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

class ContentFragment: Fragment() {
    private val viewModel: TestViewModel by viewModels()
}