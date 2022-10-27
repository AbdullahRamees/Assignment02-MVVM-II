package com.example.mvvm_tests.dialogFragmnet

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm_tests.databinding.DialogFragmentErrorLayoutBinding

class ErrorDialogFragmnet(ErrorMassage :String?): DialogFragment() {
    val  ErrorMassageRef : String? = ErrorMassage

    private var _binding: DialogFragmentErrorLayoutBinding? = null
    private val binding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = DialogFragmentErrorLayoutBinding.inflate(inflater, container, false)
            return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.descripition.text = ErrorMassageRef
        binding.closeButton.setOnClickListener {
            this.dismiss()
        }
    }


}