package com.example.cochlear.ui.clinical

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.cochlear.MainActivity
import com.example.cochlear.R
import com.example.cochlear.ui.Constants
import kotlinx.android.synthetic.main.fragment_clinical_setting.*

class ClinicalFragment : Fragment() {
    private var volumeLeftClinic = 0
    private var volumeRightClinic = 0

    private lateinit var clinicalViewModel: ClinicalViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clinicalViewModel =
            ViewModelProviders.of(this).get(ClinicalViewModel::class.java)
        return inflater.inflate(R.layout.fragment_clinical_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getData()
        setLeftVolumeData()
        setRightVolumeData()

        left_vol_up.setOnClickListener {
            if (volumeLeftClinic < 10) {
                volumeLeftClinic++
            }
            (activity as MainActivity).saveInt(Constants.VOL_LEFT_CLINIC, volumeLeftClinic)
            setLeftVolumeData()
        }

        left_vol_down.setOnClickListener {
            if (volumeLeftClinic > 1) {
                volumeLeftClinic--
            }
            (activity as MainActivity).saveInt(Constants.VOL_LEFT_CLINIC, volumeLeftClinic)
            setLeftVolumeData()
        }
        right_vol_up.setOnClickListener {
            if (volumeRightClinic < 10) {
                volumeRightClinic++
            }
            (activity as MainActivity).saveInt(Constants.VOL_RIGHT_CLINIC, volumeRightClinic)
            setRightVolumeData()
        }

        right_vol_down.setOnClickListener {
            if (volumeRightClinic > 1) {
                volumeRightClinic--
            }
            (activity as MainActivity).saveInt(Constants.VOL_RIGHT_CLINIC, volumeRightClinic)
            setRightVolumeData()
        }
    }

    private fun getData() {
        volumeLeftClinic = (activity as MainActivity).getIntValue(Constants.VOL_LEFT_CLINIC)
        volumeRightClinic = (activity as MainActivity).getIntValue(Constants.VOL_RIGHT_CLINIC)
    }


    private fun setLeftVolumeData() {
        clinic_left_value.text = volumeLeftClinic.toString()
        clinic_left_seperator.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                volumeLeftClinic.toFloat()
            )
    }

    private fun setRightVolumeData() {
        clinic_right_value.text = volumeRightClinic.toString()
        clinic_right_seperator.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                volumeRightClinic.toFloat()
            )
    }
}