package com.example.pdfdocument

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.pdfdocument.databinding.ActivityCoordinatorLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import android.view.View

class CoordinatorLayoutActivity : AppCompatActivity() {
    lateinit var activityCoordinatorLayoutBinding: ActivityCoordinatorLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCoordinatorLayoutBinding =
            ActivityCoordinatorLayoutBinding.inflate(LayoutInflater.from(this))
        setContentView(activityCoordinatorLayoutBinding.root)
        activityCoordinatorLayoutBinding.faq.setOnTouchListener(object : View.OnTouchListener {
            var dX = 0f
            var dY = 0f
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = v?.x?.minus(event.rawX) ?: 0f
                        dY = v?.y?.minus(event.rawY) ?: 0f

                    }

                    MotionEvent.ACTION_MOVE -> {
                        v?.animate()?.x(event.rawX + dX)?.y(event.rawY + dY)?.setDuration(0)?.start()
                    }
                }

                return true
            }
        })
    }
}