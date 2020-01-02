package com.example.cochlear.ui.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.cochlear.MainActivity
import com.example.cochlear.R
import com.example.cochlear.ui.Constants
import com.example.cochlear.ui.Constants.Companion.SYNCED
import com.example.cochlear.ui.Constants.Companion.VOL_LEFT_CLINIC
import com.example.cochlear.ui.Constants.Companion.VOL_LEFT_PATIENT
import com.example.cochlear.ui.Constants.Companion.VOL_RIGHT_CLINIC
import com.example.cochlear.ui.Constants.Companion.VOL_RIGHT_PATIENT
import kotlinx.android.synthetic.main.fragment_patient_control.*

class PatientFragment : Fragment() {
    private var leftVolumePatient = 0
    private var leftVolumeClinic = 0
    private var rightVolumePatient = 0
    private var rightVolumeClinic = 0
    private var synced = false

    private lateinit var patientViewModel: PatientViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        patientViewModel =
            ViewModelProviders.of(this).get(PatientViewModel::class.java)
        return inflater.inflate(R.layout.fragment_patient_control, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getData()
        checkSyncedStatus()
        setLeftVolumeData()
        setRightVolumeData()

        right_vol_down.setOnClickListener {
            if (rightVolumePatient > 1) {
                rightVolumePatient--
            }
            (activity as MainActivity).saveInt(Constants.VOL_RIGHT_PATIENT, rightVolumePatient)
            setRightVolumeData()
        }

        right_vol_up.setOnClickListener {
            if (rightVolumePatient < 10) {
                rightVolumePatient++
            }
            (activity as MainActivity).saveInt(Constants.VOL_RIGHT_PATIENT, rightVolumePatient)
            setRightVolumeData()
        }

        left_vol_down.setOnClickListener {
            if (leftVolumePatient > 1) {
                leftVolumePatient--
            }
            (activity as MainActivity).saveInt(VOL_LEFT_PATIENT, leftVolumePatient)
            setLeftVolumeData()
        }

        left_vol_up.setOnClickListener {
            if (leftVolumePatient < 10) {
                leftVolumePatient++
            }
            (activity as MainActivity).saveInt(VOL_LEFT_PATIENT, leftVolumePatient)
            setLeftVolumeData()
        }

        sync_vol_up.setOnClickListener {
            if (leftVolumePatient < 10 && rightVolumePatient < 10) {
                leftVolumePatient++
                rightVolumePatient++
            }
            (activity as MainActivity).saveInt(VOL_LEFT_PATIENT, leftVolumePatient)
            (activity as MainActivity).saveInt(VOL_RIGHT_PATIENT, rightVolumePatient)
            setLeftVolumeData()
            setRightVolumeData()
        }

        sync_vol_down.setOnClickListener {
            if (leftVolumePatient > 1 && rightVolumePatient > 1) {
                leftVolumePatient--
                rightVolumePatient--
            }
            (activity as MainActivity).saveInt(VOL_LEFT_PATIENT, leftVolumePatient)
            (activity as MainActivity).saveInt(VOL_RIGHT_PATIENT, rightVolumePatient)
            setLeftVolumeData()
            setRightVolumeData()
        }

        sync_switch.setOnClickListener {
            synced = sync_switch.isChecked
            (activity as MainActivity).saveBoolean(SYNCED, synced)
            checkSyncedStatus()
        }

    }

    private fun getData() {
        leftVolumePatient = (activity as MainActivity).getIntValue(VOL_LEFT_PATIENT)
        rightVolumePatient = (activity as MainActivity).getIntValue(VOL_RIGHT_PATIENT)
        rightVolumeClinic = (activity as MainActivity).getIntValue(VOL_RIGHT_CLINIC)
        leftVolumeClinic = (activity as MainActivity).getIntValue(VOL_LEFT_CLINIC)
        synced = (activity as MainActivity).getBoolean(SYNCED)
        sync_switch.isChecked = synced
    }

    private fun checkSyncedStatus() {
        if (synced) {
            volume_button_left_pa.visibility = GONE
            volume_button_right_pa.visibility = GONE
            volume_button_center_pa.visibility = View.VISIBLE
            calculateDelta()
        } else {
            volume_button_left_pa.visibility = View.VISIBLE
            volume_button_right_pa.visibility = View.VISIBLE
            volume_button_center_pa.visibility = GONE
        }
    }

    private fun setLeftVolumeData() {
        left_volume_pa.text = leftVolumePatient.toString()
        clinic_left_value_pa.text = leftVolumeClinic.toString()
        view_left_pa.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                leftVolumePatient.toFloat()
            )
        clinic_left_seperator_pa.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                leftVolumeClinic.toFloat()
            )
    }

    private fun setRightVolumeData() {
        right_volume_pa.text = rightVolumePatient.toString()
        clinic_right_value_pa.text = rightVolumeClinic.toString()
        view_right_pa.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                rightVolumePatient.toFloat()
            )
        clinic_right_seperator_pa.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                rightVolumeClinic.toFloat()
            )
    }


    private fun calculateDelta() {
        val deltaLeft = leftVolumePatient - leftVolumeClinic
        val deltaRight = rightVolumePatient - rightVolumeClinic
        // if deltaleft smaller than delta right
        if (deltaLeft.compareTo(deltaRight) == -1) {
            rightVolumePatient = leftVolumePatient + (rightVolumeClinic - leftVolumeClinic)
            if (rightVolumePatient < 1) {
                val deltaToIncrement = 1 - rightVolumePatient
                rightVolumePatient = rightVolumePatient + deltaToIncrement
                leftVolumePatient = leftVolumePatient + deltaToIncrement
            } else if (rightVolumePatient > 10) {
                val deltaToDecrement = rightVolumePatient - 10
                rightVolumePatient = rightVolumePatient - deltaToDecrement
                leftVolumePatient = leftVolumePatient - deltaToDecrement
            }

        }// if deltaleft greater than delta right
        else if (deltaLeft.compareTo(deltaRight) == 1) {
            leftVolumePatient = rightVolumePatient + (leftVolumeClinic - rightVolumeClinic)
            if (leftVolumePatient < 1) {
                val deltaToIncrement = 1 - leftVolumePatient
                rightVolumePatient = rightVolumePatient + deltaToIncrement
                leftVolumePatient = leftVolumePatient + deltaToIncrement
            } else if (leftVolumePatient > 10) {
                val deltaToDecrement = leftVolumePatient - 10
                rightVolumePatient = rightVolumePatient - deltaToDecrement
                leftVolumePatient = leftVolumePatient - deltaToDecrement
            }
        }

        (activity as MainActivity).saveInt(VOL_LEFT_PATIENT, leftVolumePatient)
        (activity as MainActivity).saveInt(VOL_RIGHT_PATIENT, rightVolumePatient)
        setLeftVolumeData()
        setRightVolumeData()
    }

}