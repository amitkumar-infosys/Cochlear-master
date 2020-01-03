package com.example.cochlear.ui.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.cochlear.MainActivity
import com.example.cochlear.R
import com.example.cochlear.ui.Constants.Companion.SYNCED
import com.example.cochlear.ui.Constants.Companion.VOL_LEFT_CLINIC
import com.example.cochlear.ui.Constants.Companion.VOL_LEFT_PATIENT
import com.example.cochlear.ui.Constants.Companion.VOL_RIGHT_CLINIC
import com.example.cochlear.ui.Constants.Companion.VOL_RIGHT_PATIENT
import kotlinx.android.synthetic.main.fragment_patient_control.*

class PatientFragment : Fragment() {
    private var leftVolume = 0
    private var clinicLeftVolume = 0
    private var rightVolume = 0
    private var clinicRightVolume = 0
    private var synced = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_patient_control, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // these methods are called every time activity is created/started
        getData()
        checkSyncedStatus()
        setLeftVolumeData()
        setRightVolumeData()

        //SetOnClickListeners will listen and respond to corresponding button clicks.
        left_vol_up.setOnClickListener {
            if (leftVolume < 10) {
                leftVolume++
            }
            (activity as MainActivity).saveInt(VOL_LEFT_PATIENT, leftVolume)
            setLeftVolumeData()
        }

        left_vol_down.setOnClickListener {
            if (leftVolume > 1) {
                leftVolume--
            }
            (activity as MainActivity).saveInt(VOL_LEFT_PATIENT, leftVolume)
            setLeftVolumeData()
        }

        right_vol_up.setOnClickListener {
            if (rightVolume < 10) {
                rightVolume++
            }
            (activity as MainActivity).saveInt(VOL_RIGHT_PATIENT, rightVolume)
            setRightVolumeData()
        }

        right_vol_down.setOnClickListener {
            if (rightVolume > 1) {
                rightVolume--
            }
            (activity as MainActivity).saveInt(VOL_RIGHT_PATIENT, rightVolume)
            setRightVolumeData()
        }

        //this is center volume up button
        sync_vol_up.setOnClickListener {
            if (leftVolume < 10 && rightVolume < 10) {
                leftVolume++
                rightVolume++
            }
            (activity as MainActivity).saveInt(VOL_LEFT_PATIENT, leftVolume)
            (activity as MainActivity).saveInt(VOL_RIGHT_PATIENT, rightVolume)
            setLeftVolumeData()
            setRightVolumeData()
        }

        //this is center volume down button
        sync_vol_down.setOnClickListener {
            if (leftVolume > 1 && rightVolume > 1) {
                leftVolume--
                rightVolume--
            }
            (activity as MainActivity).saveInt(VOL_LEFT_PATIENT, leftVolume)
            (activity as MainActivity).saveInt(VOL_RIGHT_PATIENT, rightVolume)
            setLeftVolumeData()
            setRightVolumeData()
        }

        sync_switch.setOnClickListener {
            synced = sync_switch.isChecked
            (activity as MainActivity).saveBoolean(SYNCED, synced)
            checkSyncedStatus()
        }

    }

    // this method retrieves all the data from Shared Preferences and updates it to the current fragment's values.
    private fun getData() {
        leftVolume = (activity as MainActivity).getIntValue(VOL_LEFT_PATIENT)
        rightVolume = (activity as MainActivity).getIntValue(VOL_RIGHT_PATIENT)
        clinicRightVolume = (activity as MainActivity).getIntValue(VOL_RIGHT_CLINIC)
        clinicLeftVolume = (activity as MainActivity).getIntValue(VOL_LEFT_CLINIC)
        synced = (activity as MainActivity).getBoolean(SYNCED)
        sync_switch.isChecked = synced
    }

    // this method checks the Bilateral Sync status.
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

    // this method sets data for left Ear's volume data on UI
    private fun setLeftVolumeData() {
        left_volume_pa.text = leftVolume.toString()
        clinic_left_value_pa.text = clinicLeftVolume.toString()
        view_left_pa.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                leftVolume.toFloat()
            )
        clinic_left_seperator_pa.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                clinicLeftVolume.toFloat()
            )
    }

    // this method sets data for right Ear's volume data on UI
    private fun setRightVolumeData() {
        right_volume_pa.text = rightVolume.toString()
        clinic_right_value_pa.text = clinicRightVolume.toString()
        view_right_pa.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                rightVolume.toFloat()
            )
        clinic_right_seperator_pa.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                clinicRightVolume.toFloat()
            )
    }

    // this method is to calculate the delta between clinical and patient values.
    private fun calculateDelta() {

        //delta here is calculated by deducting clinic setting value from patient volume.
        val deltaLeft = leftVolume - clinicLeftVolume
        val deltaRight = rightVolume - clinicRightVolume

        //Logic here is:
        // {other side patient volume = reference side patient volume + (other side patient volume - reference side clinic volume)}
        // reference volume = smaller size delta's volume

        // if delta left smaller than delta right
        if (deltaLeft.compareTo(deltaRight) == -1) {
            //reference volume here is leftVolume
            rightVolume = leftVolume + (clinicRightVolume - clinicLeftVolume)

            //here we adjust the negative patient volume by incrementing both side patient volumes by the difference between
            // minimum permissible volume and actual patient volume.
            if (rightVolume < 1) {
                val deltaToIncrement = 1 - rightVolume
                rightVolume += deltaToIncrement
                leftVolume += deltaToIncrement
            }
            //here we adjust the excessive patient volume by incrementing both side patient volumes by the difference between
            // actual patient volume and maximum permissible volume.
            else if (rightVolume > 10) {
                val deltaToDecrement = rightVolume - 10
                rightVolume -= deltaToDecrement
                leftVolume -= deltaToDecrement
            }
        }
        // if delta left greater than delta right
        else if (deltaLeft.compareTo(deltaRight) == 1) {
            //reference volume here is RightVolumePatient
            leftVolume = rightVolume + (clinicLeftVolume - clinicRightVolume)
            //here we adjust the negative patient volume by incrementing both side patient volumes by the difference between
            // minimum permissible volume and actual patient volume.
            if (leftVolume < 1) {
                val deltaToIncrement = 1 - leftVolume
                rightVolume += deltaToIncrement
                leftVolume += deltaToIncrement

            }
            //here we adjust the excessive patient volume by incrementing both side patient volumes by the difference between
            // actual patient volume and maximum permissible volume.
            else if (leftVolume > 10) {
                val deltaToDecrement = leftVolume - 10
                rightVolume -= deltaToDecrement
                leftVolume -= deltaToDecrement
            }
        }

        //saving patient volumes in Shared preferences before updating them on UI
        (activity as MainActivity).saveInt(VOL_LEFT_PATIENT, leftVolume)
        (activity as MainActivity).saveInt(VOL_RIGHT_PATIENT, rightVolume)

        //updating new values on UI
        setLeftVolumeData()
        setRightVolumeData()
    }

}