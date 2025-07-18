package com.example.pdfdocument

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.pdfdocument.databinding.ActivityBottomSheetCoordinatorLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetCoordinatorLayoutActivity : AppCompatActivity() {
    lateinit var activityBottomSheetCoordinatorLayoutBinding: ActivityBottomSheetCoordinatorLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBottomSheetCoordinatorLayoutBinding =
            ActivityBottomSheetCoordinatorLayoutBinding.inflate(
                LayoutInflater.from(this)
            )
        setContentView(activityBottomSheetCoordinatorLayoutBinding.root)
        BottomSheetBehavior.from<LinearLayout>(activityBottomSheetCoordinatorLayoutBinding.layBottomView).state =
            BottomSheetBehavior.STATE_COLLAPSED

    }
}