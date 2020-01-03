package com.example.cochlear.ui.clinical

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.cochlear.MainActivity
import com.example.cochlear.R
import com.example.cochlear.ui.Constants.Companion.VOL_LEFT_CLINIC
import com.example.cochlear.ui.Constants.Companion.VOL_RIGHT_CLINIC
import kotlinx.android.synthetic.main.fragment_clinical_setting.*

class ClinicalFragment : Fragment() {
    private var clinicLeftVolume = 0
    private var clinicRightVolume = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clinical_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // these methods are called every time activity is created/started
        getData()
        setLeftVolumeData()
        setRightVolumeData()

        //SetOnClickListeners will listen and respond to corresponding button clicks.
        left_vol_up.setOnClickListener {
            if (clinicLeftVolume < 10) {
                clinicLeftVolume++
            }
            (activity as MainActivity).saveInt(VOL_LEFT_CLINIC, clinicLeftVolume)
            setLeftVolumeData()
        }

        left_vol_down.setOnClickListener {
            if (clinicLeftVolume > 1) {
                clinicLeftVolume--
            }
            (activity as MainActivity).saveInt(VOL_LEFT_CLINIC, clinicLeftVolume)
            setLeftVolumeData()
        }
        right_vol_up.setOnClickListener {
            if (clinicRightVolume < 10) {
                clinicRightVolume++
            }
            (activity as MainActivity).saveInt(VOL_RIGHT_CLINIC, clinicRightVolume)
            setRightVolumeData()
        }

        right_vol_down.setOnClickListener {
            if (clinicRightVolume > 1) {
                clinicRightVolume--
            }
            (activity as MainActivity).saveInt(VOL_RIGHT_CLINIC, clinicRightVolume)
            setRightVolumeData()
        }
    }

    // this method retrieves all the data from Shared Preferences and updates it to the current fragment's values.
    private fun getData() {
        clinicLeftVolume = (activity as MainActivity).getIntValue(VOL_LEFT_CLINIC)
        clinicRightVolume = (activity as MainActivity).getIntValue(VOL_RIGHT_CLINIC)
    }

    // this method sets data for left Ear's Clinical setting data on UI
    private fun setLeftVolumeData() {
        clinic_left_value.text = clinicLeftVolume.toString()
        clinic_left_seperator.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                clinicLeftVolume.toFloat()
            )
    }

    // this method sets data for right Ear's Clinical setting data on UI
    private fun setRightVolumeData() {
        clinic_right_value.text = clinicRightVolume.toString()
        clinic_right_seperator.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                clinicRightVolume.toFloat()
            )
    }
}